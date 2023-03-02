package com.hexing.asset.domain.vo.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(description = "资产编辑流程VO")
public class EditProcessVO {

    @ApiModelProperty(value = "资产大类")
    private String assetType;

    @ApiModelProperty(value = "资产中类")
    private String assetCategory;

    @ApiModelProperty(value = "资产小类")
    private String assetSubCategory;

}
