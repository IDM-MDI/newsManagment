package ru.clevertec.newsmanagement.newsservice.util;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

import static ru.clevertec.newsmanagement.newsservice.builder.impl.CommentBuilder.aComment;

@ExtendWith(MockitoExtension.class)
class AspectUtilTest {
    @Nested
    class GetFieldValue {
        @Test
        void getFieldValueShouldReturnCorrectName() throws NoSuchFieldException, IllegalAccessException {
            String fieldName = "username";
            String fieldValue = AspectUtil.getFieldValue(aComment().buildToDTO(), fieldName);
            Assertions.assertThat(fieldValue).isEqualTo("test username");
        }

        @Test
        void getFieldValueShouldReturnCorrectPrice() throws NoSuchFieldException, IllegalAccessException {
            String fieldName = "text";
            String fieldValue = AspectUtil.getFieldValue(aComment().buildToDTO(), fieldName);
            Assertions.assertThat(fieldValue).isEqualTo("test text");
        }
        @Test
        void getFieldValueShouldThrowNoSuchFieldException() {
            String fieldName = "invalid_field";
            Assertions.assertThatThrownBy(() -> AspectUtil.getFieldValue(aComment().buildToDTO(), fieldName))
                    .isInstanceOf(NoSuchFieldException.class)
                    .hasMessageContaining(fieldName);
        }
        @Test
        void getFieldValueShouldReturnThrowNPE() {
            DTO.Comment comment = null;
            String fieldName = "username";

            Assertions.assertThatThrownBy(() -> AspectUtil.getFieldValue(comment, fieldName))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}