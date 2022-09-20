package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcessMaintain;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessTransformMapper;
import com.hexing.asset.domain.AssetProcessTransform;
import com.hexing.asset.service.IAssetProcessTransformService;

/**
 * 资产改造流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-20
 */
@Service
public class AssetProcessTransformServiceImpl extends ServiceImpl<AssetProcessTransformMapper, AssetProcessTransform> implements IAssetProcessTransformService
{
    @Autowired
    private AssetProcessTransformMapper assetProcessTransformMapper;

    /**
     * 查询资产改造流程
     *
     * @param id 资产改造流程主键
     * @return 资产改造流程
     */
    @Override
    public AssetProcessTransform selectAssetProcessTransformById(Long id)
    {
        return assetProcessTransformMapper.selectAssetProcessTransformById(id);
    }

    /**
     * 查询资产改造流程列表
     *
     * @param assetProcessTransform 资产改造流程
     * @return 资产改造流程
     */
    @Override
    public List<AssetProcessTransform> selectAssetProcessTransformList(AssetProcessTransform assetProcessTransform)
    {
        LambdaQueryWrapper<AssetProcessTransform> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcessTransform.getUserCode())) {
            wrapper.like(AssetProcessTransform::getUserCode, assetProcessTransform.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessTransform.getAssetCode())) {
            wrapper.like(AssetProcessTransform::getAssetCode, assetProcessTransform.getAssetCode());
        }
        return assetProcessTransformMapper.selectList(wrapper);
    }

    /**
     * 新增资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessTransform(AssetProcessTransform assetProcessTransform)
    {
        return assetProcessTransformMapper.insert(assetProcessTransform);
    }

    /**
     * 修改资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessTransform(AssetProcessTransform assetProcessTransform)
    {
        assetProcessTransform.setUpdateTime(DateUtils.getNowDate());
        return assetProcessTransformMapper.updateAssetProcessTransform(assetProcessTransform);
    }

    /**
     * 批量删除资产改造流程
     *
     * @param ids 需要删除的资产改造流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessTransformByIds(Long[] ids)
    {
        return assetProcessTransformMapper.deleteAssetProcessTransformByIds(ids);
    }

    /**
     * 删除资产改造流程信息
     *
     * @param id 资产改造流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessTransformById(Long id)
    {
        return assetProcessTransformMapper.deleteAssetProcessTransformById(id);
    }
}
