package com.hexing.asset.service;

import java.util.List;
import com.hexing.asset.domain.Asset;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产表Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetService extends IService<Asset>
{
    /**
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    public Asset selectAssetByAssetId(Long assetId);

    /**
     * 查询资产表列表
     *
     * @param asset 资产表
     * @return 资产表集合
     */
    public List<Asset> selectAssetList(Asset asset);

    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    public int insertAsset(Asset asset);

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    public int updateAsset(Asset asset);

    /**
     * 批量删除资产表
     *
     * @param assetIds 需要删除的资产表主键集合
     * @return 结果
     */
    public int deleteAssetByAssetIds(Long[] assetIds);

    /**
     * 删除资产表信息
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    public int deleteAssetByAssetId(Long assetId);
}
