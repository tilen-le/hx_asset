package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessReceiveMapper;
import com.hexing.asset.domain.AssetProcessReceive;
import com.hexing.asset.service.IAssetProcessReceiveService;

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
}
