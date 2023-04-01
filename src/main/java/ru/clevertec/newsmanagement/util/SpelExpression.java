package ru.clevertec.newsmanagement.util;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@UtilityClass
public class SpelExpression {

    public String getKeyValue(JoinPoint joinPoint, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
    }

    private Map<String, Object> getVariableMap(JoinPoint joinPoint) {
        Map<String, Object> variableMap = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        IntStream.range(0, args.length)
                .forEach(i -> variableMap.put(parameterNames[i], args[i]));
        return variableMap;
    }
}
