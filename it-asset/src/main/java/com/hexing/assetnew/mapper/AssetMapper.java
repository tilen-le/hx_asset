package com.hexing.assetnew.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.assetnew.domain.Asset;

/**
 * 资产表Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetMapper extends BaseMapper<Asset> {

    /**
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    Asset selectAssetByAssetId(Long assetId);

    /**
     * 查询资产表列表
     *
     * @param asset 资产表
     * @return 资产表集合
     */
    List<Asset> selectAssetList(Asset asset);

    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    int insertAsset(Asset asset);

    /**
     * 删除资产表
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    int deleteAssetByAssetId(Long assetId);

}
