package com.hexing.assetnew.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hexing.asset.domain.*;
import com.hexing.asset.domain.dto.ExchangeProcessDTO;
import com.hexing.asset.domain.dto.ProcessCommonDTO;
import com.hexing.asset.domain.dto.ReceiveProcessDTO;
import com.hexing.asset.domain.dto.UserAssetInfoDTO;
import com.hexing.asset.enums.*;
import com.hexing.asset.service.*;
import com.hexing.assetnew.domain.Asset;
import com.hexing.assetnew.domain.AssetInventoryTask;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.assetnew.enums.AssetProcessType;
import com.hexing.assetnew.service.IAssetInventoryTaskService;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.assetnew.service.IAssetsProcessService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.CommonUtils;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysDictDataService;
import com.hexing.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 开放给钉钉端的rest接口
 */
@Api(tags="钉钉接口")
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
    @Autowired
    private IAssetsProcessService processService;

    /**
     * 根据资产编号查询资产信息
     */
    @ApiOperation("根据资产编号查询资产信息")
    @PostMapping(value = "/queryAssetCard")
    public JSONObject queryAssetCard(@RequestBody Asset asset) {
        Result result = assetService.queryAssetCard(asset);
        JSONObject r = new JSONObject();
        r.put("result", result);
        return r;
    }

    /**
     * 根据工号,管理部门查询保管人信息及名下资产
     */
    @ApiOperation("根据工号,管理部门查询保管人信息及名下资产")
    @PostMapping(value = "/queryPersonInfoAndAssetsByUserCode")
    public JSONObject queryPersonInfoAndAssetsByUserCode(@RequestBody UserAssetInfoDTO params) {
        Result result = assetService.queryPersonInfoAndAssetsByUserCode(params);
        JSONObject r = new JSONObject();
        r.put("result", result);
        return r;
    }

    /**
     * 资产领用
     */
    @ApiOperation("资产领用流程")
    @PostMapping(value = "/assetReceive")
    @Transactional
    public Result assetReceive(@RequestBody ProcessCommonDTO<ReceiveProcessDTO> params) {

        String processType = params.getData().getProcessType();
        String userCode = params.getData().getUserCode();
        String instanceId = params.getData().getInstanceId();
        String assetCode = params.getData().getAssetCode();

        int processId = assetProcessReceiveService
                .saveProcess(instanceId, userCode, assetCode, processType);

        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getAssetCode, assetCode));


        entity.setAssetStatus(AssetStatus.USING.getStatus())
                .setUsageScenario(params.getData().getUsageScenario())
                .setLocation(params.getData().getLocation());

        if (DingTalkAssetProcessType.PROCESS_RECEIVE.getCode().equals(processType)) {       /* 领用流程 */
            entity.setResponsiblePersonCode(userCode);
            SysUser user = sysUserService.getUserByUserName(userCode);
            entity.setResponsiblePersonName(user.getNickName());
        }
        if (DingTalkAssetProcessType.PROCESS_RECEIVE_BY_ADMIN.getCode().equals(processType)) { /* 代领用流程 */
            SysUser user = sysUserService.getUserByUserName(entity.getResponsiblePersonCode()); /* 代领用流程发起人工号 */
            entity.setResponsiblePersonName(user.getNickName());
        }

        int update = assetService.updateAsset(entity, Integer.toString(processId));

        if (update > 0) {
            return Result.success("更新成功");
        } else {
            throw new ServiceException("更新失败");
        }

    }

    /**
     * 资产归还
     */
    @ApiOperation("资产归还流程")
    @PostMapping(value = "/assetBack")
    @Transactional
    public Result assetBack(@RequestBody JSONObject params) {
        params = params.getJSONObject("data");

        String processType = params.getString("processType");                           /* 资产管理流程类型 */
        String instanceId = params.getString("instanceId");
        String userCode = params.getString("userCode");                                 /* 流程发起人工号 */
        String responsiblePersonCode = params.getString("responsiblePersonCode");       /* 保管人工号（资产管理员工号） */
        JSONArray assetList = params.getJSONArray("assets");

        SysUser responsiblePerson = sysUserService.getUserByUserName(responsiblePersonCode);

        for (Object o : assetList) {
            String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
            String location = JSONUtil.parseObj(o).getStr("location");

            int processId = assetProcessBackService.saveProcess(instanceId, userCode, assetCode, processType);

            Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetCode));
            entity.setResponsiblePersonCode(responsiblePersonCode)
                    .setResponsiblePersonName(responsiblePerson.getNickName())
                    .setLocation(location);
            int update = assetService.updateAsset(entity, Integer.toString(processId));

            if (!(update > 0)) {
                throw new ServiceException("归还失败");
            }
        }

        return Result.success("归还成功");
    }

    /**
     * 资产更换
     */
    @ApiOperation("资产更换流程")
    @PostMapping(value = "/updateAssetExchange")
    public Result updateAssetExchange(@RequestBody ProcessCommonDTO<ExchangeProcessDTO> params) {
        return assetService.updateAssetExchange(params);
    }

    /**
     * 资产转移
     */
    @ApiOperation("资产转移流程")
    @PostMapping(value = "/updateAssetTransfer")
    public Result updateAssetTransfer(@RequestBody JSONObject params) {
        return assetService.updateAssetTransfer(params);
    }

    /**
     * 查询所有存放地点
     */
    @ApiOperation("查询所有存放地点")
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
    @ApiOperation("查询盘点任务编码")
    @PostMapping("/selectCountingTaskCode")
    public JSONObject selectCountingTaskCode(@RequestBody JSONObject params) {
        JSONObject result = new JSONObject();
        String userCode = params.getString("userCode");
        if (userCode.startsWith("S")){
            userCode = userCode.substring(1);
        }
        List<AssetInventoryTask> taskList = assetInventoryTaskService.list();
        List<String> list = new ArrayList<>();
        for (AssetInventoryTask task : taskList) {
            if (task.getInventoryUsers().contains(userCode)
                    && CountingTaskStatus.COUNTING.getStatus().equals(task.getStatus())) {
                list.add(task.getTaskName());
            }
        }
        JSONObject addressList = new JSONObject();
        addressList.put("data", list);
        result.put("result", addressList);;
        return result;
    }

    /**
     * 扫码盘点时，查询资产信息
     */
    @ApiOperation("扫码查询资产信息")
    @PostMapping("/counting/getAssetInfo")
    public AjaxResult getAssetInfo(@RequestBody JSONObject params) {
        String assetCode = params.getString("assetCode");
        String taskName = params.getString("taskName");
        if (StringUtils.isEmpty(taskName)) {
            Asset asset = CommonUtils.toNullStr(new Asset());
            return AjaxResult.error("盘点任务名称未选择",asset);
        }
        AssetInventoryTask task = assetInventoryTaskService
                .getOne(new LambdaQueryWrapper<AssetInventoryTask>().eq(AssetInventoryTask::getTaskName, taskName));
        if (ObjectUtil.isNull(task)) {
            Asset asset = CommonUtils.toNullStr(new Asset());
            return AjaxResult.error( "该盘点任务名称对应的盘点任务不存在",asset);
        }

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("taskCode", task.getTaskCode());
        paramMap.put("assetCode", assetCode);
        AssetProcessCounting entity = (AssetProcessCounting) processService.getOne(AssetProcessType.COUNTING_PROCESS.getCode(), paramMap);

        if (ObjectUtil.isNull(entity)) {
            Asset asset = CommonUtils.toNullStr(new Asset());
            return AjaxResult.error( "所盘点资产不在当前任务盘点范围内",asset);
        }
        if (!AssetCountingStatus.NOT_COUNTED.getStatus().equals(entity.getCountingStatus())) {
            Asset asset = CommonUtils.toNullStr(new Asset());
            return AjaxResult.error( "该资产在当前任务中已被盘点过",asset);
        }
        Asset asset = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetCode));
        if (ObjectUtil.isNull(asset)) {
            Asset asset1 = CommonUtils.toNullStr(new Asset());
            return AjaxResult.error( "该资产不存在",asset1);
        }
        if (StringUtils.isNotEmpty(asset.getResponsiblePersonCode())){
            SysUser user = sysUserService.getUserByUserName(asset.getResponsiblePersonCode());
            if (ObjectUtil.isNotNull(user)&&StringUtils.isNotEmpty(user.getDeptId().toString())){
                SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
                if (ObjectUtil.isNotNull(dept)){
                    asset.setResponsiblePersonDept(dept.getDeptName());
                }
            }
        }

        return AjaxResult.success("", asset);
    }

    /**
     * 钉钉提交盘点流程
     */
    @ApiOperation("资产盘点流程")
    @PostMapping("/counting/countAsset")
    @Transactional
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

        // 盘点任务是否结束
        if (CountingTaskStatus.FINISHED.getStatus().equals(task.getStatus())) {
            return AjaxResult.error(500, "盘点任务已结束");
        }

        String userCode = data.getString("userCode");
        String instanceId = data.getString("instanceId");
        JSONArray assetList = data.getJSONArray("assets");

        for (Object o : assetList) {
            String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
            // 若备注不为空，则为盘点异常
            String comment = JSONUtil.parseObj(o).getStr("comment");

            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("taskCode", task.getTaskCode());
            paramMap.put("assetCode", assetCode);
            AssetProcessCounting entity = (AssetProcessCounting) processService.getOne(AssetProcessType.COUNTING_PROCESS.getCode(), paramMap);

            // 若为异常数据，则跳过
            if (ObjectUtil.isNull(entity)) {
                continue;
            }
            String status = entity.getCountingStatus();
            if (AssetCountingStatus.NOT_COUNTED.getStatus().equals(status)) {
                if (StringUtils.isNotEmpty(comment)) {
                    entity.setCountingStatus(AssetCountingStatus.ABNORMAL.getStatus());
                } else {
                    entity.setCountingStatus(AssetCountingStatus.COUNTED.getStatus());
                }
                entity.setUserCode(userCode)
                        .setInstanceId(instanceId)
                        .setCountingTime(DateUtils.getNowDate())
                        .setComment(comment);
                processService.updateByProcessId(entity);
            }
        }

        // 盘点任务状态更新
        // 若指定盘点任务下待盘记录数为0
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("taskCode", task.getTaskCode());
        paramMap.put("countingStatus", AssetCountingStatus.NOT_COUNTED.getStatus());
        List<AssetsProcess> notCounted = processService.selectProcessWithCondition(AssetProcessType.COUNTING_PROCESS.getCode(), paramMap);
        if (notCounted.size() == 0) {
            // 更新盘点任务状态为已完成
            assetInventoryTaskService.update(new LambdaUpdateWrapper<AssetInventoryTask>()
                    .eq(AssetInventoryTask::getTaskCode, task.getTaskCode())
                    .set(AssetInventoryTask::getStatus, CountingTaskStatus.FINISHED.getStatus()));
        }
        return AjaxResult.success("盘点成功");
    }

    /**
     * 员工处置资产流程
     */
    @ApiOperation("资产处置流程")
    @Transactional
    @PostMapping("/disposalAssets")
    public AjaxResult disposalAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        disposalService.disposalAssets(params);
        return AjaxResult.success("处置成功");
    }

    /**
     * 员工改造资产流程
     */
    @ApiOperation("资产改造流程")
    @Transactional
    @PostMapping("/transformAssets")
    public AjaxResult transformAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        transformService.transformAssets(params);
        return AjaxResult.success("改造成功");
    }

    /**
     * 员工维修资产流程
     */
    @ApiOperation("资产维修流程")
    @Transactional
    @PostMapping("/maintainAssets")
    public AjaxResult maintainAssets(@RequestBody JSONObject params) {
        System.out.println("params: " + params);
        maintainService.maintainAssets(params);
        return AjaxResult.success("维修成功");
    }

}
