package com.hexing.asset.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 资产盘点流程对象 asset_process_counting
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetProcessCounting
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /** 流程总表id */
    private Long processId;

    /** 实例ID */
    private String instanceId;

    /** 盘点任务编码 */
    @Excel(name = "盘点任务编码")
    private String taskCode;

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

    /** 盘点状态 */
    @Excel(name = "盘点状态")
    private String countingStatus;

    /** 盘点时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "盘点时间")
    private Date countingTime;

    /** 备注 */
    private String comment;

}
