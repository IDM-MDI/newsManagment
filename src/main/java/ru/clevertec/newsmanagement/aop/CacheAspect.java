package ru.clevertec.newsmanagement.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.cache.Cache;
import ru.clevertec.newsmanagement.cache.CacheManager;
import ru.clevertec.newsmanagement.cache.DeleteCache;
import ru.clevertec.newsmanagement.cache.GetCache;
import ru.clevertec.newsmanagement.cache.PostCache;
import ru.clevertec.newsmanagement.cache.UpdateCache;
import ru.clevertec.newsmanagement.util.AspectUtil;
import ru.clevertec.newsmanagement.util.SpelExpression;

import java.util.Optional;
import java.util.SortedSet;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CacheAspect {
    private final CacheManager cacheManager;
    @Around("@annotation(getCache)")
    public Object getCacheAdvise(ProceedingJoinPoint joinPoint, GetCache getCache) throws Throwable {
        SortedSet<Cache> cache = cacheManager.getCache(AspectUtil.getReturnType(joinPoint));
        String evaluatedKey = SpelExpression.getKeyValue(joinPoint, getCache.key());
        Optional<Cache> cacheObject = cacheManager.findByKey(cache, evaluatedKey);
        if (cacheObject.isPresent()) {
            return cacheManager.getValue(cacheObject.get());
        }
        return cacheManager.createCache(cache, evaluatedKey, joinPoint.proceed());
    }


    /**
     * A method-level advice that intercepts methods annotated with {@link PostCache} annotation
     * and saves the result to the cache.
     * @param saveCache The annotation that indicates the method saves data to the cache.
     * @param result The result of the method execution.
     * @throws NoSuchFieldException If the specified field does not exist.
     * @throws IllegalAccessException If the specified field is not accessible.
     */
    @AfterReturning(value = "@annotation(saveCache)",returning = "result")
    public void saveCacheAdvise(PostCache saveCache, Object result) throws NoSuchFieldException, IllegalAccessException {
        SortedSet<Cache> cache = cacheManager.getCache(AspectUtil.getReturnType(result));
        String id = AspectUtil.getFieldValue(result,saveCache.fieldName());
        cacheManager.createCache(cache, id, result);
    }


    /**
     * A method-level advice that intercepts methods annotated with {@link DeleteCache} annotation
     * and removes the cached value associated with the specified key.
     * @param joinPoint The join point that represents the method execution.
     * @param deleteCache The annotation that indicates the method deletes data from the cache.
     * @return The result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("@annotation(deleteCache)")
    public Object deleteCacheAdvise(ProceedingJoinPoint joinPoint, DeleteCache deleteCache) throws Throwable {
        String evaluatedKey = SpelExpression.getKeyValue(joinPoint, deleteCache.key());
        SortedSet<Cache> cache = cacheManager.getCache(AspectUtil.getReturnType(joinPoint));
        Optional<Cache> cacheObject = cacheManager.findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(cache::remove);
        return result;
    }


    /**
     * A method-level advice that intercepts methods annotated with {@link UpdateCache} annotation
     * and updates the cached value associated with the specified key.
     * @param joinPoint The join point that represents the method execution.
     * @param updateCache The annotation that indicates the method updates data in the cache.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("@annotation(updateCache)")
    public Object updateCacheAdvise(ProceedingJoinPoint joinPoint, UpdateCache updateCache) throws Throwable {
        String evaluatedKey = SpelExpression.getKeyValue(joinPoint, updateCache.key());
        SortedSet<Cache> cache = cacheManager.getCache(AspectUtil.getReturnType(joinPoint));
        Optional<Cache> cacheObject = cacheManager.findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(k -> k.setValue(result));
        return cacheManager.createCache(cache, evaluatedKey, result);
    }
}
