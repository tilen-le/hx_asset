package com.hexing.assetnew.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.domain.dto.UserAssetInfoDTO;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.assetnew.domain.*;
import com.hexing.assetnew.enums.AssetProcessType;
import com.hexing.assetnew.service.*;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.CommonUtils;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.ISysDeptService;
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
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAssetsProcessService processService;
    @Autowired
    private IAssetProcessFieldService assetProcessFieldService;
    @Autowired
    private IAssetProcessVariableService processVariableService;

    //批量修改
    @PostMapping(value = "/testUpdate")
    public JSONObject testUpdate(@RequestBody List<ProcessParam> params) {
        List<AssetProcessVariable> varList = new ArrayList<>();
        List<AssetsProcess> processList = new ArrayList<>();
        for (ProcessParam datum : params) {
            if (StringUtils.isEmpty(datum.getProcessType())||StringUtils.isEmpty(datum.getAssetCode())){
                continue;
            }
            List<AssetProcessField> processFieldList = assetProcessFieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                    .eq(AssetProcessField::getProcessType, datum.getProcessType()));
            AssetsProcess process = processService.getOne(new LambdaQueryWrapper<AssetsProcess>().eq(AssetsProcess::getProcessType, datum.getProcessType())
                    .eq(AssetsProcess::getAssetCode, datum.getAssetCode()));
            processList.add(process.setStatus("1"));
            processList.add(process.setUpdateTime(new Date()));
            for (AssetProcessField field : processFieldList) {
                AssetProcessVariable variable = processVariableService.getOne(new LambdaQueryWrapper<AssetProcessVariable>().eq(AssetProcessVariable::getProcessId, process.getId())
                        .eq(AssetProcessVariable::getFieldId, field.getId()));
                if (ObjectUtil.isNotEmpty(BeanTool.getFieldValue(datum, field.getFieldKey()))) {
                    variable.setFieldValue(String.valueOf(BeanTool.getFieldValue(datum, field.getFieldKey())));
                }
                varList.add(variable);
            }
        }
        processService.updateBatchById(processList);
        processVariableService.updateBatchById(varList);
        return null;
    }

    //单个修改
    @PostMapping(value = "/testUpdateOne")
    public JSONObject testUpdateOne(@RequestBody ProcessParam datum) {
        if (StringUtils.isEmpty(datum.getProcessType())||StringUtils.isEmpty(datum.getAssetCode())){
            return null;
        }
        List<AssetProcessVariable> varList = new ArrayList<>();
        List<AssetProcessField> processFieldList = assetProcessFieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                .eq(AssetProcessField::getProcessType, datum.getProcessType()));
        AssetsProcess process = processService.getOne(new LambdaQueryWrapper<AssetsProcess>().eq(AssetsProcess::getProcessType, datum.getProcessType())
                .eq(AssetsProcess::getAssetCode, datum.getAssetCode()));
        for (AssetProcessField field : processFieldList) {
            AssetProcessVariable variable = processVariableService.getOne(new LambdaQueryWrapper<AssetProcessVariable>().eq(AssetProcessVariable::getProcessId, process.getId())
                    .eq(AssetProcessVariable::getFieldId, field.getId()));
            if (ObjectUtil.isNotEmpty(BeanTool.getFieldValue(datum, field.getFieldKey()))) {
                variable.setFieldValue(String.valueOf(BeanTool.getFieldValue(datum, field.getFieldKey())));
            }
            varList.add(variable);
        }
        process.setStatus("1");
        process.setUpdateTime(new Date());
        processService.updateById(process);
        processVariableService.updateBatchById(varList);
        return null;
    }

    @PostMapping(value = "/insertAssetCountingTask")
    public JSONObject insertAssetCountingTask(@RequestBody AssetInventoryTask assetInventoryTask){

        assetInventoryTaskService.insertAssetCountingTask(assetInventoryTask);
        return null;
    }

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
            return AjaxResult.error(HttpStatus.ERROR, "该盘点任务名称对应的盘点任务不存在");
        }

        // 盘点任务是否结束
        if (CountingTaskStatus.FINISHED.getStatus().equals(task.getStatus())) {
            return AjaxResult.error(HttpStatus.ERROR, "盘点任务已结束");
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
//                processService.updateByProcessId(entity);
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

}
