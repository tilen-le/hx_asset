package com.hexing.assetNew.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetNew.domain.AssetHistory;
import com.hexing.assetNew.mapper.AssetHistoryMapper;
import com.hexing.assetNew.service.IAssetHistoryService;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 资产历史记录Service业务层处理
 *
 * @author zxy
 * @date 2022-11-01
 */
@Service
public class AssetHistoryServiceImpl extends ServiceImpl<AssetHistoryMapper, AssetHistory> implements IAssetHistoryService
{
    @Autowired
    private AssetHistoryMapper assetHistoryMapper;

    /**
     * 查询资产历史记录
     *
     * @param id 资产历史记录主键
     * @return 资产历史记录
     */
    @Override
    public AssetHistory selectAssetHistoryById(Long id)
    {
        return assetHistoryMapper.selectAssetHistoryById(id);
    }

    /**
     * 查询资产历史记录列表
     *
     * @param assetHistory 资产历史记录
     * @return 资产历史记录
     */
    @Override
    public List<AssetHistory> selectAssetHistoryList(AssetHistory assetHistory)
    {
        return assetHistoryMapper.selectAssetHistoryList(assetHistory);
    }

    /**
     * 新增资产历史记录
     *
     * @param assetHistory 资产历史记录
     * @return 结果
     */
    @Override
    public int insertAssetHistory(AssetHistory assetHistory)
    {
        assetHistory.setCreateTime(DateUtils.getNowDate());
        return assetHistoryMapper.insertAssetHistory(assetHistory);
    }

    /**
     * 修改资产历史记录
     *
     * @param assetHistory 资产历史记录
     * @return 结果
     */
    @Override
    public int updateAssetHistory(AssetHistory assetHistory)
    {
        return assetHistoryMapper.updateAssetHistory(assetHistory);
    }

    /**
     * 批量删除资产历史记录
     *
     * @param ids 需要删除的资产历史记录主键
     * @return 结果
     */
    @Override
    public int deleteAssetHistoryByIds(Long[] ids)
    {
        return assetHistoryMapper.deleteAssetHistoryByIds(ids);
    }

    /**
     * 删除资产历史记录信息
     *
     * @param id 资产历史记录主键
     * @return 结果
     */
    @Override
    public int deleteAssetHistoryById(Long id)
    {
        return assetHistoryMapper.deleteAssetHistoryById(id);
    }
}
