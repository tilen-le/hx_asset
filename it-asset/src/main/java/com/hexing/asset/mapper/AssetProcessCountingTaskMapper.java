package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.AssetProcessCountingTask;

/**
 * 资产盘点任务流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessCountingTaskMapper extends BaseMapper<AssetProcessCountingTask>
{
    /**
     * 查询资产盘点任务流程
     *
     * @param id 资产盘点任务流程主键
     * @return 资产盘点任务流程
     */
    public AssetProcessCountingTask selectAssetProcessCountingTaskById(Long id);

    /**
     * 查询资产盘点任务流程列表
     *
     * @param assetProcessCountingTask 资产盘点任务流程
     * @return 资产盘点任务流程集合
     */
    public List<AssetProcessCountingTask> selectAssetProcessCountingTaskList(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 新增资产盘点任务流程
     *
     * @param assetProcessCountingTask 资产盘点任务流程
     * @return 结果
     */
    public int insertAssetProcessCountingTask(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 修改资产盘点任务流程
     *
     * @param assetProcessCountingTask 资产盘点任务流程
     * @return 结果
     */
    public int updateAssetProcessCountingTask(AssetProcessCountingTask assetProcessCountingTask);

    /**
     * 删除资产盘点任务流程
     *
     * @param id 资产盘点任务流程主键
     * @return 结果
     */
    public int deleteAssetProcessCountingTaskById(Long id);

    /**
     * 批量删除资产盘点任务流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessCountingTaskByIds(Long[] ids);
}
