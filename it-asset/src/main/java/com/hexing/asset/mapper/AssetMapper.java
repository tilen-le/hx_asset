package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.Asset;

/**
 * 资产表Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetMapper extends BaseMapper<Asset>
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
     * 删除资产表
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    public int deleteAssetByAssetId(Long assetId);

    /**
     * 批量删除资产表
     *
     * @param assetIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetByAssetIds(Long[] assetIds);
}
