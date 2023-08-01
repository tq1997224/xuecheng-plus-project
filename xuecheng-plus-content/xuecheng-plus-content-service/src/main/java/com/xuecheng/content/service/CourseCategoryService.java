package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;


/**
 * @description TODO 课程分类接口
 */
public interface CourseCategoryService {
    /**
     *  课程分类树行结构查询
     * @param id
     * @return
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
