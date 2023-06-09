package com.hexing.assetNew.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetNew.domain.AssetProcess;
import com.hexing.assetNew.enums.DingTalkAssetProcessType;
import com.hexing.assetNew.mapper.AssetProcessMapper;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import com.hexing.assetNew.mapper.AssetProcessExchangeMapper;
import com.hexing.assetNew.domain.AssetProcessExchange;
import com.hexing.assetNew.service.IAssetProcessExchangeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产更换流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessExchangeServiceImpl extends ServiceImpl<AssetProcessExchangeMapper, AssetProcessExchange> implements IAssetProcessExchangeService
{
    @Autowired
    private AssetProcessExchangeMapper assetProcessExchangeMapper;
    @Autowired
    private AssetProcessMapper assetProcessMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询资产更换流程
     *
     * @param id 资产更换流程主键
     * @return 资产更换流程
     */
    @Override
    public AssetProcessExchange selectAssetProcessExchangeById(Long id)
    {
        return assetProcessExchangeMapper.selectAssetProcessExchangeById(id);
    }

    /**
     * 查询资产更换流程列表
     *
     * @param assetProcessExchange 资产更换流程
     * @return 资产更换流程
     */
    @Override
    public List<AssetProcessExchange> selectAssetProcessExchangeList(AssetProcessExchange assetProcessExchange)
    {
        return assetProcessExchangeMapper.selectAssetProcessExchangeList(assetProcessExchange);
    }

    /**
     * 新增资产更换流程
     *
     * @param assetProcessExchange 资产更换流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessExchange(AssetProcessExchange assetProcessExchange)
    {
        assetProcessExchange.setCreateTime(DateUtils.getNowDate());
        return assetProcessExchangeMapper.insertAssetProcessExchange(assetProcessExchange);
    }

    /**
     * 修改资产更换流程
     *
     * @param assetProcessExchange 资产更换流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessExchange(AssetProcessExchange assetProcessExchange)
    {
        assetProcessExchange.setUpdateTime(DateUtils.getNowDate());
        return assetProcessExchangeMapper.updateAssetProcessExchange(assetProcessExchange);
    }

    /**
     * 批量删除资产更换流程
     *
     * @param ids 需要删除的资产更换流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessExchangeByIds(Long[] ids)
    {
        return assetProcessExchangeMapper.deleteAssetProcessExchangeByIds(ids);
    }

    /**
     * 删除资产更换流程信息
     *
     * @param id 资产更换流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessExchangeById(Long id)
    {
        return assetProcessExchangeMapper.deleteAssetProcessExchangeById(id);
    }

    /**
     * 新增更换流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCodeList 平台资产编号
     */
    @Override
    @Transactional
    public int saveProcess(String instanceId, String userCode, String assetCode) {
        SysUser user = sysUserMapper.getUserByUserName(userCode);
        // 新增主流程记录
        AssetProcess process = new AssetProcess()
                .setProcessType(DingTalkAssetProcessType.PROCESS_EXCHANGE.getCode())
                .setAssetCode(assetCode)
                .setUserCode(userCode)
                .setUserName(user.getNickName())
                .setCreateTime(new Date());
        int processId = assetProcessMapper.insert(process);
        // 新增更换流程记录
        AssetProcessExchange processExchange = new AssetProcessExchange()
                .setProcessId(process.getId())
                .setInstanceId(instanceId)
                .setUserCode(userCode)
                .setUserName(user.getNickName())
                .setAssetCode(assetCode)
                .setCreateTime(new Date());
        assetProcessExchangeMapper.insert(processExchange);
        return processId;
    }

}
