package com.hexing.asset.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetProcessParam {
    private static final long serialVersionUID = 1L;

    /**
     * 流程总表id
     */
    private Long id;
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
     * 成本中心
     */
    @ApiModelProperty(value = "成本中心")
    private String costCenter;
    /**
     * 成本中心描述
     */
    @ApiModelProperty(value = "成本中心描述")
    private String costCenterName;
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
    private String fixed;
    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderNo;
    /**
     * 资产管理分类
     */
    @ApiModelProperty(value = "资产管理分类")
    private String assetManagementCategory;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号")
    private String wokeCode;
    /**
     * sap资产编码
     */
    @ApiModelProperty(value = "sap资产编码")
    private String sapCode;

    //转固（用途） 派发（领用说明）
    @ApiModelProperty(value = "转固（用途）;派发（领用说明）")
    private String comment;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //资产派发字段 领用人 领用部门 领用人岗位  所在位置 资产名称 规格型号 资产出厂编号 转固验收日期 领用说明
    /**
     * 转固验收日期
     */
    @ApiModelProperty(value = "转固验收日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fixedAcceptanceDate;
    /**
     * 出厂编码
     */
    @ApiModelProperty(value = "出厂编码")
    private String factoryNo;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String standard;

    //转移字段 接收公司 接收人 接收人部门 接收人岗位 成本中心 所在位置

    //转固字段 资产类型 成本中心编码  保质期到期时间  责任成本中心编码  归属项目 供应商名称 用途

    @ApiModelProperty(value = "责任成本中心编码")
    private String dutyCostCenter;

    @ApiModelProperty(value = "保质期到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityTime;

    @ApiModelProperty(value = "归属项目")
    private String project;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    //闲置 是否清空资产保管人和保管部门，成本中心
    @ApiModelProperty(value = "是否清空")
    private String clearInfo;

    @ApiModelProperty(value = "资产大类")
    private String assetType;

    @ApiModelProperty(value = "资产中类")
    private String assetCategory;

    @ApiModelProperty(value = "资产小类")
    private String assetSubCategory;


}
