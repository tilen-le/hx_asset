package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 资产表对象 asset
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description = "资产表对象")
public class Asset {

    private static final long serialVersionUID = 1L;

    /**
     * 资产id
     */
    @ApiModelProperty(value = "资产id")
    @TableId(type = IdType.AUTO)
    private String assetId;

    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称")
    @Excel(name = "资产名称")
    private String assetName;

    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码")
    @Excel(name = "资产编码", type = Excel.Type.EXPORT)
    private String assetCode;

    /**
     * SAP资产编号
     */
    @ApiModelProperty(value = "SAP资产编号")
    @Excel(name = "SAP资产编号")
    private String sapCode;

    /**
     * SAP固定资产编码
     */
    @ApiModelProperty(value = "SAP固定资产编码")
    @Excel(name = "SAP固定资产编码")
    private String sapAssetCode;

    /**
     * 资产大类
     */
    @ApiModelProperty(value = "资产大类")
    @Excel(name = "资产大类")
    private String assetType;

    /**
     * 资产中类
     */
    @ApiModelProperty(value = "资产中类")
    @Excel(name = "资产中类", isExport = true)
    private String assetCategory;

    /**
     * 资产小类
     */
    @ApiModelProperty(value = "资产小类")
    @Excel(name = "资产小类", isExport = true)
    private String assetSubCategory;

    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    @Excel(name = "规格型号")
    private String standard;

    /**
     * 资产状态
     */
    @ApiModelProperty(value = "资产状态")
    @Excel(name = "资产状态", dictType = "asset_status")
    private String assetStatus;

    /**
     * 转固状态
     */
    @ApiModelProperty(value = "转固状态")
    @Excel(name = "转固状态", dictType = "asset_fixed")
    private String fixed;

    /**
     * 资产保管人工号（老工号）
     */
    @ApiModelProperty(value = "资产保管人工号（老工号）")
    @Excel(name = "资产保管人工号")
    private String responsiblePersonCode;

    /**
     * 资产保管人
     */
    @ApiModelProperty(value = "资产保管人")
    @Excel(name = "资产保管人")
    private String responsiblePersonName;

    /**
     * 资产保管部门
     */
    @ApiModelProperty(value = "资产保管部门")
    @Excel(name = "资产保管部门")
    private String responsiblePersonDept;

    /**
     * 所在位置
     */
    @ApiModelProperty(value = "所在位置")
    @Excel(name = "所在位置")
    private String currentLocation;

    /**
     * 所属公司
     */
    @ApiModelProperty(value = "所属公司")
    @Excel(name = "所属公司", dictType = "asset_company")
    private String company;

    /**
     * 成本中心
     */
    @ApiModelProperty(value = "成本中心")
    @Excel(name = "成本中心")
    private String costCenter;

    /**
     * 成本中心描述
     */
    @ApiModelProperty(value = "成本中心描述")
    @Excel(name = "成本中心描述")
    private String costCenterName;

    /**
     * 资产原值(含税)
     */
    @ApiModelProperty(value = "资产原值(含税)")
    @Excel(name = "资产原值")
    private String originalValue;

    /**
     * 资产净值
     */
    @ApiModelProperty(value = "资产净值")
    @Excel(name = "资产净值")
    private String netValue;

    /**
     * 资产原值币制
     */
    @ApiModelProperty(value = "资产原值币制")
    @Excel(name = "资产原值币制")
    private String monetaryUnit;

    /**
     * 资产化日期（资本化日期/资产价值录入日期）
     */
    @ApiModelProperty(value = "资产化日期（资本化日期/资产价值录入日期）")
    @Excel(name = "资产化日期", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date capitalizationDate;

    /**
     * 预计使用寿命（年）
     */
    @ApiModelProperty(value = "预计使用寿命（年）")
    @Excel(name = "预计使用寿命（年）")
    private String canUseYears;

    /**
     * 预计使用寿命（月）
     */
    @ApiModelProperty(value = "预计使用寿命（月）")
    @Excel(name = "预计使用寿命（月）")
    private String canUseMonths;

    /**
     * 质保期到期时间
     */
    @ApiModelProperty(value = "质保期到期时间")
    @Excel(name = "质保期到期时间")
    private Date warranty;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    @Excel(name = "供应商")
    private String provider;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    @Excel(name = "供应商名称")
    private String providerName;

    /**
     * 出厂编码
     */
    @ApiModelProperty(value = "出厂编码")
    @Excel(name = "出厂编码")
    private String factoryNo;

    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    @Excel(name = "采购单号")
    private String purchaseOrderNo;

    /**
     * 首次购置日期
     */
    @ApiModelProperty(value = "首次购置日期")
    @Excel(name = "首次购置日期", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;

    /**
     * 入库日期
     */
    @ApiModelProperty(value = "入库日期")
    @Excel(name = "入库日期", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date storageDate;

    /**
     * 物料凭证
     */
    @ApiModelProperty(value = "物料凭证")
    @Excel(name = "物料凭证")
    private String proofOfMaterial;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    @Excel(name = "单位")
    private String unit;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    @Excel(name = "物料号")
    private String materialNum;

    /**
     * 正确的物料号
     */
    @ApiModelProperty(value = "正确的物料号")
    @Excel(name = "正确的物料号")
    @TableField(exist = false)
    private String currentMaterialNum;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    private Integer serialNum;

    /**
     * 资产管理员
     */
    @ApiModelProperty(value = "资产管理员")
    @Excel(name = "资产管理员")
    @TableField(exist = false)
    private String assetManager;

    /**
     * 资产管理部门
     */
    @ApiModelProperty(value = "资产管理部门")
    @Excel(name = "资产管理部门")
    @TableField(exist = false)
    private String assetManagementDept;

    /**
     * 折旧
     */
    @ApiModelProperty(value = "折旧")
    private String depreciation;

    /**
     * 转移标识
     */
    @ApiModelProperty(value = "转移标识")
    @TableField(exist = false)
    private String transfer;

    /**
     * 责任成本中心
     */
    @ApiModelProperty(value = "责任成本中心")
    @Excel(name = "责任成本中心")
    private String responsibleCostCenter;

    /**
     * 责任成本中心描述
     */
    @ApiModelProperty(value = "责任成本中心描述")
    @Excel(name = "责任成本中心描述")
    private String responsibleCostCenterName;

    /**
     * 管理部门描述
     */
    @ApiModelProperty(value = "管理部门描述")
    @Excel(name = "管理部门描述")
    private String manageDeptName;

    /**
     * 出厂编号
     */
    @ApiModelProperty(value = "出厂编号")
    @Excel(name = "出厂编号")
    private String factoryNumber;


}
