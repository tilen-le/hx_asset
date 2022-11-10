package com.hexing.asset.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.domain.AssetsProcess;
import com.hexing.asset.mapper.AssetProcessVariableMapper;
import com.hexing.asset.mapper.AssetsProcessMapper;
import com.hexing.asset.service.IAssetProcessFieldService;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.asset.service.IAssetsProcessService;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    /**
     * 查询资产流程
     *
     * @param id 资产流程主键
     * @return 资产流程
     */
    @Override
    public AssetsProcess selectAssetsProcessById(Long id)
    {
        return processMapper.selectAssetsProcessById(id);
    }

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
     *
     * @param process 资产流程
     * @return 资产流程
     */
    @Override
    public List<AssetsProcess> selectAssetsProcessList(AssetsProcess process)
    {
//        LambdaQueryWrapper<AssetsProcess> processWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(process.getProcessType())) {
//            processWrapper.eq(AssetsProcess::getProcessType, process.getProcessType());
//        }
//        List<AssetsProcess> processList = list(processWrapper);
//
//        LambdaQueryWrapper<AssetProcessVariable> variableWrapper = new LambdaQueryWrapper<>();
//
//        variableWrapper.in(AssetProcessVariable::getProcessId,
//                processList.stream().map(AssetsProcess::getId).collect(Collectors.toList()));
//        List<AssetProcessVariable> variableList = variableService.list(variableWrapper);





        return null;
    }

    /**
     * 新增资产流程
     *
     * @param assetsProcess 资产流程
     * @return 结果
     */
    @Override
    public int insertAssetsProcess(AssetsProcess assetsProcess)
    {
        assetsProcess.setCreateTime(DateUtils.getNowDate());
        return processMapper.insertAssetsProcess(assetsProcess);
    }

    /**
     * 修改资产流程
     *
     * @param assetsProcess 资产流程
     * @return 结果
     */
    @Override
    public int updateAssetsProcess(AssetsProcess assetsProcess)
    {
        assetsProcess.setUpdateTime(DateUtils.getNowDate());
        return processMapper.updateAssetsProcess(assetsProcess);
    }

    /**
     * 批量删除资产流程
     *
     * @param ids 需要删除的资产流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetsProcessByIds(Long[] ids)
    {
        return processMapper.deleteAssetsProcessByIds(ids);
    }

    /**
     * 删除资产流程信息
     *
     * @param id 资产流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetsProcessById(Long id)
    {
        return processMapper.deleteAssetsProcessById(id);
    }

    @Override
    public List<AssetsProcess> selectProcessWithCondition(String processType, Map<String, Object> params) {

        List<AssetsProcess> processList = processMapper.selectProcessWithCondition(processType, params);

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
