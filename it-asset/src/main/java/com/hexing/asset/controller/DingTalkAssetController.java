package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hexing.asset.domain.*;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.service.*;
import com.hexing.asset.service.impl.AssetServiceImpl;
import com.hexing.common.annotation.RepeatSubmit;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysDictDataService;
import com.hexing.system.service.ISysUserService;
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
    private IAssetProcessBackService assetProcessBackService;
    @Autowired
    private IAssetProcessReceiveService assetProcessReceiveService;
    @Autowired
    private IAssetProcessDisposalService disposalService;
    @Autowired
    private IAssetProcessMaintainService maintainService;
    @Autowired
    private IAssetProcessTransformService transformService;
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;

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
     * 根据工号,管理部门查询保管人信息及名下资产
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
    public Result updateAssetCardByAssetCode(@RequestBody JSONObject params) {
        params = params.getJSONObject("data");
        String processType = params.getString("processType");                           /* 资产管理流程类型 */
        String userCode = params.getString("userCode");                                 /* 流程发起人工号 */
        String instanceId = params.getString("instanceId");
        Asset asset = JSONObject.toJavaObject(params, Asset.class);
        if (DingTalkAssetProcessType.PROCESS_RECEIVE.getCode().equals(processType)) {       /* 领用流程 */
            assetProcessReceiveService.saveProcess(instanceId, userCode, asset.getAssetCode());
        } else if (DingTalkAssetProcessType.PROCESS_BACK.getCode().equals(processType)) {   /* 归还流程 */
            assetProcessBackService.saveProcess(instanceId, userCode, asset.getAssetCode());
        }
        Asset temp = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, asset.getAssetCode()));
        if (ObjectUtil.isNull(temp)) {
            return Result.error(500, "该平台资产编码对应资产不存在");
        }
        boolean success = assetService.update(new LambdaUpdateWrapper<Asset>()
                .eq(Asset::getAssetCode, asset.getAssetCode())
                .set(StringUtils.isNotEmpty(asset.getAssetStatus()), Asset::getAssetStatus, asset.getAssetStatus())
                .set(StringUtils.isNotEmpty(asset.getResponsiblePersonCode()), Asset::getResponsiblePersonCode, asset.getResponsiblePersonCode())
                .set(StringUtils.isNotEmpty(asset.getLocation()), Asset::getAssetStatus, asset.getAssetStatus()));
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.success("更新失败");
        }
    }

    /**
     * 资产变更
     */
    @PostMapping(value = "/updateAssetExchange")
    public Result updateAssetExchange(@RequestBody JSONObject params) {
        return assetService.updateAssetExchange(params);
    }

    /**
     * 资产转移
     */
    @PostMapping(value = "/updateAssetTransfer")
    public Result updateAssetTransfer(@RequestBody JSONObject params) {
        return assetService.updateAssetTransfer(params);
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
                list.add(a.getTaskName());
            }
        }
        result.put("result", list.toString());
        return result.toString();
    }

    /**
     * 扫码盘点时，查询资产信息
     */
    @PostMapping("/counting/getAssetInfo")
    public AjaxResult getAssetInfo(@RequestBody JSONObject params) {
        String assetCode = params.getString("assetCode");
        String taskName = params.getString("taskName");
        if (StringUtils.isEmpty(taskName)) {
            return AjaxResult.error(500, "盘点任务名称未选择");
        }
        AssetInventoryTask task = assetInventoryTaskService
                .getOne(new LambdaQueryWrapper<AssetInventoryTask>().eq(AssetInventoryTask::getTaskName, taskName));
        if (ObjectUtil.isNull(task)) {
            return AjaxResult.error(500, "该盘点任务名称对应的盘点任务不存在");
        }
        LambdaQueryWrapper<AssetProcessCounting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetProcessCounting::getTaskCode, task.getTaskCode())
                .eq(AssetProcessCounting::getAssetCode, assetCode);
        AssetProcessCounting entity = assetProcessCountingService.getOne(wrapper);
        if (entity == null) {
            return AjaxResult.error(500, "所盘点资产不在当前任务盘点范围内");
        }
        if (!AssetCountingStatus.NOT_COUNTED.getStatus().equals(entity.getCountingStatus())) {
            return AjaxResult.error(500, "该资产在当前任务中已被盘点过");
        }
        Asset asset = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetCode));
        SysUser user = sysUserService.getUserByUserName(asset.getResponsiblePersonCode());
        SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
        asset.setResponsiblePersonDept(dept.getDeptName());
        return AjaxResult.success("", asset);
    }

    /**
     * 钉钉提交盘点流程
     */
    @PostMapping("/counting/countAsset")
    public AjaxResult countAsset(@RequestBody JSONObject params) {
        JSONObject data = params.getObject("data", JSONObject.class);
        String taskName = data.getString("taskName");
        // 判断盘点任务名称是否为空
        if (StringUtils.isEmpty(taskName)) {
            return AjaxResult.error("盘点任务名称为空");
        }
        AssetInventoryTask task = assetInventoryTaskService
                .getOne(new LambdaQueryWrapper<AssetInventoryTask>().eq(AssetInventoryTask::getTaskName, taskName));
        if (ObjectUtil.isNull(task)) {
            return AjaxResult.error(500, "该盘点任务名称对应的盘点任务不存在");
        }
        String taskCode = task.getTaskCode();
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
            LambdaQueryWrapper<AssetProcessCounting> w = new LambdaQueryWrapper<>();
            w.eq(AssetProcessCounting::getAssetCode, assetCode)
                    .eq(AssetProcessCounting::getTaskCode, taskCode);
            AssetProcessCounting entity = assetProcessCountingService.getOne(w);
            String status = entity.getCountingStatus();
            if (AssetCountingStatus.NOT_COUNTED.getStatus().equals(status)) {
                if (StringUtils.isNotEmpty(comment)) {
                    entity.setCountingStatus(AssetCountingStatus.ABNORMAL.getStatus());
                } else {
                    entity.setCountingStatus(AssetCountingStatus.COUNTED.getStatus());
                }
                entity.setUserCode(userCode)
                        .setInstanceId(instanceId)
                        .setCountingTime(new Date())
                        .setComment(comment);
                assetProcessCountingService.updateById(entity);
            }
        }

        return AjaxResult.success("盘点成功");
    }

    /**
     * 员工处置资产流程
     */
    @Transactional
    @PostMapping("/disposalAssets")
    public AjaxResult disposalAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        JSONObject data = params.getObject("data", JSONObject.class);
        String userCode = data.getString("userCode");
        String processType = data.getString("processType");
        String instanceId = data.getString("instanceId");
        String fileInfo = data.getString("fileInfo");
        JSONArray assetList = data.getJSONArray("assets");
        for (Object o : assetList) {
            String  assetCode = o.toString();
            AssetProcess assetProcess = new AssetProcess();
            assetProcess.setAssetCode(assetCode);
            assetProcess.setUserCode(userCode);
            assetProcess.setProcessType(processType);
            assetProcess.setCreateTime(new Date());
            assetProcessService.insertAssetProcess(assetProcess);

            AssetProcessDisposal entity = new AssetProcessDisposal();
            entity.setAssetCode(assetCode);
            entity.setProcessId(assetProcess.getId());
            entity.setUserCode(userCode);
            entity.setFileInfo(fileInfo);
            entity.setInstanceId(instanceId);
            entity.setCreateTime(new Date());
            disposalService.insertAssetProcessDisposal(entity);
        }
        return AjaxResult.success("处置成功");
    }

    /**
     * 员工改造资产流程
     */
    @Transactional
    @PostMapping("/transformAssets")
    public AjaxResult transformAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        JSONObject data = params.getObject("data", JSONObject.class);
        String userCode = data.getString("userCode");
        String processType = data.getString("processType");
        String instanceId = data.getString("instanceId");
        String fileInfo = data.getString("fileInfo");
        JSONArray assetList = data.getJSONArray("assets");
        for (Object o : assetList) {
            String  assetCode = o.toString();
            AssetProcess assetProcess = new AssetProcess();
            assetProcess.setAssetCode(assetCode);
            assetProcess.setUserCode(userCode);
            assetProcess.setProcessType(processType);
            assetProcess.setCreateTime(new Date());
            assetProcessService.insertAssetProcess(assetProcess);

            AssetProcessTransform entity = new AssetProcessTransform();
            entity.setAssetCode(assetCode);
            entity.setProcessId(assetProcess.getId());
            entity.setUserCode(userCode);
            entity.setFileInfo(fileInfo);
            entity.setInstanceId(instanceId);
            entity.setCreateTime(new Date());
            transformService.insertAssetProcessTransform(entity);
        }

        return AjaxResult.success("改造成功");
    }

    /**
     * 员工维修资产流程
     */
    @Transactional
    @PostMapping("/maintainAssets")
    public AjaxResult maintainAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        JSONObject data = params.getObject("data", JSONObject.class);
        String userCode = data.getString("userCode");
        String processType = data.getString("processType");
        String instanceId = data.getString("instanceId");
        String fileInfo = data.getString("fileInfo");
        JSONArray assetList = data.getJSONArray("assets");
        for (Object o : assetList) {
            String  assetCode = o.toString();
            AssetProcess assetProcess = new AssetProcess();
            assetProcess.setAssetCode(assetCode);
            assetProcess.setUserCode(userCode);
            assetProcess.setProcessType(processType);
            assetProcess.setCreateTime(new Date());
            assetProcessService.insertAssetProcess(assetProcess);

            AssetProcessMaintain entity = new AssetProcessMaintain();
            entity.setAssetCode(assetCode);
            entity.setProcessId(assetProcess.getId());
            entity.setUserCode(userCode);
            entity.setInstanceId(instanceId);
            entity.setFileInfo(fileInfo);
            entity.setCreateTime(new Date());
            maintainService.insertAssetProcessMaintain(entity);
        }
        return AjaxResult.success("维修成功");
    }
    /**
     * 新增盘点任务
     */
    @PostMapping("/createCountingTask")
    @RepeatSubmit(interval = 10000, message = "请勿重复提交")
    public AjaxResult createCountingTask(@RequestBody JSONObject params) {
        AssetInventoryTask task = params.getObject("data", AssetInventoryTask.class);
        return toAjax(assetInventoryTaskService.insertAssetCountingTask(task));
    }
}
