package com.hexing.asset.zxy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.domain.AssetsProcess;
import com.hexing.asset.mapper.AssetProcessFieldMapper;
import com.hexing.asset.mapper.AssetsProcessMapper;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.FieldAccessor_Long;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 80010641
 */
@Api(tags = "测试")
@RestController
@RequestMapping("/test")
public class TestDemo {

    @Resource
    private AssetsProcessMapper assetsProcessMapper;
    @Resource
    private AssetProcessFieldMapper assetProcessFieldMapper;

    @GetMapping(value = "/testSearch")
    public List<AssetsProcess> testSearch(PdProcessDomain pdProcessDomain) {
        {
            //前置处理
        }

        pdProcessDomain.setProcessType("100");
        List<AssetProcessField> searchDomain = getSearchDomain(pdProcessDomain);
        List<AssetsProcess> assetsProcesses = searchAssetProcess(searchDomain, pdProcessDomain);
        //后置处理
        {
            //关联表查询，以及其他字段显示
            for (AssetsProcess assetsProcess : assetsProcesses) {
                List<AssetProcessVariable> variableList = assetsProcess.getVariableList();
            }
        }

        return assetsProcesses;
    }

    /**
     * 获取字段配置并过滤
     * @param processDomain
     * @return
     */
    private List<AssetProcessField> getSearchDomain(PdProcessDomain processDomain) {
        JSONObject obj = JSONUtil.parseObj(processDomain);
        Set<String> keySet = obj.keySet();
        LambdaQueryWrapper<AssetProcessField> fieldWrapper = new LambdaQueryWrapper<>();
        fieldWrapper.eq(AssetProcessField::getProcessType, processDomain.getProcessType()).in(AssetProcessField::getFieldKey, keySet);
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
        List<AssetsProcess> assetsProcesses = assetsProcessMapper.selectProcessWithDomain(process, searchDomains);
        if (CollectionUtil.isNotEmpty(assetsProcesses)) {
            List<Long> processIds = assetsProcesses.stream().map(AssetsProcess::getId).collect(Collectors.toList());
            List<AssetProcessVariable> varList = assetsProcessMapper.selectVarWithProcessIds(processIds);
            Map<Long, List<AssetProcessVariable>> varMap = varList.stream().collect(Collectors.groupingBy(AssetProcessVariable::getProcessId));
            assetsProcesses.forEach(p -> p.setVariableList(varMap.getOrDefault(p.getId(), Collections.emptyList())));
        }
        return assetsProcesses;
    }


}
