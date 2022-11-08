package com.hexing.common.utils.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 朱祥雨
 * 不考虑父类继承等特殊情况
 */
public class BeanTool {

    /**
     * getter方法接口定义
     */
    @FunctionalInterface
    public interface IGetter<T> extends Serializable {
        Object apply(T source);
    }

    /**
     * 获得set或get或is方法对应的标准属性名，其它前缀的方法名使用原名
     */
    private static String getGeneralField(Serializable fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String getOrSetMethodName = lambda.getImplMethodName();
        final String generalField = StrUtil.getGeneralField(getOrSetMethodName);
        return StrUtil.isEmpty(generalField) ? getOrSetMethodName : generalField;
    }

    private static SerializedLambda getSerializedLambda(Serializable fn) {
        //先检查缓存中是否已存在
        SerializedLambda lambda;
        try {
            //提取SerializedLambda并缓存
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(fn);
        } catch (Exception e) {
            throw new IllegalArgumentException("获取SerializedLambda异常, class=" + fn.getClass().getSimpleName(), e);
        }
        return lambda;
    }

    /**
     * 根据lambda获取属性名称
     *
     * @param fn
     * @param <T>
     * @return fieldName
     */
    public static <T> String convertToFieldName(IGetter<T> fn) {
        return getGeneralField(fn);
    }

    /**
     * 根据属性名称获取属性值（obj不包含fieldName返回null）
     *
     * @param obj
     * @param fieldName
     * @return 属性值fieldValue
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object fieldValue = BeanUtil.getFieldValue(obj, fieldName);
        return fieldValue;
    }

    /**
     * 根据属性名称对对象进行赋值（obj不包含fieldName跳过）
     *
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
        try {
            BeanUtil.setFieldValue(obj, fieldName, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据属性名称对对象进行赋值（obj不包含fieldName抛出异常）
     *
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldValueThrowEx(Object obj, String fieldName, String fieldValue) {
        BeanUtil.setFieldValue(obj, fieldName, fieldValue);
    }

    /**
     * 对象转Map
     */
    public static Map<String, Object> objectToMap(Object obj) {
        String SERIAL_VERSION_FIELD_NAME = "serialVersionUID";
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (SERIAL_VERSION_FIELD_NAME.equals(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                if (ObjectUtil.isNotNull(field.get(obj)) && !"".equals(field.get(obj))) {
                    map.put(field.getName(), field.get(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
