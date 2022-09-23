package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessExchange;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessTransferMapper;
import com.hexing.asset.domain.AssetProcessTransfer;
import com.hexing.asset.service.IAssetProcessTransferService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产转移流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-20
 */
@Service
public class AssetProcessTransferServiceImpl extends ServiceImpl<AssetProcessTransferMapper, AssetProcessTransfer> implements IAssetProcessTransferService
{
    @Autowired
    private AssetProcessTransferMapper assetProcessTransferMapper;
    @Autowired
    private AssetProcessMapper assetProcessMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询资产转移流程
     *
     * @param id 资产转移流程主键
     * @return 资产转移流程
     */
    @Override
    public AssetProcessTransfer selectAssetProcessTransferById(Long id)
    {
        return assetProcessTransferMapper.selectAssetProcessTransferById(id);
    }

    /**
     * 查询资产转移流程列表
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 资产转移流程
     */
    @Override
    public List<AssetProcessTransfer> selectAssetProcessTransferList(AssetProcessTransfer assetProcessTransfer)
    {
        return assetProcessTransferMapper.selectAssetProcessTransferList(assetProcessTransfer);
    }

    /**
     * 新增资产转移流程
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessTransfer(AssetProcessTransfer assetProcessTransfer)
    {
        assetProcessTransfer.setCreateTime(DateUtils.getNowDate());
        return assetProcessTransferMapper.insertAssetProcessTransfer(assetProcessTransfer);
    }

    /**
     * 修改资产转移流程
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessTransfer(AssetProcessTransfer assetProcessTransfer)
    {
        assetProcessTransfer.setUpdateTime(DateUtils.getNowDate());
        return assetProcessTransferMapper.updateAssetProcessTransfer(assetProcessTransfer);
    }

    /**
     * 批量删除资产转移流程
     *
     * @param ids 需要删除的资产转移流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessTransferByIds(Long[] ids)
    {
        return assetProcessTransferMapper.deleteAssetProcessTransferByIds(ids);
    }

    /**
     * 删除资产转移流程信息
     *
     * @param id 资产转移流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessTransferById(Long id)
    {
        return assetProcessTransferMapper.deleteAssetProcessTransferById(id);
    }

    /**
     * 新增转移流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCode 平台资产编号
     */
    @Override
    @Transactional
    public void saveProcess(String instanceId, String userCode, String assetCode) {
        // 新增主流程记录
        SysUser user = sysUserMapper.getUserByUserName(userCode);
        AssetProcess process = new AssetProcess()
                .setProcessType(DingTalkAssetProcessType.PROCESS_TRANSFER.getCode())
                .setAssetCode(assetCode)
                .setUserCode(userCode)
                .setUserName(user.getNickName())
                .setCreateTime(new Date());
        assetProcessMapper.insert(process);
        // 新增更换流程记录
        AssetProcessTransfer processTransfer = new AssetProcessTransfer()
                .setProcessId(process.getId())
                .setInstanceId(instanceId)
                .setUserCode(userCode)
                .setUserName(user.getNickName())
                .setAssetCode(assetCode)
                .setCreateTime(new Date());
        assetProcessTransferMapper.insert(processTransfer);
    }
}
