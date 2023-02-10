package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.mapper.AssetProcessFieldMapper;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.asset.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产流程Service业务层处理
 *
 * @author zxy
 * @date 2022-11-04
 */
@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private AssetProcessMapper processMapper;
    @Autowired
    private AssetProcessFieldMapper assetProcessFieldMapper;
    @Autowired
    private IAssetProcessVariableService assetProcessVariableService;

    @Override
    public List<AssetProcessField> getProcessFields() {
        List<AssetProcessField> assetProcessFields = assetProcessFieldMapper.selectList(new LambdaQueryWrapper());
        return assetProcessFields;
    }

    @Override
    public List<AssetProcess> listProcessInfo(AssetProcess process, List<AssetProcessField> assetProcessFields) {
        JSONObject obj = JSONUtil.parseObj(process);
        Set<String> keySet = obj.keySet();
        List<AssetProcessField> list = new ArrayList<>();
        for (AssetProcessField assetProcessField : assetProcessFields) {
            if (assetProcessField.getProcessType().equals(process.getProcessType())) {
                if (keySet.contains(assetProcessField.getFieldKey())) {
                    list.add(assetProcessField);
                }
            }
        }
        assetProcessFields = list;
        Iterator<AssetProcessField> it = assetProcessFields.iterator();
        while (it.hasNext()) {
            AssetProcessField field = it.next();
            Object value = obj.get(field.getFieldKey());
            if (Objects.nonNull(value)) {
                field.setFieldValue(value);
            } else {
                it.remove();
            }
        }
        List<AssetProcess> assetProcesses = searchAssetProcess(assetProcessFields, process);
        return assetProcesses;
    }

    @Override
    public List<AssetProcess> searchAssetProcess(List<AssetProcessField> searchDomains, AssetProcess process) {
        List<AssetProcess> assetProcesses = processMapper.selectProcessWithDomain(process, searchDomains);
        if (CollectionUtil.isNotEmpty(assetProcesses)) {
            List<Long> processIds = assetProcesses.stream().map(AssetProcess::getId).collect(Collectors.toList());
            List<AssetProcessVariable> varList = assetProcessVariableService.selectVarWithProcessIds(processIds);
            Map<Long, List<AssetProcessVariable>> varMap = varList.stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
            assetProcesses.forEach(p -> p.setVariableList(varMap.getOrDefault(p.getId(), Collections.emptyList())));
        }
        return assetProcesses;
    }


    @Override
    public List<AssetProcessField> getSearchDomain(AssetProcess process) {
        JSONObject obj = JSONUtil.parseObj(process);
        Set<String> keySet = obj.keySet();
        LambdaQueryWrapper<AssetProcessField> fieldWrapper = new LambdaQueryWrapper<>();
        fieldWrapper.eq(AssetProcessField::getProcessType, process.getProcessType())
                .in(AssetProcessField::getFieldKey, keySet);
        List<AssetProcessField> assetProcessFields = assetProcessFieldMapper.selectList(fieldWrapper);

        Iterator<AssetProcessField> it = assetProcessFields.iterator();
        while (it.hasNext()) {
            AssetProcessField field = it.next();
            Object value = obj.get(field.getFieldKey());
            if (Objects.nonNull(value)) {
                field.setFieldValue(value);
            } else {
                it.remove();
            }
        }

        return assetProcessFields;
    }
}
