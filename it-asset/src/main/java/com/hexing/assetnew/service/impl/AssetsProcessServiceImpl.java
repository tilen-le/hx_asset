package com.hexing.assetnew.service.impl;

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
    public List<AssetsProcess> list(AssetsProcess process) {
        List<AssetProcessField> searchDomain = getSearchDomain(process);
        PageUtil.startPage();
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
     * 盘点状态统计
     * @param taskCode 盘点任务编号
     * @return
     */
    @Override
    public CountingStatusNumDTO countingStatusCount(String taskCode) {
        final String FIELD_COUNTING_STATUS = "countingStatus";

        AssetProcessCountingDomain entity = new AssetProcessCountingDomain();
        entity.setTaskCode(taskCode);
        List<AssetProcessField> searchDomain = getSearchDomain(entity);
        List<AssetsProcess> processList = searchAssetProcess(searchDomain, entity);

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
                    numDTO.setNotCounted(numDTO.getNotCounted()+1);
                }
                if (AssetCountingStatus.COUNTED.getStatus().equals(status)) {
                    numDTO.setCounted(numDTO.getCounted()+1);
                }
                if (AssetCountingStatus.ABNORMAL.getStatus().equals(status)) {
                    numDTO.setAbnormal(numDTO.getAbnormal()+1);
                }
            }
        }

        return numDTO;
    }


}
