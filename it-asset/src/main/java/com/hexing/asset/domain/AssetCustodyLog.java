package com.hexing.asset.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

/**
 * 资产保管记录对象 asset_custody_log
 *
 * @author zxy
 * @date 2023-02-10
 */
@Data
@EqualsAndHashCode
@ApiModel(description = "资产保管记录")
@Accessors(chain = true)
public class AssetCustodyLog
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资产保管记录主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产编码 */
    @ApiModelProperty(value = "资产编码")
    private String assetCode;

    /** 资产保管人工号 */
    @ApiModelProperty(value = "资产保管人工号")
    private String responsiblePersonCode;

    /** 资产保管人 */
    @ApiModelProperty(value = "资产保管人")
    private String responsiblePersonName;

    /** 资产保管部门*/
    @ApiModelProperty(value = "资产保管部门")
    private String responsiblePersonDept;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


}
