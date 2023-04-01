package ru.clevertec.newsmanagement.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;

@UtilityClass
public class AspectUtil {
    public Class<?> getReturnType(@NotNull ProceedingJoinPoint joinPoint) {
        Signature signature =  joinPoint.getSignature();
        return ((MethodSignature) signature).getReturnType();
    }
    public Class<?> getReturnType(@NotNull Object object) {
        return object.getClass();
    }

    public String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }
}
