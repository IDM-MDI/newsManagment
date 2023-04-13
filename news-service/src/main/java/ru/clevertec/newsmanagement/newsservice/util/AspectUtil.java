package ru.clevertec.newsmanagement.newsservice.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

/**
 * This class provides utility methods for aspect-oriented programming.
 * It has a private constant DTO_POSTFIX and a public method getFieldValue.
 * @author Dayanch
 */
@UtilityClass
public class AspectUtil {

    /**
     * A constant string representing the postfix used to identify DTO fields.
     */
    private static final String DTO_POSTFIX = "_";

    /**
     * Returns the value of the field with the given name in the given object, by reflecting on its class.
     * The field name is expected to have a DTO_POSTFIX appended to it.
     * @param result the object whose field value is to be returned
     * @param field the name of the field whose value is to be returned
     * @return the value of the field with the given name in the given object
     * @throws NoSuchFieldException if the field with the given name does not exist in the object's class
     * @throws IllegalAccessException if the field with the given name is not accessible in the object's class
     */
    public String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field + DTO_POSTFIX);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }
}
