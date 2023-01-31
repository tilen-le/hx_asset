package com.hexing.assetNew.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetNew.domain.AssetInfo;
import com.hexing.assetNew.mapper.AssetInfoMapper;
import com.hexing.assetNew.service.IAssetInfoService;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 资产Service业务层处理
 *
 * @author zxy
 * @date 2022-11-01
 */
@Service
public class AssetInfoServiceImpl extends ServiceImpl<AssetInfoMapper, AssetInfo> implements IAssetInfoService
{
    @Autowired
    private AssetInfoMapper assetInfoMapper;

    /**
     * 查询资产
     *
     * @param assetId 资产主键
     * @return 资产
     */
    @Override
    public AssetInfo selectAssetInfoByAssetId(Long assetId)
    {
        return assetInfoMapper.selectAssetInfoByAssetId(assetId);
    }

    /**
     * 查询资产列表
     *
     * @param assetInfo 资产
     * @return 资产
     */
    @Override
    public List<AssetInfo> selectAssetInfoList(AssetInfo assetInfo)
    {
        return assetInfoMapper.selectAssetInfoList(assetInfo);
    }

    /**
     * 新增资产
     *
     * @param assetInfo 资产
     * @return 结果
     */
    @Override
    public int insertAssetInfo(AssetInfo assetInfo)
    {
        assetInfo.setCreateTime(DateUtils.getNowDate());
        return assetInfoMapper.insertAssetInfo(assetInfo);
    }

    /**
     * 修改资产
     *
     * @param assetInfo 资产
     * @return 结果
     */
    @Override
    public int updateAssetInfo(AssetInfo assetInfo)
    {
        assetInfo.setUpdateTime(DateUtils.getNowDate());
        return assetInfoMapper.updateAssetInfo(assetInfo);
    }

    /**
     * 批量删除资产
     *
     * @param assetIds 需要删除的资产主键
     * @return 结果
     */
    @Override
    public int deleteAssetInfoByAssetIds(Long[] assetIds)
    {
        return assetInfoMapper.deleteAssetInfoByAssetIds(assetIds);
    }

    /**
     * 删除资产信息
     *
     * @param assetId 资产主键
     * @return 结果
     */
    @Override
    public int deleteAssetInfoByAssetId(Long assetId)
    {
        return assetInfoMapper.deleteAssetInfoByAssetId(assetId);
    }
}
