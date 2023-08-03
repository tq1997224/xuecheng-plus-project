package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

public interface Teachplanservice {

    public List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 新增、修改、保存课程计划的接口
     * @param saveTeachplanDto
     */
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto);
}
