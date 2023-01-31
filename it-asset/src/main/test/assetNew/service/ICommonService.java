package com.hexing.assetNew.service;

import com.hexing.assetNew.domain.AssetProcessField;
import com.hexing.assetNew.domain.AssetsProcess;

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
    List<AssetsProcess> listProcessInfo(AssetsProcess process,List<AssetProcessField> assetProcessFields);

    /**
     * 获取字段配置并过滤
     * @param process
     * @return
     */
    List<AssetProcessField> getSearchDomain(AssetsProcess process);

    /**
     *获取流程详情列表
     */
    List<AssetsProcess> searchAssetProcess(List<AssetProcessField> searchDomains, AssetsProcess process);

}
