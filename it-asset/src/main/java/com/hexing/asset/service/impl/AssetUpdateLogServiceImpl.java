package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.mapper.AssetUpdateLogMapper;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资产信息更新日志Service业务层处理
 *
 * @author zxy
 * @date 2022-10-19
 */
@Service
public class AssetUpdateLogServiceImpl extends ServiceImpl<AssetUpdateLogMapper, AssetUpdateLog> implements IAssetUpdateLogService
{
    @Autowired
    private AssetUpdateLogMapper assetUpdateLogMapper;

    /**
     * 查询资产信息更新日志
     *
     * @param id 资产信息更新日志主键
     * @return 资产信息更新日志
     */
    @Override
    public AssetUpdateLog selectAssetUpdateLogById(Long id)
    {
        return assetUpdateLogMapper.selectAssetUpdateLogById(id);
    }

    /**
     * 查询资产信息更新日志列表
     *
     * @param assetUpdateLog 资产信息更新日志
     * @return 资产信息更新日志
     */
    @Override
    public List<AssetUpdateLog> selectAssetUpdateLogList(AssetUpdateLog assetUpdateLog)
    {
        return assetUpdateLogMapper.selectAssetUpdateLogList(assetUpdateLog);
    }

    /**
     * 新增资产信息更新日志
     *
     * @param assetUpdateLog 资产信息更新日志
     * @return 结果
     */
    @Override
    public int insertAssetUpdateLog(AssetUpdateLog assetUpdateLog)
    {
        return assetUpdateLogMapper.insertAssetUpdateLog(assetUpdateLog);
    }

    /**
     * 修改资产信息更新日志
     *
     * @param assetUpdateLog 资产信息更新日志
     * @return 结果
     */
    @Override
    public int updateAssetUpdateLog(AssetUpdateLog assetUpdateLog)
    {
        assetUpdateLog.setUpdateTime(DateUtils.getNowDate());
        return assetUpdateLogMapper.updateAssetUpdateLog(assetUpdateLog);
    }

    /**
     * 批量删除资产信息更新日志
     *
     * @param ids 需要删除的资产信息更新日志主键
     * @return 结果
     */
    @Override
    public int deleteAssetUpdateLogByIds(Long[] ids)
    {
        return assetUpdateLogMapper.deleteAssetUpdateLogByIds(ids);
    }

    /**
     * 删除资产信息更新日志信息
     *
     * @param id 资产信息更新日志主键
     * @return 结果
     */
    @Override
    public int deleteAssetUpdateLogById(Long id)
    {
        return assetUpdateLogMapper.deleteAssetUpdateLogById(id);
    }
}
