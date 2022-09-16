package com.hexing.asset.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.asset.service.IAssetInventoryTaskService;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.service.impl.AssetServiceImpl;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开放给钉钉端的rest接口
 */
@RestController
@RequestMapping("/api/dingtalk/asset")
public class DingTalkAssetController extends BaseController {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetInventoryTaskService assetInventoryTaskService;
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
        AssetInventoryTask task = params.getObject("data", AssetInventoryTask.class);
        return toAjax(assetInventoryTaskService.insertAssetCountingTask(task));
    }

    /**
     * 查询盘点任务编码
     */
    @PostMapping("/selectCountingTaskCode")
    public String  selectCountingTaskCode(@RequestBody JSONObject params) {
        logger.info("--------调用selectCountingTaskCode接口");
        AssetInventoryTask task =new AssetInventoryTask();
        JSONObject result =new JSONObject();
        String userCode =params.getString("userCode");
        List<AssetInventoryTask> taskList = assetInventoryTaskService.selectAssetCountingTaskList(task);
        List list=new ArrayList();
        Date date=new Date();

        for (AssetInventoryTask a:taskList){
            if (a.getInventoryUsers().contains(userCode)&&(a.getEndDate().getTime()>=date.getTime())){
                list.add(a.getTaskCode());
            }
        }
        result.put("result",list.toString());
        return result.toString();
    }

    /**
     * 新增盘点资产记录
     */
    @PostMapping("/countingAssets")
    public AjaxResult countingAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        AssetProcessCounting vo = params.getObject("data", AssetProcessCounting.class);
        return toAjax(assetProcessCountingService.insertAssetProcessCounting(vo));
    }

    /**
     * 扫码盘点时，查询资产信息
     */
    @PostMapping("/counting/getAssetInfo")
    public AjaxResult getAssetInfo(@RequestBody JSONObject params) {
        String assetCode = params.getString("assetCode");
        String taskCode = params.getString("taskCode");

        QueryWrapper<AssetProcessCounting> wrapper = new QueryWrapper<>();
        wrapper.eq("task_code", taskCode)
                .eq("asset_code", assetCode);
        AssetProcessCounting entity = assetProcessCountingService.getOne(wrapper);
        if (entity == null) {
            return AjaxResult.error("所盘点资产不在当前任务盘点范围内");
        }
        if (!AssetCountingStatus.NOT_COUNTED.getStatus().equals(entity.getCountingStatus())) {
            return AjaxResult.error("该资产在当前任务中已被盘点过");
        }
        Asset asset = assetService.getOne(new QueryWrapper<Asset>().eq("asset_code", assetCode));
        JSONObject jsonObject = AssetServiceImpl.setNewAsset(asset);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 钉钉提交盘点流程
     */
    @PostMapping("/counting/countAsset")
    public AjaxResult countAsset(@RequestBody JSONObject params) {
        JSONObject data = params.getObject("data", JSONObject.class);
        String taskCode = data.getString("taskCode");
        // 判断盘点任务编码是否为空
        if (StringUtils.isEmpty(taskCode)) {
            return AjaxResult.error("盘点任务编码为空");
        }
        // 判断盘点任务编码是否存在
        AssetInventoryTask task = assetInventoryTaskService
                .getOne(new QueryWrapper<AssetInventoryTask>().eq("task_code", taskCode));
        if (task == null) {
            return AjaxResult.error("盘点任务编码不存在");
        }
        // 若超出盘点任务截止日期
        if (CountingTaskStatus.FINISHED.getStatus().equals(task.getStatus()) || new Date().compareTo(task.getEndDate()) > 0) {
            return AjaxResult.error("盘点任务已结束");
        }

        String userCode = data.getString("userCode");
        String instanceId = data.getString("instanceId");
        JSONArray assetList = data.getJSONArray("assets");

        for (Object o : assetList) {
            String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
            // 若备注不为空，则为盘点异常
            String comment = JSONUtil.parseObj(o).getStr("comment");
            QueryWrapper<AssetProcessCounting> w = new QueryWrapper();
            w.eq("asset_code", assetCode);
            w.eq("task_code", taskCode);
            AssetProcessCounting entity = assetProcessCountingService.getOne(w);
            String status = entity.getCountingStatus();
            if (AssetCountingStatus.NOT_COUNTED.getStatus().equals(status)) {
                if (StringUtils.isNotEmpty(comment)) {
                    entity.setCountingStatus(AssetCountingStatus.ABNORMAL.getStatus());
                } else {
                    entity.setCountingStatus(AssetCountingStatus.COUNTED.getStatus());
                }
                entity.setUserCode(userCode);
                entity.setInstanceId(instanceId);
                entity.setCountingTime(new Date());
                entity.setComment(comment);
                assetProcessCountingService.updateById(entity);
            }
        }

        return AjaxResult.success("盘点成功");
    }

}
