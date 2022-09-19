package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessDisposalMapper;
import com.hexing.asset.domain.AssetProcessDisposal;
import com.hexing.asset.service.IAssetProcessDisposalService;

/**
 * 资产处置流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessDisposalServiceImpl extends ServiceImpl<AssetProcessDisposalMapper, AssetProcessDisposal> implements IAssetProcessDisposalService
{
    @Autowired
    private AssetProcessDisposalMapper assetProcessDisposalMapper;

    /**
     * 查询资产处置流程
     *
     * @param id 资产处置流程主键
     * @return 资产处置流程
     */
    @Override
    public AssetProcessDisposal selectAssetProcessDisposalById(Long id)
    {
        return assetProcessDisposalMapper.selectAssetProcessDisposalById(id);
    }

    /**
     * 查询资产处置流程列表
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 资产处置流程
     */
    @Override
    public List<AssetProcessDisposal> selectAssetProcessDisposalList(AssetProcessDisposal assetProcessDisposal)
    {
        LambdaQueryWrapper<AssetProcessDisposal> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcessDisposal.getUserCode())) {
            wrapper.like(AssetProcessDisposal::getUserCode, assetProcessDisposal.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessDisposal.getAssetCode())) {
            wrapper.like(AssetProcessDisposal::getAssetCode, assetProcessDisposal.getAssetCode());
        }
        return assetProcessDisposalMapper.selectList(wrapper);
    }

    /**
     * 新增资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal)
    {
        assetProcessDisposal.setCreateTime(DateUtils.getNowDate());
        return assetProcessDisposalMapper.insertAssetProcessDisposal(assetProcessDisposal);
    }

    /**
     * 修改资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal)
    {
        assetProcessDisposal.setUpdateTime(DateUtils.getNowDate());
        return assetProcessDisposalMapper.updateAssetProcessDisposal(assetProcessDisposal);
    }

    /**
     * 批量删除资产处置流程
     *
     * @param ids 需要删除的资产处置流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessDisposalByIds(Long[] ids)
    {
        return assetProcessDisposalMapper.deleteAssetProcessDisposalByIds(ids);
    }

    /**
     * 删除资产处置流程信息
     *
     * @param id 资产处置流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessDisposalById(Long id)
    {
        return assetProcessDisposalMapper.deleteAssetProcessDisposalById(id);
    }
}
