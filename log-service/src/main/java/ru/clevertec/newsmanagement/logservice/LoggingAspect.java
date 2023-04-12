package ru.clevertec.newsmanagement.logservice;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The LoggingAspect class is used to log method calls and returns in the persistence, service, and controller layers of
 * the application. It is implemented as an aspect using Spring AOP.
 * @author Dayanch
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    /**
     * The message to be logged before a layer executes a method. Includes placeholders for the class name, method name,
     * and arguments.
     */
    private static final String BEFORE_LAYER = "Executing class \"{}\" with method \"{}\" with arguments \"{}\"";

    /**
     * The message to be logged after a layer returns from a method. Includes placeholders for the class name, method name,
     * and returned object.
     */
    private static final String AFTER_LAYER = "Returning from class \"{}\" with method \"{}\", returned obj \"{}\"";


    /**
     * The request object, injected via constructor injection. Used to log the request URL and HTTP method in the
     * controller layer.
     */
    private final HttpServletRequest request;


    /**
     * A pointcut expression for methods in the persistence layer (i.e. Spring Data repositories).
     */
    @Pointcut("this(org.springframework.data.repository.Repository)")
    public void inPersistenceLayer() {}


    /**
     * A pointcut expression for methods in the service layer (i.e. classes in the ru.clevertec.newsmanagement.newsservice.service.impl package).
     */
    @Pointcut("execution(* ru.clevertec.newsmanagement.*.service.impl.*.*(*))")
    public void inServiceLayer() {}


    /**
     * A pointcut expression for methods in the controller layer (i.e. classes in the ru.clevertec.newsmanagement.newsservice.controller package).
     */
    @Pointcut("execution(* ru.clevertec.newsmanagement.*.controller.*.*(*))")
    public void inControllerLayer() {}


    /**
     * A before advice to be executed before methods in the persistence or service layers.
     * Logs the method name, class name, and arguments.
     * @param joinPoint a special object that stores information about the method
     */
    @Before("inPersistenceLayer() || inServiceLayer()")
    public void beforeNonControllerLayerLogging(JoinPoint joinPoint) {
        beforeLayerLogging(joinPoint);
    }


    /**
     * An after-throwing advice to be executed after methods in the service layer throw a CustomException.
     * Logs the exception message at the ERROR level.
     * @param exception that are thrown in the service layer
     */
    @AfterThrowing(value = "inServiceLayer()", throwing = "exception")
    public void afterThrowing(CustomException exception) {
        Logger logger = (Logger) LoggerFactory.getLogger(CustomException.class);
        logger.error(exception.getMessage());
    }


    /**
     * A before advice to be executed before methods in the controller layer.
     * Logs the method name, class name, and arguments, as well as the request URL and HTTP method.
     * @param joinPoint a special object that stores information about the method
     */
    @Before("inControllerLayer()")
    public void beforeControllerLayerLogging(JoinPoint joinPoint) {
        beforeLayerLogging(joinPoint);
        Logger logger = (Logger) LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("URL: {}, Method: {}",
                request.getRequestURL().toString(),
                request.getMethod());
    }


    /**
     * An after-returning advice to be executed after methods in any layer return.
     * Logs the method name, class name, and returned object.
     * @param joinPoint a special object that stores information about the method
     * @param result the object that is returned by the method
     */
    @AfterReturning(value = "inPersistenceLayer() || inServiceLayer() || inControllerLayer()",
                    returning = "result")
    public void afterServiceLayerLogging(JoinPoint joinPoint, Object result) {
        afterLayerLogging(joinPoint,result);
    }


    /**
     * A private helper method to log the method name, class name, and arguments before a layer executes a method.
     * @param joinPoint a special object that stores information about the method
     */
    private static void beforeLayerLogging(JoinPoint joinPoint) {
        Logger logger = (Logger) LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info(BEFORE_LAYER,
                joinPoint.getTarget().getClass().getCanonicalName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * A private helper method to log the class name, method name, and returned object after a layer returns from a method.
     * @param joinPoint a special object that stores information about the method
     * @param result the object that is returned by the method
     */
    private static void afterLayerLogging(JoinPoint joinPoint, Object result) {
        Logger logger = (Logger) LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info(AFTER_LAYER,
                joinPoint.getTarget().getClass().getCanonicalName(),
                joinPoint.getSignature().getName(),
                result);
    }
}
