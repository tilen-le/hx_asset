package com.hexing.asset.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

/**
 * 资产归还流程对象 asset_process_back
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetProcessBack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 流程总表id */
    @Excel(name = "流程总表id")
    private Long processId;

    /** 实例ID */
    @Excel(name = "实例ID")
    private String instanceId;

    /** 发起人工号 */
    @Excel(name = "发起人工号")
    private String userCode;

    /** 平台资产编码 */
    @Excel(name = "平台资产编码")
    private String assetCode;


}
