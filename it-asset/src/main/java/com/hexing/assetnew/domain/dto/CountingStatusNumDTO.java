package com.hexing.assetnew.domain.dto;

import lombok.Data;

/**
 * 盘点状态统计数量DTO
 */
@Data
public class CountingStatusNumDTO {
    /**
     * 固定资产总数
     */
    Integer total = 0;
    /**
     * 待盘点资产总数
     */
    Integer notCounted = 0;
    /**
     * 已盘点资产总数
     */
    Integer counted = 0;
    /**
     * 盘点异常设备数目
     */
    Integer abnormal = 0;
}
