package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: Tq
 * @Desc: 课程计划信息模型类
 * @create: 2023-08-02 10:13
 **/
@Data
@ToString
public class TeachplanDto extends Teachplan {
    //与媒资管理的信息
    private TeachplanMedia teachplanMedia;
    //小章节list
    private List<TeachplanMedia> teachPlanTreeNodes;
}
