package com.hexing.asset.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hexing.asset.constant.AssetConstants;
import com.hexing.asset.domain.dto.AssetInventoryTaskDTO;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetInventoryTaskService;
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
    private IAssetInventoryTaskService assetProcessCountingTaskService;
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
     * 获取人员资产信息
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
        AssetInventoryTaskDTO dto = params.getObject("data", AssetInventoryTaskDTO.class);
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

    /**
     * 盘点资产
     */
    @PostMapping("/countAsset")
    public AjaxResult countAsset(@RequestBody JSONObject params) {
        JSONObject data = params.getObject("data", JSONObject.class);
        String assetCode = data.getString("assetCode");
        AssetProcessCounting entity = assetProcessCountingService
                .getOne(new QueryWrapper<AssetProcessCounting>().eq("asset_code", assetCode));
        String status = entity.getCountingStatus();
        if (AssetConstants.COUNTING_STATUS_NOT_COUNTED.equals(status)) {
            entity.setCountingStatus(AssetConstants.COUNTING_STATUS_COUNTED);
            assetProcessCountingService.updateById(entity);
            return AjaxResult.success("盘点成功");
        } else {
            return AjaxResult.error("盘点失败，该资产已盘点过");
        }
    }

}
