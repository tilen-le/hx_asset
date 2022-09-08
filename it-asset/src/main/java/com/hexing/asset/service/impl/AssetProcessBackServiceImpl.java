package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessBackMapper;
import com.hexing.asset.domain.AssetProcessBack;
import com.hexing.asset.service.IAssetProcessBackService;

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
}
