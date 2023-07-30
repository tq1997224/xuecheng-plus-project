package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @description TODO 课程查询参数Dto模型类
 * @author Mr.M
 * @date 2022/9/6 14:36
 * @version 1.0
 */
@Data
@ToString
public class QueryCourseParamsDto {

    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publishStatus;

}