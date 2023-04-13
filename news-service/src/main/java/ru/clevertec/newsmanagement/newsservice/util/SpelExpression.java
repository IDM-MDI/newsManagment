package ru.clevertec.newsmanagement.newsservice.util;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * This class provides utility methods for evaluating SpEL expressions.
 * It has a public method getKeyValue that takes a JoinPoint and a String key as input.
 * The method returns the value of the SpEL expression corresponding to the given key, using the
 * arguments of the join point as variables in the SpEL expression evaluation.
 * @author Dayanch
 */
@UtilityClass
public class SpelExpression {

    /**
     * Returns the value of the SpEL expression corresponding to the given key,
     * using the arguments of the join point as variables in the SpEL expression evaluation.
     * @param joinPoint the join point whose arguments are used as variables in SpEL evaluation
     * @param key the SpEL expression key whose value is to be returned
     * @return the value of the SpEL expression corresponding to the given key
     */
    public String getKeyValue(JoinPoint joinPoint, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
    }

    /**
     * Returns a map of variable names to their corresponding values in the join point arguments.
     * @param joinPoint the join point whose arguments are used as variables in SpEL evaluation
     * @return a map of variable names to their corresponding values in the join point arguments
     */
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
