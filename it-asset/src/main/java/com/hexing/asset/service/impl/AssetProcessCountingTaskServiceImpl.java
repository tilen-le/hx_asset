package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessCountingTaskMapper;
import com.hexing.asset.domain.AssetProcessCountingTask;
import com.hexing.asset.service.IAssetProcessCountingTaskService;

/**
 * 资产盘点任务流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessCountingTaskServiceImpl extends ServiceImpl<AssetProcessCountingTaskMapper, AssetProcessCountingTask> implements IAssetProcessCountingTaskService
{
    @Autowired
    private AssetProcessCountingTaskMapper assetProcessCountingTaskMapper;

    /**
     * 查询资产盘点任务流程
     *
     * @param id 资产盘点任务流程主键
     * @return 资产盘点任务流程
     */
    @Override
    public AssetProcessCountingTask selectAssetProcessCountingTaskById(Long id)
    {
        return assetProcessCountingTaskMapper.selectAssetProcessCountingTaskById(id);
    }

    /**
     * 查询资产盘点任务流程列表
     *
     * @param assetProcessCountingTask 资产盘点任务流程
     * @return 资产盘点任务流程
     */
    @Override
    public List<AssetProcessCountingTask> selectAssetProcessCountingTaskList(AssetProcessCountingTask assetProcessCountingTask)
    {
        return assetProcessCountingTaskMapper.selectAssetProcessCountingTaskList(assetProcessCountingTask);
    }

    /**
     * 新增资产盘点任务流程
     *
     * @param assetProcessCountingTask 资产盘点任务流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessCountingTask(AssetProcessCountingTask assetProcessCountingTask)
    {
        assetProcessCountingTask.setCreateTime(DateUtils.getNowDate());
        return assetProcessCountingTaskMapper.insertAssetProcessCountingTask(assetProcessCountingTask);
    }

    /**
     * 修改资产盘点任务流程
     *
     * @param assetProcessCountingTask 资产盘点任务流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessCountingTask(AssetProcessCountingTask assetProcessCountingTask)
    {
        assetProcessCountingTask.setUpdateTime(DateUtils.getNowDate());
        return assetProcessCountingTaskMapper.updateAssetProcessCountingTask(assetProcessCountingTask);
    }

    /**
     * 批量删除资产盘点任务流程
     *
     * @param ids 需要删除的资产盘点任务流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingTaskByIds(Long[] ids)
    {
        return assetProcessCountingTaskMapper.deleteAssetProcessCountingTaskByIds(ids);
    }

    /**
     * 删除资产盘点任务流程信息
     *
     * @param id 资产盘点任务流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingTaskById(Long id)
    {
        return assetProcessCountingTaskMapper.deleteAssetProcessCountingTaskById(id);
    }
}
