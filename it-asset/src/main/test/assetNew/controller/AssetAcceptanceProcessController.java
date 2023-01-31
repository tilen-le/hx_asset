package com.hexing.assetNew.controller;

import com.hexing.assetNew.domain.Asset;
import com.hexing.common.core.controller.BaseController;

import java.util.List;

/**
 * 资产验收流程Controller
 */
public class AssetAcceptanceProcessController extends BaseController {

    /**
     * TODO 发起验收申请，支持批量
     *
     * @param assetIdList 资产ID列表
     * @return
     */
    public String acceptanceApply(List<String> assetIdList) {
        return null;
    }

    /**
     * TODO 定时任务：试用到期发起验收申请
     *
     * @return
     */
    public String notificationJob() {
        return null;
    }

    /**
     * TODO 验收成功的资产信息推送SAP
     *
     * @param assetList 资产列表
     * @return SAP响应消息
     */
    public String syncAcceptedAssetInfoToSAP(List<Asset> assetList) {
        return null;
    }

}
