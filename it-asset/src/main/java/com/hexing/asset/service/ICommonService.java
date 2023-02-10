package com.hexing.asset.service;

import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.domain.AssetProcess;

import java.util.List;

/**
 * 资产流程Service接口
 *
 * @author zxy
 * @date 2022-11-04
 */
public interface ICommonService {

    /**
     *获取流程属性字段
     */
    List<AssetProcessField> getProcessFields();

    /**
     *获取流程详情列表
     */
    List<AssetProcess> listProcessInfo(AssetProcess process, List<AssetProcessField> assetProcessFields);

    /**
     * 获取字段配置并过滤
     * @param process
     * @return
     */
    List<AssetProcessField> getSearchDomain(AssetProcess process);

    /**
     *获取流程详情列表
     */
    List<AssetProcess> searchAssetProcess(List<AssetProcessField> searchDomains, AssetProcess process);

}
