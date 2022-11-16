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
     * 根据公司代码和财务资产编码更新资产信息
     *
     * @param asset
     * @return
     */
    public int updateAssetByCompanyCodeAndFinancialAssetCode(Asset asset);

    /**
     * 删除资产表
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    public int deleteAssetByAssetId(Long assetId);

    /**
     * 查询用户资产
     */
    public List selectAssetsByUserCodes(List userCodes);
}