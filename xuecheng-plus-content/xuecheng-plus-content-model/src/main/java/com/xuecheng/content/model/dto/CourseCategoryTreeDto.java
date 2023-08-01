package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * @author: Tq
 * @Desc: TODO 课程分类dto
 * @create: 2023-07-31 19:05
 **/

@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable{
    //子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
