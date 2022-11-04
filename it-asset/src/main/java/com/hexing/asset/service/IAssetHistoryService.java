package com.hexing.asset.service;

import java.util.List;

import com.hexing.asset.domain.AssetHistory;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产历史记录Service接口
 *
 * @author zxy
 * @date 2022-11-01
 */
public interface IAssetHistoryService extends IService<AssetHistory>
{
    /**
     * 查询资产历史记录
     *
     * @param id 资产历史记录主键
     * @return 资产历史记录
     */
    public AssetHistory selectAssetHistoryById(Long id);

    /**
     * 查询资产历史记录列表
     *
     * @param assetHistory 资产历史记录
     * @return 资产历史记录集合
     */
    public List<AssetHistory> selectAssetHistoryList(AssetHistory assetHistory);

    /**
     * 新增资产历史记录
     *
     * @param assetHistory 资产历史记录
     * @return 结果
     */
    public int insertAssetHistory(AssetHistory assetHistory);

    /**
     * 修改资产历史记录
     *
     * @param assetHistory 资产历史记录
     * @return 结果
     */
    public int updateAssetHistory(AssetHistory assetHistory);

    /**
     * 批量删除资产历史记录
     *
     * @param ids 需要删除的资产历史记录主键集合
     * @return 结果
     */
    public int deleteAssetHistoryByIds(Long[] ids);

    /**
     * 删除资产历史记录信息
     *
     * @param id 资产历史记录主键
     * @return 结果
     */
    public int deleteAssetHistoryById(Long id);
}
