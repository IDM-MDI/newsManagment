package ru.clevertec.newsmanagement.newsservice.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.spel.SpelEvaluationException;

import static org.mockito.Mockito.doReturn;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.NewsBuilder.aNews;

@ExtendWith(MockitoExtension.class)
class SpelExpressionTest {
    @Mock
    private JoinPoint joinPoint;
    @Mock
    private MethodSignature signature;
    @Nested
    class GetKeyValueFromMethod {
        @Test
        void getKeyValueFromMethodShouldReturnCorrectPrimitive() {
            String id = "id";
            doReturn(new Object[]{1})
                    .when(joinPoint).getArgs();
            doReturn(signature)
                    .when(joinPoint).getSignature();
            doReturn(new String[]{id})
                    .when(signature).getParameterNames();

            Object result = SpelExpression.getKeyValue(joinPoint,"#id");

            Assertions.assertThat(result).isEqualTo("1");
        }

        @Test
        void getKeyValueFromMethodShouldReturnCorrectObject() {
            String id = "#model.id";
            doReturn(new Object[]{aNews().buildToEntity()})
                    .when(joinPoint).getArgs();
            doReturn(signature)
                    .when(joinPoint).getSignature();
            doReturn(new String[]{"model"})
                    .when(signature).getParameterNames();

            Object result = SpelExpression.getKeyValue(joinPoint,id);

            Assertions.assertThat(result).isEqualTo("1");
        }

        @Test
        void getKeyValueFromMethodShouldReturnThrowSpelEvaluationException() {
            String id = "id";
            doReturn(new Object[]{1})
                    .when(joinPoint).getArgs();
            doReturn(signature)
                    .when(joinPoint).getSignature();
            doReturn(new String[]{id})
                    .when(signature).getParameterNames();
            Assertions.assertThatThrownBy(() -> SpelExpression.getKeyValue(joinPoint,id))
                    .isInstanceOf(SpelEvaluationException.class);
        }

        @Test
        void getKeyValueFromMethodShouldReturnThrowOutOfBoundsException() {
            String id = "id";
            doReturn(new Object[]{1,2})
                    .when(joinPoint).getArgs();
            doReturn(signature)
                    .when(joinPoint).getSignature();
            doReturn(new String[]{id})
                    .when(signature).getParameterNames();

            Assertions.assertThatThrownBy(() -> SpelExpression.getKeyValue(joinPoint,id))
                    .isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }
        @Test
        void getKeyValueFromMethodShouldReturnThrowNPE() {
            Assertions.assertThatThrownBy(() -> SpelExpression.getKeyValue(joinPoint,null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}