package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.dto.ExchangeProcessDTO;
import com.hexing.asset.domain.dto.OldProcessCommonDTO;
import com.hexing.asset.domain.dto.ReceiveProcessDTO;
import com.hexing.asset.domain.dto.UserAssetInfoDTO;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.service.*;
import com.hexing.assetnew.domain.*;
import com.hexing.assetnew.service.*;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysDictDataService;
import com.hexing.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 开放给钉钉端的rest接口
 */
//@Api(tags="钉钉接口")
//@RestController
//@RequestMapping("/old/api/dingtalk/asset")
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
//            processList.add(process.setStatus("1"));
//            processList.add(process.setUpdateTime(new Date()));
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
     * 资产领用
     */
    @ApiOperation("资产领用流程")
    @PostMapping(value = "/assetReceive")
    @Transactional
    public Result assetReceive(@RequestBody OldProcessCommonDTO<ReceiveProcessDTO> params) {

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
    public Result updateAssetExchange(@RequestBody OldProcessCommonDTO<ExchangeProcessDTO> params) {
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
