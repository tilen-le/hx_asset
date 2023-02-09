package com.hexing.asset.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetProcess {
    private static final long serialVersionUID = 1L;

    /**
     * 资产id
     */
    @ApiModelProperty(value = "资产id")
    private String assetId;
    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码")
    private String assetCode;
    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称")
    private String assetName;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String standard;
    /**
     * 领用人工号
     */
    @ApiModelProperty(value = "保管人工号")
    private String responsiblePersonCode;
    /**
     * 领用人名称
     */
    @ApiModelProperty(value = "保管人名称")
    private String responsiblePersonName;
    /**
     * 领用人保管部门
     */
    @ApiModelProperty(value = "保管部门")
    private String responsiblePersonDept;
    /**
     * 领用人岗位
     */
    @ApiModelProperty(value = "保管人岗位")
    private String responsiblePersonJob;
    /**
     * 存放地点
     */
    @ApiModelProperty(value = "存放地点")
    private String currentLocation;
    /**
     * 出厂编码
     */
    @ApiModelProperty(value = "出厂编码")
    private String factoryNo;
    /**
     * 资产分类
     */
    @ApiModelProperty(value = "资产分类")
    private String assetCategory;
    /**
     * 成本中心
     */
    @ApiModelProperty(value = "成本中心")
    private String costCenter;
    /**
     * 所属公司
     */
    @ApiModelProperty(value = "所属公司")
    private String company;
    /**
     * 资产状态
     */
    @ApiModelProperty(value = "资产状态")
    private String assetStatus;
    /**
     * 是否转固
     */
    @ApiModelProperty(value = "是否转固")
    private Boolean fixed;
    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderNo;
    /**
     * 转固验收日期
     */
    @ApiModelProperty(value = "转固验收日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fixedAcceptanceDate;

}
