package com.hexing.assetnew.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.domain.Process;
import com.hexing.asset.domain.dto.MapperQueryParam;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.zxy.PdProcessDomain;
import com.hexing.assetnew.domain.*;
import com.hexing.assetnew.domain.dto.CountingStatusNumDTO;
import com.hexing.assetnew.enums.AssetProcessType;
import com.hexing.assetnew.mapper.AssetProcessFieldMapper;
import com.hexing.assetnew.mapper.AssetProcessVariableMapper;
import com.hexing.assetnew.mapper.AssetsProcessMapper;
import com.hexing.assetnew.service.IAssetProcessFieldService;
import com.hexing.assetnew.service.IAssetProcessVariableService;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.assetnew.service.IAssetsProcessService;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.utils.PageUtil;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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
    @Autowired
    private AssetProcessFieldMapper assetProcessFieldMapper;


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

    /**
     * 根据流程id更新值表
     */
    @Override
    @Transactional
    public void updateByProcessId(AssetsProcess process) {

        List<AssetProcessVariable> varList = this.selectVariableListByProcessId(process.getId());

        for (AssetProcessVariable var : varList) {
            String fieldValue = (String) BeanTool.getFieldValue(process, var.getFieldKey());
            if (StringUtils.isNotEmpty(fieldValue)) {
                var.setFieldValue(fieldValue);
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
                String fieldValue = (String) BeanTool.getFieldValue(process, field.getName());
                if (StringUtils.isNotEmpty(fieldValue)) {
                    newVar.setFieldValue(fieldValue);
                } else {
                    newVar.setFieldValue(null);
                }
            }
            variableService.saveBatch(newVarList);
        }

    }

    @Override
    public List<AssetProcessVariable> selectVariableListByProcessId(Long processId) {
        return processMapper.selectVariableListByProcessId(processId);
    }

    /**
     * 查询资产流程列表
     */
    @Override
    public List<AssetsProcess> listByPage(AssetsProcess process) {
        List<AssetProcessField> searchDomain = getSearchDomain(process);
        PageUtil.startPage();
        return searchAssetProcess(searchDomain, process);
    }

    @Override
    public List<AssetsProcess> list(AssetsProcess process) {
        List<AssetProcessField> searchDomain = getSearchDomain(process);
        return searchAssetProcess(searchDomain, process);
    }


    @Override
    public List<AssetsProcess> selectProcessWithCondition(String processType, Map<String, Object> params) {
        List<AssetsProcess> processList = processMapper.selectProcessWithCondition(null);
        List<AssetProcessVariable> varList = processVariableMapper.selectProcessVariableWithCondition(processList
                .stream().map(AssetsProcess::getId).collect(Collectors.toList()));
        Map<Long, List<AssetProcessVariable>> varMap = varList
                .stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
        for (AssetsProcess process : processList) {
            process.setVariableList(varMap.get(String.valueOf(process.getId())));
        }
        return processList;
    }

    /**
     * 获取字段配置并过滤
     * @param process
     * @return
     */
    private List<AssetProcessField> getSearchDomain(AssetsProcess process) {
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

    /**
     * 此方法只查数据库，不对结果进行二次封装
     * @param searchDomains 查询条件集合
     * @return
     */
    private List<AssetsProcess> searchAssetProcess(List<AssetProcessField> searchDomains, AssetsProcess process) {
        List<AssetsProcess> assetsProcesses = processMapper.selectProcessWithDomain(process, searchDomains);
        if (CollectionUtil.isNotEmpty(assetsProcesses)) {
            List<Long> processIds = assetsProcesses.stream().map(AssetsProcess::getId).collect(Collectors.toList());
            List<AssetProcessVariable> varList = processMapper.selectVarWithProcessIds(processIds);
            Map<Long, List<AssetProcessVariable>> varMap = varList.stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
            assetsProcesses.forEach(p -> p.setVariableList(varMap.getOrDefault(p.getId(), Collections.emptyList())));
        }
        return assetsProcesses;
    }

    /**
     * process 转为 domain
     * @param process
     * @param domain
     */
    public <T> T convertProcess(AssetsProcess process, T domain) {
        List<AssetProcessVariable> variableList = process.getVariableList();
        for (AssetProcessVariable variable : variableList) {
            BeanTool.setFieldValueThrowEx(domain, variable.getFieldKey(), variable.getFieldValue());
        }
        process.setVariableList(null);
        BeanUtil.copyProperties(process, domain);
        return domain;
    }

    /**
     * 盘点状态统计
     * @param taskCode 盘点任务编号
     * @return
     */
    @Override
    public CountingStatusNumDTO countingStatusCount(String taskCode) {
        final String FIELD_COUNTING_STATUS = "countingStatus";

        AssetProcessCountingDomain entity = new AssetProcessCountingDomain();
        entity.setTaskCode(taskCode);
        entity.setProcessType("100"); // TODO 暂时写死
        List<AssetsProcess> processList = this.list(entity);

        CountingStatusNumDTO numDTO = new CountingStatusNumDTO();

        if (CollectionUtil.isNotEmpty(processList)) {
            numDTO.setTotal(processList.size());

            List<String> countStatusList = processList.stream()
                    .map(process -> process.getVariableList().stream()
                            .filter(x -> FIELD_COUNTING_STATUS.equals(x.getFieldKey()))
                            .map(AssetProcessVariable::getFieldValue)
                            .findFirst()
                            .orElse(null))
                    .collect(Collectors.toList());

            for (String status : countStatusList) {
                if (AssetCountingStatus.NOT_COUNTED.getStatus().equals(status)) {
                    numDTO.setNotCounted(numDTO.getNotCounted() + 1);
                }
                if (AssetCountingStatus.COUNTED.getStatus().equals(status)) {
                    numDTO.setCounted(numDTO.getCounted() + 1);
                }
                if (AssetCountingStatus.ABNORMAL.getStatus().equals(status)) {
                    numDTO.setAbnormal(numDTO.getAbnormal() + 1);
                }
            }
        }

        return numDTO;
    }

    @Override
    public void saveBatchProcess(List<? extends AssetsProcess> processList) {

        List<AssetProcessField> fieldList = fieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                .eq(AssetProcessField::getProcessType, AssetProcessType.COUNTING_PROCESS.getCode()));

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
