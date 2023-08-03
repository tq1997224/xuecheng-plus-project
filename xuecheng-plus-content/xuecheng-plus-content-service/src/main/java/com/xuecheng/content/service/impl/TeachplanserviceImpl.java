package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.Teachplanservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Tq
 * @Desc:课程计划接口
 * @create: 2023-08-02 12:23
 **/
@Slf4j
@Service
public class TeachplanserviceImpl implements Teachplanservice {

    @Autowired
    TeachplanMapper teachplanMapper;
    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过课程计划的id是新增还是修改
        Long teachplanId = saveTeachplanDto.getId();

        if (teachplanId==null){

                //新增
                Teachplan teachplan = new Teachplan();
                BeanUtils.copyProperties(saveTeachplanDto, teachplan);

                //确定排序字段，找到同级节点个数加一
                Long parentid = saveTeachplanDto.getParentid();
                Long courseId = saveTeachplanDto.getCourseId();
                int teachplanCount = getTeachplanCount(parentid, courseId);
                teachplan.setOrderby(teachplanCount);
                teachplanMapper.insert(teachplan);

        }else {
            //修改
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            //将参数复制到teachplan
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }

    private int getTeachplanCount( Long parentid, Long courseId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentid);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count+1;
    }
}
