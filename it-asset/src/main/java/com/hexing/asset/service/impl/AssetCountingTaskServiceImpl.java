package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetCountingTask;
import com.hexing.asset.domain.vo.AssetCountingTaskVO;
import com.hexing.asset.mapper.AssetCountingTaskMapper;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetCountingTaskService;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
public class AssetCountingTaskServiceImpl extends ServiceImpl<AssetCountingTaskMapper, AssetCountingTask> implements IAssetCountingTaskService
{
    @Autowired
    private AssetCountingTaskMapper assetCountingTaskMapper;
    @Autowired
    private AssetMapper assetMapper;

    /**
     * 查询盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 盘点任务
     */
    @Override
    public AssetCountingTask selectAssetCountingTaskByTaskId(String taskId)
    {
        return assetCountingTaskMapper.selectAssetCountingTaskByTaskId(taskId);
    }

    /**
     * 查询盘点任务列表
     *
     * @param assetCountingTask 盘点任务
     * @return 盘点任务
     */
    @Override
    public List<AssetCountingTask> selectAssetCountingTaskList(AssetCountingTask assetCountingTask)
    {
        return assetCountingTaskMapper.selectAssetCountingTaskList(assetCountingTask);
    }

    /**
     * 新增盘点任务
     */
    @Override
    public int insertAssetCountingTask(AssetCountingTaskVO vo)
    {
        AssetCountingTask task = new AssetCountingTask();
        BeanUtils.copyProperties(vo, task);

        // 统计待盘点资产总数
        task.setAssetCounted(0);   /* 已盘点资产数 */
        task.setAssetAbnormal(0);  /* 异常资产数目 */

        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        wrapper.in("responsible_person_code", vo.getResponsiblePersonCodes());
        Integer assetNotCounted = assetMapper.selectCount(wrapper);
        task.setAssetNotCounted(assetNotCounted);   /* 待盘点资产数 */

        task.setCreateTime(new Date());

        return assetCountingTaskMapper.insert(task);
    }

    /**
     * 修改盘点任务
     *
     * @param assetCountingTask 盘点任务
     * @return 结果
     */
    @Override
    public int updateAssetCountingTask(AssetCountingTask assetCountingTask)
    {
        return assetCountingTaskMapper.updateAssetCountingTask(assetCountingTask);
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
        return assetCountingTaskMapper.deleteAssetCountingTaskByTaskIds(taskIds);
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
        return assetCountingTaskMapper.deleteAssetCountingTaskByTaskId(taskId);
    }
}
