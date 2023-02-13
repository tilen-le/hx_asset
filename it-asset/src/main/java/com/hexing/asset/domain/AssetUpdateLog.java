package com.hexing.asset.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String processType;

    /** 主流程id */
    private String processId;

}
