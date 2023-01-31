package com.hexing.assetNew.service;

import java.util.List;

import com.hexing.assetNew.domain.AssetInfo;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产Service接口
 *
 * @author zxy
 * @date 2022-11-01
 */
public interface IAssetInfoService extends IService<AssetInfo>
{
    /**
     * 查询资产
     *
     * @param assetId 资产主键
     * @return 资产
     */
    public AssetInfo selectAssetInfoByAssetId(Long assetId);

    /**
     * 查询资产列表
     *
     * @param assetInfo 资产
     * @return 资产集合
     */
    public List<AssetInfo> selectAssetInfoList(AssetInfo assetInfo);

    /**
     * 新增资产
     *
     * @param assetInfo 资产
     * @return 结果
     */
    public int insertAssetInfo(AssetInfo assetInfo);

    /**
     * 修改资产
     *
     * @param assetInfo 资产
     * @return 结果
     */
    public int updateAssetInfo(AssetInfo assetInfo);

    /**
     * 批量删除资产
     *
     * @param assetIds 需要删除的资产主键集合
     * @return 结果
     */
    public int deleteAssetInfoByAssetIds(Long[] assetIds);

    /**
     * 删除资产信息
     *
     * @param assetId 资产主键
     * @return 结果
     */
    public int deleteAssetInfoByAssetId(Long assetId);
}
