package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

/**
 * 流程值对象 asset_process_variable
 *
 * @author zxy
 * @date 2022-11-03
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetProcessVariable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 流程id */
    @Excel(name = "流程id")
    private String processId;

    /** 字段id */
    @Excel(name = "字段id")
    private String fieldId;

    /** 字段值 */
    @Excel(name = "字段值")
    private String fieldValue;

}
