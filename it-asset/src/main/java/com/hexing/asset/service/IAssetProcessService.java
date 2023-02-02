package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.Asset;
import com.hexing.common.core.domain.Result;

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
     * @param asset 资产表
     * @return 结果
     */
    int backAsset(Asset asset);
    /**
     * 资产转固
     *
     * @param asset 资产表
     * @return 结果
     */
    int fixationAsset(Asset asset);
    /**
     * 资产转移
     *
     * @param asset 资产表
     * @return 结果
     */
    int transferAsset(Asset asset,String recipient);
    /**
     * 资产待外卖
     *
     * @param asset 资产表
     * @return 结果
     */
    int waiteTakeOutAsset(Asset asset);
    /**
     * 资产已外卖
     *
     * @param asset 资产表
     * @return 结果
     */
    int takeOutAsset(Asset asset);
    /**
     * 资产返修
     *
     * @param asset 资产表
     * @return 结果
     */
    int repairAsset(Asset asset);
    /**
     * 资产操作-领用
     *
     * @param asset 资产表
     * @return 结果
     */
    int receiveAsset(Asset asset);
    /**
     * 资产操作-退货
     *
     * @param asset 资产表
     * @return 结果
     */
    int returnAsset(Asset asset);
    /**
     * 资产操作-维修
     *
     * @param asset 资产表
     * @return 结果
     */
    int maintainAsset(Asset asset);
    /**
     * 资产操作-已维修
     *
     * @param asset 资产表
     * @return 结果
     */
    int maintainedAsset(Asset asset);
    /**
     * 资产操作-报废
     *
     * @param asset 资产表
     * @return 结果
     */
    int scrapAsset(Asset asset);
    /**
     * 资产操作-已报废
     *
     * @param asset 资产表
     * @return 结果
     */
    int scrapedAsset(Asset asset);
}
