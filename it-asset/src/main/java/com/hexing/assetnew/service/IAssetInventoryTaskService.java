package com.hexing.assetnew.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetnew.domain.AssetInventoryTask;
import com.hexing.assetnew.domain.AssetProcessField;
import com.hexing.assetnew.domain.dto.CountingStatusNumDTO;

/**
 * 盘点任务Service接口
 *
 * @author zxy
 * @date 2022-09-13
 */
public interface IAssetInventoryTaskService extends IService<AssetInventoryTask> {
    /**
     * 查询盘点任务列表
     */
    List<AssetInventoryTask> selectAssetCountingTaskList(AssetInventoryTask assetInventoryTask);

    /**
     * 新增盘点任务
     */
    boolean insertAssetCountingTask(AssetInventoryTask assetInventoryTask);

    /**
     * 批量删除盘点任务
     */
    int deleteAssetCountingTaskByTaskIds(List<String> taskCode);

    /**
     * 更新盘点任务状态
     */
    void updateInventoryTaskStatus();

    /**
     * 盘点状态统计
     *
     * @param taskCode 盘点任务编号
     * @return
     */
    CountingStatusNumDTO countingStatusCount(String taskCode, List<AssetProcessField> processFields);

}
