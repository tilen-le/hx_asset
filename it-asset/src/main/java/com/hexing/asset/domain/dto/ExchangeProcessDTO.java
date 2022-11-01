package com.hexing.asset.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(description= "更换流程请求参数DTO")
public class ExchangeProcessDTO {

    @ApiModelProperty(value = "资产管理员工号")
    private String adminCode;

    @ApiModelProperty(value = "流程申请人工号")
    private String userCode;

    @ApiModelProperty(value = "实例ID")
    private String instanceId;

    @ApiModelProperty(value = "旧资产信息")
    private AssetInfoSimpleDO before;

    @ApiModelProperty(value = "新资产信息")
    private AssetInfoSimpleDO after;


}

