package com.hexing.asset.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

/**
 * 资产信息更新日志对象 asset_update_log
 *
 * @author zxy
 * @date 2022-10-19
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetUpdateLog extends Asset
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 主流程类型 */
    @ApiModelProperty(value = "资产id")
    private String processType;

    /** 主流程id */
    @ApiModelProperty(value = "资产id")
    private String processId;


    /** 主流程id */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date endTime;


}
