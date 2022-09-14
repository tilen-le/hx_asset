package com.hexing.asset.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.AssetProcessCountingTask;
import com.hexing.asset.domain.dto.AssetProcessCountingTaskDTO;

/**
 * 盘点任务Service接口
 *
 * @author zxy
 * @date 2022-09-13
 */
public interface IAssetProcessCountingTaskService extends IService<AssetProcessCountingTask>
{
    /**
     * 查询盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 盘点任务
     */
    public AssetProcessCountingTask selectAssetCountingTaskByTaskId(String taskId);

    /**
     * 查询盘点任务列表
     *
     * @param assetProcessCountingTask 盘点任务
     * @return 盘点任务集合
     */
    public List<AssetProcessCountingTask> selectAssetCountingTaskList(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 新增盘点任务
     *
     * @param dto 盘点任务DTO对象
     * @return 结果
     */
    public int insertAssetCountingTask(AssetProcessCountingTaskDTO dto);

    /**
     * 修改盘点任务
     *
     * @param assetProcessCountingTask 盘点任务
     * @return 结果
     */
    public int updateAssetCountingTask(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 批量删除盘点任务
     *
     * @param taskIds 需要删除的盘点任务主键集合
     * @return 结果
     */
    public int deleteAssetCountingTaskByTaskIds(String[] taskIds);

    /**
     * 删除盘点任务信息
     *
     * @param taskId 盘点任务主键
     * @return 结果
     */
    public int deleteAssetCountingTaskByTaskId(String taskId);
}
