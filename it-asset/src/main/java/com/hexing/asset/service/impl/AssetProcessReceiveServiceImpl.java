package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessTransfer;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessReceiveMapper;
import com.hexing.asset.domain.AssetProcessReceive;
import com.hexing.asset.service.IAssetProcessReceiveService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产领用流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessReceiveServiceImpl extends ServiceImpl<AssetProcessReceiveMapper, AssetProcessReceive> implements IAssetProcessReceiveService
{
    @Autowired
    private AssetProcessReceiveMapper assetProcessReceiveMapper;
    @Autowired
    private AssetProcessMapper assetProcessMapper;

    /**
     * 查询资产领用流程
     *
     * @param id 资产领用流程主键
     * @return 资产领用流程
     */
    @Override
    public AssetProcessReceive selectAssetProcessReceiveById(Long id)
    {
        return assetProcessReceiveMapper.selectAssetProcessReceiveById(id);
    }

    /**
     * 查询资产领用流程列表
     *
     * @param assetProcessReceive 资产领用流程
     * @return 资产领用流程
     */
    @Override
    public List<AssetProcessReceive> selectAssetProcessReceiveList(AssetProcessReceive assetProcessReceive)
    {
        return assetProcessReceiveMapper.selectAssetProcessReceiveList(assetProcessReceive);
    }

    /**
     * 新增资产领用流程
     *
     * @param assetProcessReceive 资产领用流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessReceive(AssetProcessReceive assetProcessReceive)
    {
        assetProcessReceive.setCreateTime(DateUtils.getNowDate());
        return assetProcessReceiveMapper.insertAssetProcessReceive(assetProcessReceive);
    }

    /**
     * 修改资产领用流程
     *
     * @param assetProcessReceive 资产领用流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessReceive(AssetProcessReceive assetProcessReceive)
    {
        assetProcessReceive.setUpdateTime(DateUtils.getNowDate());
        return assetProcessReceiveMapper.updateAssetProcessReceive(assetProcessReceive);
    }

    /**
     * 批量删除资产领用流程
     *
     * @param ids 需要删除的资产领用流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessReceiveByIds(Long[] ids)
    {
        return assetProcessReceiveMapper.deleteAssetProcessReceiveByIds(ids);
    }

    /**
     * 删除资产领用流程信息
     *
     * @param id 资产领用流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessReceiveById(Long id)
    {
        return assetProcessReceiveMapper.deleteAssetProcessReceiveById(id);
    }

    /**
     * 新增领用流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCode 平台资产编号
     */
    @Override
    @Transactional
    public void saveProcess(String instanceId, String userCode, String assetCode, String type) {
        // 新增主流程记录
        AssetProcess process = new AssetProcess()
                .setProcessType(DingTalkAssetProcessType.PROCESS_RECEIVE.getCode())
                .setAssetCode(assetCode)
                .setUserCode(userCode)
                .setCreateTime(new Date());
        if (DingTalkAssetProcessType.PROCESS_RECEIVE_BY_ADMIN.getCode().equals(type)) {
            process.setProcessType(DingTalkAssetProcessType.PROCESS_RECEIVE_BY_ADMIN.getCode());
        }
        assetProcessMapper.insert(process);
        // 新增领用流程记录
        AssetProcessReceive processReceive = new AssetProcessReceive()
                .setProcessId(process.getId())
                .setInstanceId(instanceId)
                .setUserCode(userCode)
                .setAssetCode(assetCode)
                .setCreateTime(new Date());
        assetProcessReceiveMapper.insert(processReceive);
    }
}
