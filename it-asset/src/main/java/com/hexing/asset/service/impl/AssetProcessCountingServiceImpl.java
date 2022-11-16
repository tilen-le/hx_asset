package com.hexing.asset.service.impl;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.*;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.assetnew.enums.AssetProcessType;
import com.hexing.assetnew.mapper.AssetProcessVariableMapper;
import com.hexing.assetnew.mapper.AssetsProcessMapper;
import com.hexing.asset.service.*;
import com.hexing.assetnew.domain.AssetProcessField;
import com.hexing.assetnew.domain.AssetProcessVariable;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.assetnew.service.IAssetProcessFieldService;
import com.hexing.assetnew.service.IAssetProcessVariableService;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessCountingMapper;

/**
 * 资产盘点流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessCountingServiceImpl extends ServiceImpl<AssetProcessCountingMapper, AssetProcessCounting> implements IAssetProcessCountingService {
    @Autowired
    private AssetProcessCountingMapper assetProcessCountingMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IAssetProcessVariableService processVariableService;
    @Autowired
    private IAssetProcessFieldService processFieldService;
    @Autowired
    private AssetsProcessMapper assetsProcessMapper;
    @Autowired
    private AssetProcessVariableMapper processVariableMapper;

    /**
     * 查询资产盘点流程列表
     *
     * @param params 资产盘点流程
     * @return 资产盘点流程
     */
//    @Override
//    public List<Map<String, Object>> selectAssetProcessCountingList(AssetProcessCountingDTO assetProcessCountingDTO) {

//        String processType = AssetProcessType.COUNTING_PROCESS.getCode();
//        List<AssetsProcess> processList = assetsProcessMapper
//                .selectProcessWithCondition(processType, assetProcessCountingDTO);
//        List<AssetsProcess> processList = assetsProcessMapper
//                .selectProcessWithCondition(null);
//
//        List<AssetProcessVariable> varList = processVariableMapper.selectProcessVariableWithCondition(processList
//                        .stream().map(AssetsProcess::getId).collect(Collectors.toList()));
//
//        Map<Long, List<AssetProcessVariable>> varMap = varList
//                .stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (AssetsProcess process : processList) {
//            List<AssetProcessVariable> variableList = varMap.get(String.valueOf(process.getId()));
//            Map<String, Object> map = new HashMap<>();
//            for (AssetProcessVariable var : variableList) {
//                map.put(var.getFieldKey(), var.getFieldValue());
//            }
//            list.add(map);
//        }
//
//        return list;
//    }


//    @Override
//    public List<AssetProcessCountingVO> toAssetProcessCountingVOList(List<Map<String, Object>> list) {
//        List<AssetProcessCountingVO> voList = new ArrayList<>();
//
//        List<String> assetCodeList = list.stream().map(AssetProcessCounting::getAssetCode).collect(Collectors.toList());
//        Map<String, Asset> assetMap = new HashMap<>();
//        if (CollectionUtil.isNotEmpty(assetCodeList)) {
//            assetMap = assetService.list(new LambdaQueryWrapper<Asset>().in(Asset::getAssetCode, assetCodeList))
//                    .stream().collect(Collectors.toMap(Asset::getAssetCode, asset -> asset));
//        }
//        Map<String, SysUser> userMap = sysUserService
//                .getUserByUserNames(list.stream().map(AssetProcessCounting::getUserCode).collect(Collectors.toSet()));
//        Map<String, SysUser> responsiblePersonMap = sysUserService
//                .getUserByUserNames(assetMap.values().stream().map(Asset::getResponsiblePersonCode).collect(Collectors.toSet()));
//        Map<Long, SysDept> deptMap = deptService
//                .selectDeptByIds(responsiblePersonMap.values().stream().map(SysUser::getDeptId).collect(Collectors.toList()));
//
//        for (AssetProcessCounting obj : list) {
//            Asset asset = assetMap.get(obj.getAssetCode());
//            SysUser responsiblePerson = responsiblePersonMap.get(asset.getResponsiblePersonCode());
//            String inventoryPerson = "";
//            if (StringUtils.isNotBlank(obj.getUserCode())) {
//                SysUser userByUserName = userMap.get(obj.getUserCode());
//                if (Objects.nonNull(userByUserName)) {
//                    inventoryPerson = userByUserName.getNickName();
//                }
//            }
//            SysDept dept = deptMap.get(responsiblePerson.getDeptId());
//            AssetProcessCountingVO vo = new AssetProcessCountingVO()
//                    .setUserCode(obj.getUserCode())
//                    .setUserName(inventoryPerson)
//                    .setCompanyName(asset.getCompanyName())
//                    .setAssetCode(asset.getAssetCode())
//                    .setAssetName(asset.getAssetName())
//                    .setFactoryNo(asset.getFactoryNo())
//                    .setStandard(asset.getStandard())
//                    .setUsageScenario(asset.getUsageScenario())
//                    .setManageDept(asset.getManageDept())
//                    .setResponsiblePersonName(responsiblePerson.getNickName())
//                    .setResponsiblePersonCode(responsiblePerson.getUserName())
//                    .setResponsiblePersonDept(dept.getDeptName())
//                    .setLocation(asset.getLocation())
//                    .setCountingTime(obj.getCountingTime())
//                    .setCountingStatus(obj.getCountingStatus())
//                    .setComment(obj.getComment());
//            voList.add(vo);
//        }
//        return voList;
//    }

//    @Override
//    public JSONObject countingStatusCount(String taskCode) {
//        JSONObject result = new JSONObject();
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("taskCode", taskCode);
////        List<AssetProcessCounting> assetProcessCountingList = this.selectAssetProcessCountingList(params);
//        List<AssetProcessCounting> assetProcessCountingList = null;
//
//        int total = 0;
//        int notCounted = 0;
//        int counted = 0;
//        int abnormal = 0;
//        if (CollectionUtil.isNotEmpty(assetProcessCountingList)) {
//            total = assetProcessCountingList.size();
//            for (AssetProcessCounting obj : assetProcessCountingList) {
//                String status = obj.getCountingStatus();
//                if (AssetCountingStatus.NOT_COUNTED.getStatus().equals(status)) {
//                    notCounted++;
//                }
//                if (AssetCountingStatus.COUNTED.getStatus().equals(status)) {
//                    counted++;
//                }
//                if (AssetCountingStatus.ABNORMAL.getStatus().equals(status)) {
//                    abnormal++;
//                }
//            }
//        }
//        counted += abnormal;
//
//        result.put("total", total);
//        result.put("notCounted", notCounted);
//        result.put("counted", counted);
//        result.put("abnormal", abnormal);
//
//        return result;
//    }

    @Override
    public List<AssetProcessCounting> inventoryCountList(String startDate, String endDate) {
        LambdaQueryWrapper<AssetProcessCounting> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(AssetProcessCounting::getCreateTime, startDate);
        wrapper.le(AssetProcessCounting::getCreateTime, endDate);
        wrapper.orderByAsc(AssetProcessCounting::getCreateTime);
        List<AssetProcessCounting> list = assetProcessCountingMapper.selectList(wrapper);
        return list;
    }

    @Override
    public JSONObject inventoryCount(String type, String startDate, String endDate) {
        JSONObject jsonObject = new JSONObject();
        List x = new ArrayList();
        List isCount = new ArrayList();
        List isExcept = new ArrayList();
        LambdaQueryWrapper<AssetProcessCounting> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(AssetProcessCounting::getCreateTime, startDate);
        wrapper.le(AssetProcessCounting::getCreateTime, endDate);
        List<AssetProcessCounting> list = assetProcessCountingMapper.selectList(wrapper).stream()
                .filter(i -> ObjectUtil.isNotNull(i.getCountingTime()))
                .collect(Collectors.toList());
        if ("年".equals(type)) {
            Map<String, Long> isCountList = list.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCountingTime(), "yyyy"), Collectors.counting())
                    );
            Map<String, Long> isExceptList = list.stream()
                    .filter(o -> "2".equals(o.getCountingStatus()))
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCountingTime(), "yyyy"), Collectors.counting())
                    );
            List<String> yearBetween = null;
            try {
                yearBetween = DateUtils.getDatePeriodFromTwoTime(startDate, endDate, type);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (CollectionUtil.isNotEmpty(yearBetween)) {
                for (String year : yearBetween) {
                    x.add(year);
                    isCount.add(isCountList.getOrDefault(year, 0L));
                    isExcept.add(isExceptList.getOrDefault(year, 0L));
                }
            }
            jsonObject.put("x", x);
            jsonObject.put("isCount", isCount);
            jsonObject.put("isExcept", isExcept);
        }
        if ("月".equals(type)) {
            Map<String, Long> isCountList = list.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCountingTime(), "yyyy-MM"), Collectors.counting())
                    );
            Map<String, Long> isExceptList = list.stream()
                    .filter(o -> "2".equals(o.getCountingStatus()))
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCountingTime(), "yyyy-MM"), Collectors.counting())
                    );
            List<String> monthBetween = null;
            try {
                monthBetween = DateUtils.getDatePeriodFromTwoTime(startDate, endDate, type);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (CollectionUtil.isNotEmpty(monthBetween)) {
                for (String month : monthBetween) {
                    x.add(month);
                    isCount.add(isCountList.getOrDefault(month, 0L));
                    isExcept.add(isExceptList.getOrDefault(month, 0L));
                }
            }
            jsonObject.put("x", x);
            jsonObject.put("isCount", isCount);
            jsonObject.put("isExcept", isExcept);
        }
        return jsonObject;
    }

    /**
     * 新增资产盘点流程
     *
     * @return 结果
     */
    @Override
    public int insertAssetProcessCounting(AssetProcessCounting entity) {
        return assetProcessCountingMapper.insert(entity);
    }

    /**
     * 删除资产盘点流程信息
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingById(Long id) {
        return assetProcessCountingMapper.deleteAssetProcessCountingById(id);
    }

    @Override
    public void saveBatch(List<AssetProcessCounting> processCountingList) throws Exception {

        List<AssetProcessField> processFieldList = processFieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                .eq(AssetProcessField::getProcessType, AssetProcessType.COUNTING_PROCESS.getCode()));

        for (AssetProcessCounting processCounting : processCountingList) {
            // 字段值存入流程值表
            List<AssetProcessVariable> varList = new ArrayList<>();
            try {
                for (AssetProcessField field : processFieldList) {
                    AssetProcessVariable var = new AssetProcessVariable();
                    var.setProcessId(processCounting.getProcessId())
                            .setFieldId(field.getId());
                    if (ObjectUtil.isNotEmpty(BeanTool.getFieldValue(processCounting, field.getFieldKey()))) {
                        var.setFieldValue(String.valueOf(BeanTool.getFieldValue(processCounting, field.getFieldKey())));
                    } else {
                        var.setFieldValue(null);
                    }
                    varList.add(var);
                }
            } catch (Exception e) {
                throw e;
            }
            processVariableService.saveBatch(varList);
        }
    }

}
