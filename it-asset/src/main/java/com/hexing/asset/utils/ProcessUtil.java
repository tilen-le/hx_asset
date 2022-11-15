package com.hexing.asset.utils;

import com.hexing.assetnew.domain.AssetProcessField;
import com.hexing.assetnew.domain.AssetProcessVariable;
import com.hexing.common.utils.bean.BeanTool;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessUtil {

    public static Object wrapProcessObject(Class clazz, List<AssetProcessField> fieldList, List<AssetProcessVariable> varList) {
        Map<Long, AssetProcessField> fieldMap = fieldList
                .stream().collect(Collectors.toMap(AssetProcessField::getId, x -> x));
        Object obj = new Object();
        try {
            obj = clazz.newInstance();
            for (AssetProcessVariable var : varList) {
                BeanTool.setFieldValue(obj, fieldMap.get(Long.valueOf(var.getFieldId())).getFieldKey(), var.getFieldValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
