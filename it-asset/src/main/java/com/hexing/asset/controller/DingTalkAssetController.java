package com.hexing.asset.controller;

import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.dto.AssetProcessCountingTaskDTO;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetProcessCountingTaskService;
import com.hexing.asset.service.IAssetProcessCountingService;
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
    private IAssetProcessCountingTaskService assetProcessCountingTaskService;
    @Autowired
    private IAssetProcessCountingService assetProcessCountingService;

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
     * 获取人员信息
     */
    @PostMapping(value = "/getAssets")
    public JSONObject getAssets(@RequestBody JSONObject params) {
        logger.info("--------调用getAssets接口");
        JSONObject result = assetService.getAssets(params);
        JSONObject R = new JSONObject();
        R.put("result", result);
        return R;
    }


    /**
     * 新增盘点任务
     */
    @PostMapping("/createCountingTask")
    public AjaxResult createCountingTask(@RequestBody JSONObject params) {
        AssetProcessCountingTaskDTO dto = params.getObject("data", AssetProcessCountingTaskDTO.class);
        return toAjax(assetProcessCountingTaskService.insertAssetCountingTask(dto));
    }

    /**
     * 新增盘点资产记录
     */
    @PostMapping("/countingAssets")
    public AjaxResult countingAssets(@RequestBody JSONObject params) {
        System.out.println("params: "+params);
        AssetProcessCounting vo = params.getObject("data", AssetProcessCounting.class);
        return toAjax(assetProcessCountingService.insertAssetProcessCounting(vo));
    }

}
