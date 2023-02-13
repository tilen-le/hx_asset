package com.hexing.asset.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 资产转移 VO
 *
 * @author sty
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description = "资产转移VO")
public class AssetTransferVO {
    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码")
    private String assetCode;
    /**
     * 接收公司
     */
    @ApiModelProperty(value = "接收公司")
    private String receiveCompany;
    /**
     * 接收人
     */
    @ApiModelProperty(value = "接收人")
    private String receiveEmployee;
    /**
     * 接收人部门
     */
    @ApiModelProperty(value = "接收人部门")
    private String receiveDept;
    /**
     * 接收人岗位
     */
    @ApiModelProperty(value = "接收人岗位")
    private String receiverPosition;
    /**
     * 成本中心
     */
    @ApiModelProperty(value = "成本中心")
    private String costCenter;
    /**
     * 所在位置
     */
    @ApiModelProperty(value = "所在位置（新位置）")
    private String newLocation;
}
