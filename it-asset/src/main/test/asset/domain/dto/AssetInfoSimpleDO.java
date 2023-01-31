package com.hexing.assetNew.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(description= "")
public class AssetInfoSimpleDO {

    @ApiModelProperty(value = "存放地点")
    private String location;

    @ApiModelProperty(value = "平台资产编号")
    private String assetCode;

}
