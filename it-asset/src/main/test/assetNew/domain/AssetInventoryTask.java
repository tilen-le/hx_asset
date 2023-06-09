package com.hexing.assetNew.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 盘点任务对象 asset_counting_task
 *
 * @author zxy
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetInventoryTask
{
    private static final long serialVersionUID = 1L;

    /** 盘点任务编码 */
    @TableId
    @Excel(name = "盘点任务编码")
    private String taskCode;

    @Excel(name = "盘点任务名称")
    private String taskName;

    /** 盘点人 */
    private String inventoryUsers;

    /** 盘点人姓名 */
    @TableField(exist = false)
    private String inventoryUsersName;

    /** 盘点范围 */
    @Excel(name = "盘点范围")
    private String inventoryDept;

    /** 盘点范围名称 */
    @TableField(exist = false)
    private String inventoryDeptName;

    /** 已盘点资产数 */
    @TableField(exist = false)
    @Excel(name = "固定资产总数")
    private Integer assetTotal;

    /** 已盘点资产数 */
    @TableField(exist = false)
    @Excel(name = "已盘点资产数")
    private Integer assetCounted;

    /** 待盘点资产数 */
    @TableField(exist = false)
    @Excel(name = "待盘点资产数")
    private Integer assetNotCounted;

    /** 异常资产数目 */
    @TableField(exist = false)
    @Excel(name = "异常资产数目")
    private Integer assetAbnormal;

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

    /** 发起人 */
    @Excel(name = "发起人")
    private String createBy;

    /** 发起人姓名 */
    private String creatorName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 盘点人 */
    @TableField(exist = false)
    private List<String> inventoryUserList;
}
