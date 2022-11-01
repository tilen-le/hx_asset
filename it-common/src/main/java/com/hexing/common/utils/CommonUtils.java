package com.hexing.common.utils;

import java.lang.reflect.Field;

public class CommonUtils {

    public static <T> T toNullStr(T cls) {
        Field[] fields = cls.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return cls;
        }
        for (Field field : fields) {
            if ("String".equals(field.getType().getSimpleName())) {
                field.setAccessible(true);
                try {
                    Object val = field.get(cls);
                    if (val == null) {
                        field.set(cls, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return cls;
    }
}
