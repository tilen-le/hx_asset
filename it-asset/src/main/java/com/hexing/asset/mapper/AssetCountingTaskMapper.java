package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.asset.domain.AssetProcessCountingTask;
import org.springframework.stereotype.Repository;

/**
 * 盘点任务Mapper接口
 *
 * @author zxy
 * @date 2022-09-13
 */
@Repository
public interface AssetCountingTaskMapper extends BaseMapper<AssetProcessCountingTask>
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
     * @param assetProcessCountingTask 盘点任务
     * @return 结果
     */
    public int insertAssetCountingTask(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 修改盘点任务
     *
     * @param assetProcessCountingTask 盘点任务
     * @return 结果
     */
    public int updateAssetCountingTask(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 删除盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 结果
     */
    public int deleteAssetCountingTaskByTaskId(String taskId);

    /**
     * 批量删除盘点任务
     *
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetCountingTaskByTaskIds(String[] taskIds);
}
