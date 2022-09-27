package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessReceive;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessBackMapper;
import com.hexing.asset.domain.AssetProcessBack;
import com.hexing.asset.service.IAssetProcessBackService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产归还流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessBackServiceImpl extends ServiceImpl<AssetProcessBackMapper, AssetProcessBack> implements IAssetProcessBackService
{
    @Autowired
    private AssetProcessBackMapper assetProcessBackMapper;
    @Autowired
    private AssetProcessMapper assetProcessMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询资产归还流程
     *
     * @param id 资产归还流程主键
     * @return 资产归还流程
     */
    @Override
    public AssetProcessBack selectAssetProcessBackById(Long id)
    {
        return assetProcessBackMapper.selectAssetProcessBackById(id);
    }

    /**
     * 查询资产归还流程列表
     *
     * @param assetProcessBack 资产归还流程
     * @return 资产归还流程
     */
    @Override
    public List<AssetProcessBack> selectAssetProcessBackList(AssetProcessBack assetProcessBack)
    {
        return assetProcessBackMapper.selectAssetProcessBackList(assetProcessBack);
    }

    /**
     * 新增资产归还流程
     *
     * @param assetProcessBack 资产归还流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessBack(AssetProcessBack assetProcessBack)
    {
        assetProcessBack.setCreateTime(DateUtils.getNowDate());
        return assetProcessBackMapper.insertAssetProcessBack(assetProcessBack);
    }

    /**
     * 修改资产归还流程
     *
     * @param assetProcessBack 资产归还流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessBack(AssetProcessBack assetProcessBack)
    {
        assetProcessBack.setUpdateTime(DateUtils.getNowDate());
        return assetProcessBackMapper.updateAssetProcessBack(assetProcessBack);
    }

    /**
     * 批量删除资产归还流程
     *
     * @param ids 需要删除的资产归还流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessBackByIds(Long[] ids)
    {
        return assetProcessBackMapper.deleteAssetProcessBackByIds(ids);
    }

    /**
     * 删除资产归还流程信息
     *
     * @param id 资产归还流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessBackById(Long id)
    {
        return assetProcessBackMapper.deleteAssetProcessBackById(id);
    }

    /**
     * 新增归还流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCode 平台资产编号
     */
    @Override
    @Transactional
    public void saveProcess(String instanceId, String userCode, String assetCode, String type) {
        SysUser user = sysUserMapper.getUserByUserName(userCode);
        // 新增主流程记录
        AssetProcess process = new AssetProcess()
                .setProcessType(DingTalkAssetProcessType.PROCESS_BACK.getCode())
                .setAssetCode(assetCode)
                .setUserCode(userCode)
                .setUserName(user.getNickName())
                .setCreateTime(new Date());
        if (DingTalkAssetProcessType.PROCESS_BACK_BY_ADMIN.getCode().equals(type)) {
            process.setProcessType(DingTalkAssetProcessType.PROCESS_BACK_BY_ADMIN.getCode());
        }
        assetProcessMapper.insert(process);
        // 新增归还流程记录
        AssetProcessBack processBack = new AssetProcessBack()
                .setProcessId(process.getId())
                .setInstanceId(instanceId)
                .setUserCode(userCode)
                .setUserName(user.getNickName())
                .setAssetCode(assetCode)
                .setCreateTime(new Date());
        assetProcessBackMapper.insert(processBack);
    }

}
