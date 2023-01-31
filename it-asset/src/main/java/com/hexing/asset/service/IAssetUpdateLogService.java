package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetUpdateLog;

/**
 * 资产信息更新日志Service接口
 *
 * @author zxy
 * @date 2022-10-19
 */
public interface IAssetUpdateLogService extends IService<AssetUpdateLog>
{
    /**
     * 创建资产信息更新日志
     *
     * @param asset
     * @param processId
     * @return
     */
    boolean saveLog(Asset asset, String processId);
}
