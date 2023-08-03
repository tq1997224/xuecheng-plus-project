package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Tq
 * @Desc:
 * @create: 2023-08-01 17:19
 **/
@Data
public class EditCourseDto extends AddCourseDto{
    @ApiModelProperty(value = "课程id", required = true)
    private Long id;
}
