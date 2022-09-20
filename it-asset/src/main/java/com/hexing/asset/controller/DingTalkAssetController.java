package com.hexing.asset.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.service.*;
import com.hexing.asset.service.impl.AssetServiceImpl;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDictDataService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private IAssetProcessBackService assetProcessBackService;
    @Autowired
    private IAssetProcessReceiveService assetProcessReceiveService;


    /**
     * 根据资产编号查询资产信息
     */
    @PostMapping(value = "/queryAssetCard")
    public JSONObject queryAssetCard(@RequestBody Asset asset) {
        Result result = assetService.queryAssetCard(asset);
        JSONObject R = new JSONObject();
        R.put("result", result);
        return R;
    }

    /**
     * 根据工号查询保管人信息及名下资产
     */
    @PostMapping(value = "/queryPersonInfoAndAssetsByUserCode")
    public JSONObject queryPersonInfoAndAssetsByUserCode(@RequestBody JSONObject params) {
        Result result = assetService.queryPersonInfoAndAssetsByUserCode(params);
        JSONObject R = new JSONObject();
        R.put("result", result);
        return R;
    }

    /**
     * 根据资产编号更新资产信息
     */
    @PostMapping(value = "/updateAssetCardByAssetCode")
    @Transactional
    public JSONObject updateAssetCardByAssetCode(@RequestBody JSONObject params) {
        params = params.getJSONObject("data");
        String processType = params.getString("processType"); /* 资产管理流程类型 */
        String userCode = params.getString("userCode");       /* 流程发起人工号 */
        AssetProcess assetProcess = new AssetProcess();
        assetProcess.setCreateTime(new Date());
        assetProcess.setUserCode(userCode);
        if (DingTalkAssetProcessType.PROCESS_RECEIVE.getCode().equals(processType)) { // 领用流程
            assetProcess.setProcessType(DingTalkAssetProcessType.PROCESS_RECEIVE.getCode());
        } else if (DingTalkAssetProcessType.PROCESS_BACK.getCode().equals(processType)) { // 归还流程
            assetProcess.setProcessType(DingTalkAssetProcessType.PROCESS_BACK.getCode());
        }
        assetProcessService.save(assetProcess);
        Asset asset = JSONObject.toJavaObject(params, Asset.class);
        boolean success = assetService.update(asset.setUpdateTime(new Date()), new LambdaUpdateWrapper<Asset>()
                .eq(Asset::getAssetCode, asset.getAssetCode()));
        if (success) {
            return JSONObject.parseObject(Result.success("更新成功").toString());
        } else {
            return JSONObject.parseObject(Result.success("更新失败").toString());
        }
    }

    /**
     * 资产变更
     */
    @PostMapping(value = "/updateAssetExchange")
    public JSONObject updateAssetExchange(@RequestBody JSONObject params) {
        Result result = assetService.updateAssetExchange(params);
        return JSONObject.parseObject(result.toString());
    }

    /**
     * 资产转移
     */
    @PostMapping(value = "/updateAssetTransfer")
    public JSONObject updateAssetTransfer(@RequestBody JSONObject params) {
        Result result = assetService.updateAssetTransfer(params);
        return JSONObject.parseObject(result.toString());
    }

    /**
     * 查询所有存放地点
     */
    @PostMapping(value = "/queryAllAssetAddresses")
    public JSONObject queryAllAssetAddresses() {
        String dictType = "asset_location";
        List<SysDictData> locationDictDataList = sysDictDataService.selectDictDataByType(dictType);
        List<String> locationList = locationDictDataList.stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
        JSONObject addressList = new JSONObject();
        addressList.put("addressList", locationList);
        JSONObject result = new JSONObject();
        result.put("result", addressList);
        return result;
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
    public String selectCountingTaskCode(@RequestBody JSONObject params) {
        JSONObject result = new JSONObject();
        String userCode = params.getString("userCode");
        List<AssetInventoryTask> taskList = assetInventoryTaskService
                .selectAssetCountingTaskList(new AssetInventoryTask());
        List<String> list = new ArrayList<>();
        for (AssetInventoryTask a : taskList) {
            JSONObject countResult = assetProcessCountingService.countingStatusCount(a.getTaskCode());
            int notCounted = countResult.getIntValue("notCounted");
            if (a.getInventoryUsers().contains(userCode) && (a.getEndDate().getTime() >= new Date().getTime()) && notCounted != 0) {
                list.add(a.getTaskCode());
            }
        }
        result.put("result", list.toString());
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

        LambdaQueryWrapper<AssetProcessCounting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetProcessCounting::getTaskCode, taskCode)
                .eq(AssetProcessCounting::getAssetCode, assetCode);
        AssetProcessCounting entity = assetProcessCountingService.getOne(wrapper);
        if (entity == null) {
            return AjaxResult.error("所盘点资产不在当前任务盘点范围内");
        }
        if (!AssetCountingStatus.NOT_COUNTED.getStatus().equals(entity.getCountingStatus())) {
            return AjaxResult.error("该资产在当前任务中已被盘点过");
        }
        Asset asset = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetCode));
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
