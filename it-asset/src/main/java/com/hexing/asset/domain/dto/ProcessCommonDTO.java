package com.hexing.asset.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "流程公共请求参数DTO")
public class ProcessCommonDTO<T> {

    @ApiModelProperty(value = "data")
    private T data;

}
