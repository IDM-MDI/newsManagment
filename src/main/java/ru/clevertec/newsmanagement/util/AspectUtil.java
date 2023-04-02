package ru.clevertec.newsmanagement.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class AspectUtil {
    private static final String DTO_POSTFIX = "_";
    public String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field + DTO_POSTFIX);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }
}
