package com.hexing.asset.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

/**
 * 流程总对象 asset_process
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 流程类型 */
    @Excel(name = "流程类型")
    private String processType;

    /** 发起人工号 */
    @Excel(name = "发起人工号")
    private String userCode;

    /** 资产编码 */
    @Excel(name = "资产编码")
    private String assetCode;


}
