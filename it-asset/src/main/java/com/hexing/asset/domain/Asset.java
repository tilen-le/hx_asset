package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
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
public class Asset {

    private static final long serialVersionUID = 1L;

    /**
     * 资产id
     */
    @TableId(type = IdType.AUTO)
    private String assetId;

    /**
     * 资产名称
     */
    @Excel(name = "资产名称")
    private String assetName;

    /**
     * 资产编码
     */
    @Excel(name = "资产编码", type = Excel.Type.EXPORT)
    private String assetCode;

    /**
     * SAP固定资产编码
     */
    private String assetSapCode;

    /**
     * 资产大类
     */
    private String assetType;

    /**
     * 资产中类
     */
    private String assetCategory;

    /**
     * 资产小类
     */
    private String assetSubCategory;

    /**
     * 规格型号
     */
    private String standard;

    /**
     * 资产状态
     */
    private String assetStatus;

    /**
     * 转固状态
     */
    private Boolean fixed;

    /**
     * 保管人工号（老工号）
     */
    @Excel(name = "资产保管人工号")
    private String responsiblePersonCode;

    /**
     * 保管人姓名
     */
    @Excel(name = "资产保管人", type = Excel.Type.EXPORT)
    private String responsiblePersonName;

    /**
     * 资产保管部门
     */
    private String responsiblePersonDept;

    /**
     * 所在位置
     */
    private String currentLocation;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 成本中心
     */
    private String costCenter;

    /**
     * 资产原值(含税)
     */
    private Double originalValue;

    /**
     * 资产净值
     */
    private Double netValue;

    /**
     * 资产原值币制
     */
    private String monetaryUnit;

    /**
     * 资产化日期（资本化日期/资产价值录入日期）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date capitalizationDate;

    /**
     * 预计使用寿命（年）
     */
    private String canUseYears;

    /**
     * 预计使用寿命（月）
     */
    private String canUseMonths;

    /**
     * 保修期
     */
    private Integer warranty;

    /**
     * 供应商
     */
    private String provider;

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 出厂编码
     */
    private String factoryNo;

    /**
     * 采购单号
     */
    private String purchaseOrderNo;

    /**
     * 首次购置日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;

    /**
     * 入库日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date storageDate;

    /**
     * 备注
     */
    private String comment;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 物料号
     */
    private String materialNum;

    /**
     * 流水号
     */
    private Integer serialNum;

}
