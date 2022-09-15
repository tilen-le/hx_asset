package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.constant.AssetConstants;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.dto.AssetInventoryTaskDTO;
import com.hexing.asset.mapper.AssetInventoryTaskMapper;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.asset.service.IAssetInventoryTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 盘点任务Service业务层处理
 *
 * @author zxy
 * @date 2022-09-13
 */
@Service
public class AssetInventoryTaskServiceImpl extends ServiceImpl<AssetInventoryTaskMapper, AssetInventoryTask> implements IAssetInventoryTaskService
{



    @Autowired
    private AssetInventoryTaskMapper assetInventoryTaskMapper;
    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private IAssetProcessCountingService assetProcessCountingService;

    /**
     * 查询盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 盘点任务
     */
    @Override
    public AssetInventoryTask selectAssetCountingTaskByTaskId(String taskId)
    {
        return assetInventoryTaskMapper.selectAssetCountingTaskByTaskId(taskId);
    }

    /**
     * 查询盘点任务列表
     *
     * @param assetInventoryTask 盘点任务
     * @return 盘点任务
     */
    @Override
    public List<AssetInventoryTask> selectAssetCountingTaskList(AssetInventoryTask assetInventoryTask)
    {
        QueryWrapper<AssetInventoryTask> wrapper = new QueryWrapper<>();
        return assetInventoryTaskMapper.selectList(wrapper);
    }

    /**
     * 新增盘点任务
     */
    @Override
    public int insertAssetCountingTask(AssetInventoryTaskDTO dto)
    {
        AssetInventoryTask task = new AssetInventoryTask();
        BeanUtils.copyProperties(dto, task);

        // 统计待盘点资产总数
        task.setAssetCounted(0);   /* 已盘点资产数 */
        task.setAssetAbnormal(0);  /* 异常资产数目 */
        task.setAssetNotCounted(0); /* 待盘点资产数 */

        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        wrapper.in("responsible_person_code", dto.getResponsiblePersonCodes());
        List<Asset> assetList = assetMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(assetList)) {
            task.setAssetNotCounted(assetList.size());
        }

        task.setCreateTime(new Date());

        for (Asset asset : assetList) {
            AssetProcessCounting entity = new AssetProcessCounting();
            entity.setTaskCode(task.getTaskCode());
            entity.setAssetCode(asset.getAssetCode());
            entity.setCreateTime(new Date());
            entity.setCountingStatus(AssetConstants.COUNTING_STATUS_NOT_COUNTED);
            assetProcessCountingService.insertAssetProcessCounting(entity);
        }

        return assetInventoryTaskMapper.insert(task);
    }

    /**
     * 修改盘点任务
     *
     * @param assetInventoryTask 盘点任务
     * @return 结果
     */
    @Override
    public int updateAssetCountingTask(AssetInventoryTask assetInventoryTask)
    {
        return assetInventoryTaskMapper.updateAssetCountingTask(assetInventoryTask);
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
        return assetInventoryTaskMapper.deleteAssetCountingTaskByTaskIds(taskIds);
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
        return assetInventoryTaskMapper.deleteAssetCountingTaskByTaskId(taskId);
    }
}
