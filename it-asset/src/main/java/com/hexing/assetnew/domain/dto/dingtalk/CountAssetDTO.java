package com.hexing.assetnew.domain.dto.dingtalk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description= "提交盘点流程请求参数DTO")
public class CountAssetDTO {

    @ApiModelProperty(value = "资产管理员工号")
    private String taskName;

    @ApiModelProperty(value = "资产编码")
    private String assetCode;

    @ApiModelProperty(value = "发起人工号")
    private String userCode;

    @ApiModelProperty(value = "实例Id")
    private String instanceId;

    @ApiModelProperty(value = "资产信息列表")
    private List<CountAssetSimpleDTO> assets;

}
