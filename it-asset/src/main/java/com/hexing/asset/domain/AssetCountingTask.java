package com.hexing.asset.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

/**
 * 盘点任务对象 asset_counting_task
 *
 * @author zxy
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetCountingTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 盘点任务id */
    private String taskId;

    /** 盘点任务编码 */
    @Excel(name = "盘点任务编码")
    private String taskCode;

    /** 发起人 */
    @Excel(name = "发起人")
    private String userCode;

    /** 盘点范围 */
    @Excel(name = "盘点范围")
    private String inventoryRange;

    /** 已盘点资产数 */
    @Excel(name = "已盘点资产数")
    private Long assetCounted;

    /** 待盘点资产数 */
    @Excel(name = "待盘点资产数")
    private Long assetNotCounted;

    /** 异常资产数目 */
    @Excel(name = "异常资产数目")
    private Long assetAbnormal;

    /** 盘点开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 盘点结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 盘点状态 */
    @Excel(name = "盘点状态")
    private String status;


}
