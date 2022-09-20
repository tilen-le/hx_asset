package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcessDisposal;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessMaintainMapper;
import com.hexing.asset.domain.AssetProcessMaintain;
import com.hexing.asset.service.IAssetProcessMaintainService;

/**
 * 资产维修流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessMaintainServiceImpl extends ServiceImpl<AssetProcessMaintainMapper, AssetProcessMaintain> implements IAssetProcessMaintainService
{
    @Autowired
    private AssetProcessMaintainMapper assetProcessMaintainMapper;

    /**
     * 查询资产维修流程
     *
     * @param id 资产维修流程主键
     * @return 资产维修流程
     */
    @Override
    public AssetProcessMaintain selectAssetProcessMaintainById(Long id)
    {
        return assetProcessMaintainMapper.selectAssetProcessMaintainById(id);
    }

    /**
     * 查询资产维修流程列表
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 资产维修流程
     */
    @Override
    public List<AssetProcessMaintain> selectAssetProcessMaintainList(AssetProcessMaintain assetProcessMaintain)
    {
        LambdaQueryWrapper<AssetProcessMaintain> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcessMaintain.getUserCode())) {
            wrapper.like(AssetProcessMaintain::getUserCode, assetProcessMaintain.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessMaintain.getAssetCode())) {
            wrapper.like(AssetProcessMaintain::getAssetCode, assetProcessMaintain.getAssetCode());
        }
        return assetProcessMaintainMapper.selectList(wrapper);
    }

    /**
     * 新增资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain)
    {
        return assetProcessMaintainMapper.insert(assetProcessMaintain);
    }

    /**
     * 修改资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain)
    {
        assetProcessMaintain.setUpdateTime(DateUtils.getNowDate());
        return assetProcessMaintainMapper.updateAssetProcessMaintain(assetProcessMaintain);
    }

    /**
     * 批量删除资产维修流程
     *
     * @param ids 需要删除的资产维修流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessMaintainByIds(Long[] ids)
    {
        return assetProcessMaintainMapper.deleteAssetProcessMaintainByIds(ids);
    }

    /**
     * 删除资产维修流程信息
     *
     * @param id 资产维修流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessMaintainById(Long id)
    {
        return assetProcessMaintainMapper.deleteAssetProcessMaintainById(id);
    }
}
