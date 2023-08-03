package com.xuecheng.content.api;


import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.Teachplanservice;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Tq
 * @Desc:  课程计划相关的接口
 * @create: 2023-08-02 10:29
 **/
@Api(value = "课程计划接口",tags = "课程计划接口")
@Slf4j
@RestController
public class TeachplanController {
    //查询课程计划
    @Autowired
    Teachplanservice teachplanservice;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        List<TeachplanDto> teachplanDtos = teachplanservice.findTeachplanTree(courseId);
        return teachplanDtos;
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan( @RequestBody SaveTeachplanDto teachplan){
        teachplanservice.saveTeachplan(teachplan);
    }


}
