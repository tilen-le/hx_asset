package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;

import java.util.List;

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
     * @param process
     * @return
     */
    int saveLog(Asset asset, AssetProcess process);
    /**
     * 查询保管记录
     */
    List<AssetUpdateLog> custodyLogList(AssetProcessParam assetProcess);
    /**
     * 查询工单记录
     */
    List<AssetProcess> workLogList(AssetProcessParam assetProcess);
    /**
     * 查询操作记录
     */
    List<AssetUpdateLog> operationLogList(AssetProcessParam assetProcess);
    /**
     * 获取资产操作记录详细信息
     */
    AssetUpdateLog getOperationLogById(Long id);
}
