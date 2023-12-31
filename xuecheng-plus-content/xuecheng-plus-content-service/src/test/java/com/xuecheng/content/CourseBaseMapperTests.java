package com.xuecheng.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseBaseMapperTests {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Test
    void testCourseBaseMapper(){
        CourseBase courseBase = courseBaseMapper.selectById(18L);
        Assertions.assertNotNull(courseBase);

        //详细进行分页查询的单元测试
        //查询条件
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");//课程名称
        courseParamsDto.setAuditStatus("202004");//课程审核状态，202004表示通过
        courseParamsDto.setPublishStatus("203001"); //课程发布状态
        //1.拼装擦好像条件
        LambdaQueryWrapper<CourseBase> queryWrapper=new LambdaQueryWrapper<>();
        //根据名称模糊查询,在sql中拼凑courese_base like ‘%值%’
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName())
                ,CourseBase::getName
                ,courseParamsDto.getCourseName());
        // 根据课程的审核状态，在sql中拼凑course_base.audit_status=?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus())
                ,CourseBase::getAuditStatus
                ,courseParamsDto.getAuditStatus());
        //根据课程的发布状态，在Sql中拼凑course_base.Pubstatus=?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus())
                ,CourseBase::getStatus
                ,courseParamsDto.getPublishStatus());
        //todo 课程发布状态查询未写（已写）
        //分页参数对象
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(2l);
        pageParams.setPageSize(2l);
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

        System.err.println(courseBasePageResult);
    }
}
