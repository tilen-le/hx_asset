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
 * 资产盘点任务流程对象 asset_process_counting_task
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetProcessCountingTask extends BaseEntity
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

    /** 盘点任务编码 */
    @Excel(name = "盘点任务编码")
    private String countingTaskCode;

    /** 盘点范围 */
    @Excel(name = "盘点范围")
    private String countingRange;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;


}
