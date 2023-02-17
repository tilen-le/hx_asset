package com.hexing.asset.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 资产转固 VO
 *
 * @author sty
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description = "资产转固VO")
public class AssetFixVO {
    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码")
    private String assetCode;
    /**
     * 资产类型
     */
    @ApiModelProperty(value = "资产类型")
    private String category;
    /**
     * 成本中心编码
     */
    @ApiModelProperty(value = "成本中心编码")
    private String costCenterCode;
    /**
     * 成本中心描述
     */
    @ApiModelProperty(value = "成本中心描述")
    private String costCenterName;
    /**
     * 保质期到期时间
     */
    @ApiModelProperty(value = "保质期到期时间")
    private Date expirationDate;
    /**
     * 责任成本中心编码
     */
    @ApiModelProperty(value = "责任成本中心编码")
    private String responsibilityCostCenterCode;
    /**
     * 归属项目
     */
    @ApiModelProperty(value = "归属项目")
    private String belong;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String provider;
    /**
     * 用途
     */
    @ApiModelProperty(value = "用途")
    private String usage;
}
