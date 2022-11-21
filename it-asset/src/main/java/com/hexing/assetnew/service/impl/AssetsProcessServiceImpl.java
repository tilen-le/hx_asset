package com.hexing.assetnew.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.assetnew.domain.AssetProcessCountingDomain;
import com.hexing.assetnew.domain.AssetProcessField;
import com.hexing.assetnew.domain.AssetProcessVariable;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.assetnew.domain.dto.CountingStatusNumDTO;
import com.hexing.assetnew.enums.AssetProcessType;
import com.hexing.assetnew.mapper.AssetProcessFieldMapper;
import com.hexing.assetnew.mapper.AssetsProcessMapper;
import com.hexing.assetnew.service.IAssetProcessFieldService;
import com.hexing.assetnew.service.IAssetProcessVariableService;
import com.hexing.assetnew.service.IAssetsProcessService;
import com.hexing.assetnew.service.ICommonService;
import com.hexing.common.utils.PageUtil;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 资产流程Service业务层处理
 *
 * @author zxy
 * @date 2022-11-04
 */
@Service
public class AssetsProcessServiceImpl extends ServiceImpl<AssetsProcessMapper, AssetsProcess> implements IAssetsProcessService {

    @Autowired
    private IAssetProcessVariableService variableService;
    @Autowired
    private IAssetProcessFieldService fieldService;
    @Autowired
    private ICommonService commonService;

    /**
     * 查询资产流程
     */
    @Override
    public AssetsProcess getOne(AssetsProcess process) {
        List<AssetsProcess> processList = this.list(process);
        if (CollectionUtil.isNotEmpty(processList)){
            return processList.get(0);
        }
        return new AssetsProcess();
    }

    /**
     * 根据流程id更新值表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByProcessId(AssetsProcess process) {

        List<AssetProcessVariable> varList = variableService.selectVariableListByProcessId(process.getId());

        for (AssetProcessVariable var : varList) {
            Object fieldValue = BeanTool.getFieldValue(process, var.getFieldKey());
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                var.setFieldValue(String.valueOf(fieldValue));
            } else {
                var.setFieldValue(null);
            }
        }
        variableService.updateBatchById(varList);

        // 对于新添加的流程字段
        Field[] fields = process.getClass().getDeclaredFields();
        List<Field> newFieldList = Arrays.stream(fields).filter(new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                return varList.stream().noneMatch(x -> x.getFieldKey().equals(field.getName()));
            }
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(newFieldList)) {
            List<AssetProcessField> processFieldList = fieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                    .eq(AssetProcessField::getProcessType, process.getProcessType())
                    .in(AssetProcessField::getFieldKey, newFieldList.stream().map(Field::getName)));

            Map<String, Long> fieldKeyIdMap = processFieldList
                    .stream().collect(Collectors.toMap(AssetProcessField::getFieldKey, AssetProcessField::getId));

            List<AssetProcessVariable> newVarList = new ArrayList<>();
            for (Field field : newFieldList) {
                AssetProcessVariable newVar = new AssetProcessVariable();
                newVar.setProcessId(process.getId());
                newVar.setFieldId(fieldKeyIdMap.get(field.getName()));
                Object fieldValue = BeanTool.getFieldValue(process, field.getName());
                if (ObjectUtil.isNotEmpty(fieldValue)) {
                    newVar.setFieldValue(String.valueOf(fieldValue));
                } else {
                    newVar.setFieldValue(null);
                }
            }
            variableService.saveBatch(newVarList);
        }

    }

    /**
     * 查询资产流程列表
     */
    @Override
    public List<AssetsProcess> listByPage(AssetsProcess process) {
        List<AssetProcessField> searchDomain = commonService.getSearchDomain(process);
        PageUtil.startPage();
        return commonService.searchAssetProcess(searchDomain, process);
    }

    @Override
    public List<AssetsProcess> list(AssetsProcess process) {
        List<AssetProcessField> searchDomain = commonService.getSearchDomain(process);
        return commonService.searchAssetProcess(searchDomain, process);
    }



    /**
     * process 转为 domain
     * @param process
     * @param domain
     */
    @Override
    public <T> T convertProcess(AssetsProcess process, T domain) {
        List<AssetProcessVariable> variableList = process.getVariableList();
        if (CollectionUtil.isNotEmpty(variableList)){
            for (AssetProcessVariable variable : variableList) {
                BeanTool.setFieldValueThrowEx(domain, variable.getFieldKey(), variable.getFieldValue());
            }
        }
        process.setVariableList(null);
        BeanUtil.copyProperties(process, domain);
        return domain;
    }

    @Override
    public void saveBatchProcess(List<? extends AssetsProcess> processList) {
        List<AssetProcessField> processFields = commonService.getProcessFields();
        List<AssetProcessField> fieldList = processFields.stream().filter(assetProcessField ->
                assetProcessField.getProcessType().equals(AssetProcessType.COUNTING_PROCESS.getCode()))
                .collect(Collectors.toList());

        List<AssetProcessVariable> varList = new ArrayList<>();
        for (AssetsProcess process : processList) {
            // 字段值存入流程值表
            for (AssetProcessField field : fieldList) {
                AssetProcessVariable var = new AssetProcessVariable();
                var.setProcessId(process.getId())
                        .setFieldId(field.getId());
                Object fieldValue = BeanTool.getFieldValue(process, field.getFieldKey());
                if (ObjectUtil.isNotEmpty(fieldValue)) {
                    var.setFieldValue(String.valueOf(fieldValue));
                } else {
                    var.setFieldValue(null);
                }
                varList.add(var);
            }
        }
        variableService.saveBatch(varList);
    }

}
