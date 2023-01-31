package com.hexing.assetNew.domain.dto.dingtalk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "提交盘点流程资产简单实体DTO")
public class CountAssetSimpleDTO {

    @ApiModelProperty(value = "平台资产编号")
    private String assetCode;

    @ApiModelProperty(value = "错误信息修改框")
    private String comment;

}
