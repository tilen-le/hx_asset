package com.hexing.assetnew.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetnew.domain.AssetInventoryTask;

/**
 * 盘点任务Service接口
 *
 * @author zxy
 * @date 2022-09-13
 */
public interface IAssetInventoryTaskService extends IService<AssetInventoryTask>
{
    /**
     * 查询盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 盘点任务
     */
    public AssetInventoryTask selectAssetCountingTaskByTaskId(String taskId);

    /**
     * 查询盘点任务列表
     *
     * @param assetInventoryTask 盘点任务
     * @return 盘点任务集合
     */
    public List<AssetInventoryTask> selectAssetCountingTaskList(AssetInventoryTask assetInventoryTask);

    /**
     * 新增盘点任务
     * @return 结果
     */
    public boolean insertAssetCountingTask(AssetInventoryTask assetInventoryTask);

    /**
     * 批量删除盘点任务
     *
     * @param taskCode 需要删除的盘点任务主键集合
     * @return 结果
     */
    public int deleteAssetCountingTaskByTaskIds(List<String> taskCode);

    /**
     * 更新盘点任务状态
     */
    void updateInventoryTaskStatus();
}
