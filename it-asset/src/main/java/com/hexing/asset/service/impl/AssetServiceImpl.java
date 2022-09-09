package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.service.IAssetService;

/**
 * 资产表Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService
{
    @Autowired
    private AssetMapper assetMapper;

    /**
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    @Override
    public Asset selectAssetByAssetId(Long assetId)
    {
        return assetMapper.selectAssetByAssetId(assetId);
    }

    /**
     * 查询资产表列表
     *
     * @param asset 资产表
     * @return 资产表
     */
    @Override
    public List<Asset> selectAssetList()
    {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        return assetMapper.selectList(wrapper);
    }

    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    @Override
    public int insertAsset(Asset asset)
    {
        asset.setCreateTime(DateUtils.getNowDate());
        return assetMapper.insertAsset(asset);
    }

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    @Override
    public int updateAsset(Asset asset)
    {
        asset.setUpdateTime(DateUtils.getNowDate());
        return assetMapper.updateAsset(asset);
    }

    /**
     * 批量删除资产表
     *
     * @param assetIds 需要删除的资产表主键
     * @return 结果
     */
    @Override
    public int deleteAssetByAssetIds(Long[] assetIds)
    {
        return assetMapper.deleteAssetByAssetIds(assetIds);
    }

    /**
     * 删除资产表信息
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    @Override
    public int deleteAssetByAssetId(Long assetId)
    {
        return assetMapper.deleteAssetByAssetId(assetId);
    }

    /**
     * TODO 资产信息导入
     *
     * @param assetList 资产信息列表
     * @param isUpdateSupport 是否存在则覆盖
     * @param operName 操作人姓名
     * @return
     */
    @Override
    public String importAsset(List<Asset> assetList, Boolean isUpdateSupport, String operName) {
        return null;
    }
}
