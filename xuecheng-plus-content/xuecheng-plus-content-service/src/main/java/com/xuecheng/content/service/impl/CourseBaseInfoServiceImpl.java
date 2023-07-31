package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryCourBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        //详细进行分页查询的单元测试
        //1.拼装擦好像条件
        LambdaQueryWrapper<CourseBase> queryWrapper=new LambdaQueryWrapper<>();
        //根据名称模糊查询,在sql中拼凑courese_base like ‘%值%’
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName())
                ,CourseBase::getName
                ,courseParamsDto.getCourseName());
        // 根据课程的审核状态，在sql中擦好像course_base.audit_status=?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus())
                ,CourseBase::getAuditStatus
                ,courseParamsDto.getAuditStatus());
        //根据课程的发布状态，在Sql中拼凑course_base.Pubstatus=?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus())
                ,CourseBase::getStatus
                ,courseParamsDto.getPublishStatus());
        //todo 课程发布状态查询未写(已写)

        //创建page分页参数对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        //开始进行分页查询
        Page<CourseBase> pageresult=courseBaseMapper.selectPage(page,queryWrapper);

        //数据列表
        List<CourseBase> items = pageresult.getRecords();
        //总记录数
        long total = pageresult.getTotal();

        //List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(items,total,pageParams.getPageNo(), pageParams.getPageSize());



        return courseBasePageResult;
    }
}
