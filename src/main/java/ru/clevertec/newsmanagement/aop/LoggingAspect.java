package ru.clevertec.newsmanagement.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {
    private static final String BEFORE_LAYER = "Executing class \"{}\" with method \"{}\" with arguments \"{}\"";
    private static final String AFTER_LAYER = "Returning from class \"{}\" with method \"{}\", returned obj \"{}\"";

    private final HttpServletRequest request;

    @Pointcut("this(org.springframework.data.repository.Repository)")
    public void inPersistenceLayer() {}
    @Pointcut("execution(* ru.clevertec.newsmanagement.service.impl.*.*(*))")
    public void inServiceLayer() {}
    @Pointcut("execution(* ru.clevertec.newsmanagement.controller.*.*(*))")
    public void inControllerLayer() {}

    @Before("inPersistenceLayer() || inServiceLayer()")
    public void beforeNonControllerLayerLogging(JoinPoint joinPoint) {
        beforeLayerLogging(joinPoint);
    }

    @Before("inControllerLayer()")
    public void beforeControllerLayerLogging(JoinPoint joinPoint) throws IOException {
        beforeLayerLogging(joinPoint);
        log.info("URL: {}, Method: {}",
                request.getRequestURL().toString(),
                request.getMethod());
    }

    @AfterReturning(value = "inPersistenceLayer() || inServiceLayer() || inControllerLayer()",
                    returning = "result")
    public void afterServiceLayerLogging(JoinPoint joinPoint, Object result) {
        afterLayerLogging(joinPoint,result);
    }
    private static void beforeLayerLogging(JoinPoint joinPoint) {
        log.info(BEFORE_LAYER,
                joinPoint.getTarget().getClass().getCanonicalName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
    private static void afterLayerLogging(JoinPoint joinPoint, Object result) {
        log.info(AFTER_LAYER,
                joinPoint.getTarget().getClass().getCanonicalName(),
                joinPoint.getSignature().getName(),
                result.toString());
    }
}
