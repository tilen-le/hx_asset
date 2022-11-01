package com.hexing.asset.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(description= "领用流程请求参数DTO")
public class ReceiveProcessDTO {

    @ApiModelProperty(value = "资产编号")
    private String assetCode;

    @ApiModelProperty(value = "实例Id")
    private String instanceId;

    @ApiModelProperty(value = "管理部门")
    private String manageDept;

    @ApiModelProperty(value = "流程类型")
    private String processType;

    @ApiModelProperty(value = "流程发起人工号")
    private String userCode;

    @ApiModelProperty(value = "保管人工号")
    private String responsiblePersonCode;

    @ApiModelProperty(value = "资产使用场景")
    private String usageScenario;

    @ApiModelProperty(value = "存放地点")
    private String location;

}
