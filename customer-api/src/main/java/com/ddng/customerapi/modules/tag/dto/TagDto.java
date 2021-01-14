package com.ddng.customerapi.modules.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("태그 DTO")
public class TagDto
{
    @ApiModelProperty(value = "태그", dataType = "String", example = "귀여움")
    private String title;
}
