package com.hexing.asset.domain;

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

import java.util.Date;

/**
 * 资产处置流程对象 asset_process_disposal
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@Accessors(chain = true)
public class AssetProcessDisposal
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
