package com.hexing.assetNew.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 资产历史记录对象 asset_history
 *
 * @author zxy
 * @date 2022-11-01
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description= "资产历史记录")
public class AssetHistory {
    private static final long serialVersionUID = 1L;

    /** 资产历史id */
    private Long id;

    /** 固定资产名称 */
    @ApiModelProperty(name = "固定资产名称")
    private String assetName;

    /** 平台资产编号 */
    @ApiModelProperty(name = "平台资产编号")
    private String assetCode;

    /** 财务资产编号 */
    @ApiModelProperty(name = "财务资产编号")
    private String financialAssetCode;

    /** 保管人姓名 */
    @ApiModelProperty(name = "保管人姓名")
    private String responsiblePersonName;

    /** 资产状态 */
    @ApiModelProperty(name = "资产状态")
    private String assetStatus;

    /** 公司代码 */
    @ApiModelProperty(name = "公司代码")
    private String companyCode;

    /** 公司名 */
    @ApiModelProperty(name = "公司名")
    private String companyName;

    /** 存放地点 */
    @ApiModelProperty(name = "存放地点")
    private String location;

    /** 成本中心（编号） */
    @ApiModelProperty(name = "成本中心")
    private String costCenter;

    /** 成本中心描述 */
    @ApiModelProperty(name = "成本中心描述")
    private String costCenterName;

    /** 资产使用场景 */
    @ApiModelProperty(name = "资产使用场景")
    private String usageScenario;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 备注 */
    private String remark;
}
