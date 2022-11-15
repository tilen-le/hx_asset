package com.hexing.asset.domain.dto;

import com.hexing.assetnew.domain.Asset;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(description= "用户信息及名下资产请求参数DTO")
public class UserAssetInfoDTO {

    @ApiModelProperty(value = "保管人工号（老工号）")
    private String userCode;

    @ApiModelProperty(value = "管理部门")
    private String manageDept;

    @ApiModelProperty(value = "资产信息列表", hidden = true)
    private List<Asset> assets;

}
