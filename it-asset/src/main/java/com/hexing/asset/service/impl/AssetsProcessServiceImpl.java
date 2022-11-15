package com.hexing.asset.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Process;
import com.hexing.asset.domain.*;
import com.hexing.asset.domain.dto.MapperQueryParam;
import com.hexing.asset.enums.AssetProcessType;
import com.hexing.asset.mapper.AssetProcessVariableMapper;
import com.hexing.asset.mapper.AssetsProcessMapper;
import com.hexing.asset.service.IAssetProcessFieldService;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.service.IAssetsProcessService;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产流程Service业务层处理
 *
 * @author zxy
 * @date 2022-11-04
 */
@Service
public class AssetsProcessServiceImpl extends ServiceImpl<AssetsProcessMapper, AssetsProcess> implements IAssetsProcessService
{
    @Autowired
    private AssetsProcessMapper processMapper;
    @Autowired
    private IAssetProcessVariableService variableService;
    @Autowired
    private IAssetProcessFieldService fieldService;
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private AssetProcessVariableMapper processVariableMapper;
    @Autowired
    private IAssetService assetService;


    /**
     * 查询资产流程
     * @param processType 流程类型
     * @param params 查询参数
     * @return
     */
    @Override
    public Object getOne(String processType, Map<String, Object> params) {
        final String PROCESS_DICT_TYPE = "asset_process";
        List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType(PROCESS_DICT_TYPE);
        String processClassName = dictDataList
                .stream().filter(x->processType.equals(x.getDictValue())).map(SysDictData::getDictLabelEn).findFirst().orElse(null);
        String fullClassName = "com.hexing.asset.domain." + processClassName;

        Object obj = null;
        try {
            Class<?> clazz = Class.forName(fullClassName);
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<AssetsProcess> processList = this.selectProcessWithCondition(processType, params);
        AssetsProcess process = processList.get(0);
        for (AssetProcessVariable var : process.getVariableList()) {
            BeanTool.setFieldValue(obj, var.getFieldKey(), var.getFieldValue());
        }

        try {
            Field processIdField = obj.getClass().getDeclaredField("processId");
            processIdField.setAccessible(true);
            processIdField.set(obj, process.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public void updateByProcessId(Object obj) {

        final String FIELD_PROCESS_ID = "processId";

        Field[] fields = obj.getClass().getDeclaredFields();
        String processId = "";
        try {
            Field processIdField = obj.getClass().getDeclaredField(FIELD_PROCESS_ID);
            processIdField.setAccessible(true);
            processId = String.valueOf(processIdField.get(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<AssetProcessVariable> varList = this.selectVariableListByProcessId(processId);

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            for (AssetProcessVariable var : varList) {
                if (fieldName.equals(var.getFieldKey())) {
                    try {
                        if (ObjectUtil.isNotEmpty(field.get(obj))) {
                            var.setFieldValue(String.valueOf(field.get(obj)));
                        } else {
                            var.setFieldValue(null);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        variableService.updateBatchById(varList);
    }

    @Override
    public List<AssetProcessVariable> selectVariableListByProcessId(String processId) {
        return processMapper.selectVariableListByProcessId(processId);
    }

    /**
     * 查询资产流程列表
     */
    @Override
    public List<Map<String, Object>> list(Process assetProcess) {

        // 封装MapperQueryParam
        List<AssetProcessField> fieldList = fieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                .eq(AssetProcessField::getProcessType, AssetProcessType.getValue(assetProcess)));
        MapperQueryParam param = new MapperQueryParam(assetProcess, fieldList);
        List<AssetsProcess> processList = processMapper.selectProcessWithCondition(param);

        List<AssetProcessVariable> varList = processVariableMapper
                .selectProcessVariableWithCondition(processList.stream().map(AssetsProcess::getId).collect(Collectors.toList()));

        Map<Long, List<AssetProcessVariable>> varMap = varList
                .stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));

        List<Map<String, Object>> list = new ArrayList<>();
        for (AssetsProcess process : processList) {
            List<AssetProcessVariable> variableList = varMap.get(String.valueOf(process.getId()));
            Map<String, Object> map = new HashMap<>();
            for (AssetProcessVariable var : variableList) {
                map.put(var.getFieldKey(), var.getFieldValue());
            }
            list.add(map);
        }

        // 关联表拼接
        String relationTable = "";
        for (AssetProcessField field : fieldList) {
            if (StringUtils.isNotEmpty(field.getRelationTable())) {
                relationTable = field.getRelationTable();
                break;
            }
        }
        if (StringUtils.isNotEmpty(relationTable)) {
            if ("asset".equals(relationTable)) {
                List<String> assetCodeList = varList.stream().filter(x -> "assetCode".equals(x.getFieldKey()))
                        .map(AssetProcessVariable::getFieldValue).collect(Collectors.toList());
                List<Asset> assetList = assetService.list(new LambdaQueryWrapper<Asset>().in(Asset::getAssetCode, assetCodeList));
                Map<String, List<Asset>> assetMap = assetList.stream().collect(Collectors.groupingBy(Asset::getAssetCode));
                for (Map<String, Object> map : list) {
                    Asset asset = assetMap.get(map.get("assetCode")).get(0);
                    map.putAll(BeanTool.objectToMap(asset));
                }
            }
        }

        return list;
    }

    @Override
    public List<AssetsProcess> selectProcessWithCondition(String processType, Map<String, Object> params) {
        List<AssetsProcess> processList = processMapper.selectProcessWithCondition(null);
        List<AssetProcessVariable> varList = processVariableMapper.selectProcessVariableWithCondition(processList
                .stream().map(AssetsProcess::getId).collect(Collectors.toList()));
        Map<String, List<AssetProcessVariable>> varMap = varList
                .stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
        for (AssetsProcess process : processList) {
            process.setVariableList(varMap.get(String.valueOf(process.getId())));
        }
        return processList;
    }

}
