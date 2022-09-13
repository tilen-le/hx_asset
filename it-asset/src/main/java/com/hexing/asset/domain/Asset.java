package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import com.hexing.common.core.domain.BaseEntity;
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
public class Asset
{
    private static final long serialVersionUID = 1L;

    /** 资产id */
    @TableId(type= IdType.UUID)
    private String assetId;

    /** 公司代码 */
    @Excel(name = "*公司代码")         /*  name属性值中，前缀”*“表示通过excel导入时该字段必填  */
    private String companyCode;

    /** 公司代码描述 */
    @Excel(name = "*公司代码描述")
    private String companyName;

    /** 固定资产名称 */
    @Excel(name = "*固定资产名称")
    private String assetName;

    /** 平台资产编号 */
    @Excel(name = "平台资产编号", type= Excel.Type.EXPORT)
    private String assetCode;

    /** 财务资产编号 */
    @Excel(name = "*财务资产编号")
    private String financialAssetCode;

    /** 保管人工号（老工号） */
    @Excel(name = "保管人工号")
    private String responsiblePersonNo;

    /** 资产分类描述 */
    @Excel(name = "*资产分类描述")
    private String category;

    /** 资产状态描述 */
    @Excel(name = "资产状态描述")
    private String assetStatus;

    /** 出厂编号 */
    @Excel(name = "出厂编号")
    private String factoryNo;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String standard;

    /** 单位 */
    @Excel(name = "单位")
    private String measure;

    /** 采购人（工号） */
    @Excel(name = "采购人")
    private String buyer;

    /** 采购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购日期", width = 30, dateFormat = "yyyy/MM/dd")
    private Date buyDate;

    /** 资产总价值 */
    @Excel(name = "*资产总价值")
    private Double totalValue;

    /** 净值 */
    @Excel(name = "*净值")
    private Double netWorth;

    /** 保修期（月） */
    @Excel(name = "保修期（月）")
    private Integer warranty;

    /** 预计使用寿命（月） */
    @Excel(name = "预计使用寿命（月）")
    private Integer canUseMonths;

    /** 预计使用寿命（年） */
    @Excel(name = "预计使用寿命（年）")
    private Integer canUseYears;

    /** 资本化日期/资产价值录入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*资本化日期/资产价值录入日期", width = 30, dateFormat = "yyyy/MM/dd")
    private Date capitalizationDate;

    /** 资产原值币制 */
    @Excel(name = "资产原值币制")
    private String monetaryUnit;

    /** 存放地点 */
    @Excel(name = "存放地点")
    private String location;

    /** 供应商 */
    @Excel(name = "供应商")
    private String provider;

    /** 数量 */
    @Excel(name = "数量")
    private Integer amount;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 成本中心（编号） */
    @Excel(name = "成本中心")
    private String costCenter;

    /** 成本中心描述 */
    @Excel(name = "成本中心描述")
    private String costCenterName;

    /** 管理部门描述 */
    @Excel(name = "*管理部门描述")
    private String manageDept;

    /** 合同单号 */
    @Excel(name = "合同单号", type= Excel.Type.EXPORT)
    private String contractNo;

    /** 申请人（工号） */
    @Excel(name = "申请人")
    private String proposer;

    /** 资产使用场景 */
    @Excel(name = "资产使用场景")
    private String usageScenario;

    /** 备注 */
    @Excel(name = "备注")
    private String comment;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
