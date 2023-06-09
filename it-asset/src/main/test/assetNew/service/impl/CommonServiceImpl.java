package com.hexing.assetNew.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.assetNew.domain.AssetProcessField;
import com.hexing.assetNew.domain.AssetProcessVariable;
import com.hexing.assetNew.domain.AssetsProcess;
import com.hexing.assetNew.mapper.AssetProcessFieldMapper;
import com.hexing.assetNew.mapper.AssetsProcessMapper;
import com.hexing.assetNew.service.IAssetProcessVariableService;
import com.hexing.assetNew.service.ICommonService;
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
    private AssetsProcessMapper processMapper;
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
    public List<AssetsProcess> listProcessInfo(AssetsProcess process, List<AssetProcessField> assetProcessFields) {
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
        List<AssetsProcess> assetsProcesses = searchAssetProcess(assetProcessFields, process);
        return assetsProcesses;
    }

    @Override
    public List<AssetsProcess> searchAssetProcess(List<AssetProcessField> searchDomains, AssetsProcess process) {
        List<AssetsProcess> assetsProcesses = processMapper.selectProcessWithDomain(process, searchDomains);
        if (CollectionUtil.isNotEmpty(assetsProcesses)) {
            List<Long> processIds = assetsProcesses.stream().map(AssetsProcess::getId).collect(Collectors.toList());
            List<AssetProcessVariable> varList = assetProcessVariableService.selectVarWithProcessIds(processIds);
            Map<Long, List<AssetProcessVariable>> varMap = varList.stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
            assetsProcesses.forEach(p -> p.setVariableList(varMap.getOrDefault(p.getId(), Collections.emptyList())));
        }
        return assetsProcesses;
    }


    @Override
    public List<AssetProcessField> getSearchDomain(AssetsProcess process) {
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
