package com.hexing.asset.controller;

import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.vo.AssetCountingTaskVO;
import com.hexing.asset.service.IAssetCountingTaskService;
import com.hexing.asset.service.IAssetService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开放给钉钉端的rest接口
 */
@RestController
@RequestMapping("/api/dingtalk/asset")
public class DingTalkAssetController extends BaseController {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetCountingTaskService assetCountingTaskService;
    /**
     * 通过资产编码，管理部门获取资产信息
     */
    @PostMapping(value = "/getAssetsByAssetCodes")
    public String getAssetsByAssetCodes(@RequestBody JSONObject params) {
        logger.info("--------调用getAssetsByAssetCodes接口");
        String asset = assetService.getAssetsByAssetCodes(params);
        return asset;
    }

    /**
     * 新增盘点任务
     */
    @PostMapping("/createCountingTask")
    public AjaxResult createCountingTask(@RequestBody JSONObject params) {
        AssetCountingTaskVO vo = params.getObject("data", AssetCountingTaskVO.class);
        return toAjax(assetCountingTaskService.insertAssetCountingTask(vo));
    }

}
