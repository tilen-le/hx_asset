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

}
