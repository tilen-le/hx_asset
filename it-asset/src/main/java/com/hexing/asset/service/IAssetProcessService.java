package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;

import java.util.List;

/**
 * 资产表Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessService extends IService<Asset> {

    /**
     * 资产归还
     *
     * 
     * @return 结果
     */
    int backAsset(AssetProcess assetProcess);
    /**
     * 资产转固
     *
     * 
     * @return 结果
     */
    int fixationAsset(AssetProcess assetProcess);
    /**
     * 资产转移
     *
     * 
     * @return 结果
     */
    int transferAsset(AssetProcess assetProcess);
    /**
     * 资产待外卖
     *
     *
     * @return 结果
     */
    int waiteTakeOutAsset(AssetProcess assetProcess);
    /**
     * 资产已外卖
     *
     * 
     * @return 结果
     */
    int takeOutAsset(AssetProcess assetProcess);
    /**
     * 资产返修
     *
     * 
     * @return 结果
     */
    int repairAsset(AssetProcess assetProcess);
    /**
     * 资产操作-派发
     *
     * @param assetProcess 资产表
     * @return 结果
     */
    int receiveAsset(AssetProcess assetProcess);
    /**
     * 资产操作-退货
     *
     * 
     * @return 结果
     */
    int returnAsset(AssetProcess assetProcess);
    /**
     * 资产操作-维修
     *
     * 
     * @return 结果
     */
    int maintainAsset(AssetProcess assetProcess);
    /**
     * 资产操作-已维修
     *
     * 
     * @return 结果
     */
    int maintainedAsset(AssetProcess assetProcess);
    /**
     * 资产操作-报废
     *
     * 
     * @return 结果
     */
    int scrapAsset(AssetProcess assetProcess);
    /**
     * 资产操作-已报废
     *
     * 
     * @return 结果
     */
    int scrapedAsset(AssetProcess assetProcess);
}
