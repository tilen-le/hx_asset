package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcessCountingTask;
import com.hexing.asset.domain.dto.AssetProcessCountingTaskDTO;
import com.hexing.asset.mapper.AssetProcessCountingTaskMapper;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetProcessCountingTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 盘点任务Service业务层处理
 *
 * @author zxy
 * @date 2022-09-13
 */
@Service
public class AssetProcessCountingTaskServiceImpl extends ServiceImpl<AssetProcessCountingTaskMapper, AssetProcessCountingTask> implements IAssetProcessCountingTaskService
{
    @Autowired
    private AssetProcessCountingTaskMapper assetProcessCountingTaskMapper;
    @Autowired
    private AssetMapper assetMapper;

    /**
     * 查询盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 盘点任务
     */
    @Override
    public AssetProcessCountingTask selectAssetCountingTaskByTaskId(String taskId)
    {
        return assetProcessCountingTaskMapper.selectAssetCountingTaskByTaskId(taskId);
    }

    /**
     * 查询盘点任务列表
     *
     * @param assetProcessCountingTask 盘点任务
     * @return 盘点任务
     */
    @Override
    public List<AssetProcessCountingTask> selectAssetCountingTaskList(AssetProcessCountingTask assetProcessCountingTask)
    {
        QueryWrapper<AssetProcessCountingTask> wrapper = new QueryWrapper<>();
        return assetProcessCountingTaskMapper.selectList(wrapper);
    }

    /**
     * 新增盘点任务
     */
    @Override
    public int insertAssetCountingTask(AssetProcessCountingTaskDTO dto)
    {
        AssetProcessCountingTask task = new AssetProcessCountingTask();
        BeanUtils.copyProperties(dto, task);

        // 统计待盘点资产总数
        task.setAssetCounted(0);   /* 已盘点资产数 */
        task.setAssetAbnormal(0);  /* 异常资产数目 */

        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        wrapper.in("responsible_person_code", dto.getResponsiblePersonCodes());
        Integer assetNotCounted = assetMapper.selectCount(wrapper);
        task.setAssetNotCounted(assetNotCounted);   /* 待盘点资产数 */

        task.setCreateTime(new Date());

        return assetProcessCountingTaskMapper.insert(task);
    }

    /**
     * 修改盘点任务
     *
     * @param assetProcessCountingTask 盘点任务
     * @return 结果
     */
    @Override
    public int updateAssetCountingTask(AssetProcessCountingTask assetProcessCountingTask)
    {
        return assetProcessCountingTaskMapper.updateAssetCountingTask(assetProcessCountingTask);
    }

    /**
     * 批量删除盘点任务
     *
     * @param taskIds 需要删除的盘点任务主键
     * @return 结果
     */
    @Override
    public int deleteAssetCountingTaskByTaskIds(String[] taskIds)
    {
        return assetProcessCountingTaskMapper.deleteAssetCountingTaskByTaskIds(taskIds);
    }

    /**
     * 删除盘点任务信息
     *
     * @param taskId 盘点任务主键
     * @return 结果
     */
    @Override
    public int deleteAssetCountingTaskByTaskId(String taskId)
    {
        return assetProcessCountingTaskMapper.deleteAssetCountingTaskByTaskId(taskId);
    }
}
