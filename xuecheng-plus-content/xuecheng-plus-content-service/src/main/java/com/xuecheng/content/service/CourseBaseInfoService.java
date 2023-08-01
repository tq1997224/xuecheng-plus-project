package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @description TODO 课程信息管理接口
 */
public interface CourseBaseInfoService {

    /**
     * 课程分页查询
     * @param pageParams 分页查询参数
     * @param courseParamsDto 查询条件
     * @return  查询的返回结果
     */
    public PageResult<CourseBase> queryCourBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto);

    /**
     *  新增课程
     * @param companyId 机构id
     * @param addCourseDto 课程信息
     * @return 课程详细信息
     */

    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);

}
