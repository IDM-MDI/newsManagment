package ru.clevertec.newsmanagement.newsservice.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.newsservice.cache.Cache;
import ru.clevertec.newsmanagement.newsservice.cache.CacheList;
import ru.clevertec.newsmanagement.newsservice.cache.CustomCacheManager;
import ru.clevertec.newsmanagement.newsservice.cache.DeleteCache;
import ru.clevertec.newsmanagement.newsservice.cache.GetCache;
import ru.clevertec.newsmanagement.newsservice.cache.PostCache;
import ru.clevertec.newsmanagement.newsservice.cache.UpdateCache;
import ru.clevertec.newsmanagement.newsservice.util.AspectUtil;
import ru.clevertec.newsmanagement.newsservice.util.SpelExpression;

import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CacheAspect {

    private final CustomCacheManager customCacheManager;

    /**
     * A method-level advice that intercepts methods annotated with {@link GetCache} annotation
     * and provides caching functionality.
     * @param joinPoint The join point that represents the method execution.
     * @param getCache The annotation that indicates the method is cacheable.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("@annotation(getCache)")
    public Object getCacheAdvise(ProceedingJoinPoint joinPoint, GetCache getCache) throws Throwable {
        CacheList cache = customCacheManager.getCache(getCache.type());
        String evaluatedKey = SpelExpression.getKeyValue(joinPoint, getCache.key());
        Optional<Cache> cacheObject = cache.remove(evaluatedKey);
        if (cacheObject.isPresent()) {
            return customCacheManager.hitCache(cache, cacheObject.get());
        }
        return cache.add(evaluatedKey, joinPoint.proceed())
                .getValue();
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
        CacheList cache = customCacheManager.getCache(saveCache.type());
        String id = AspectUtil.getFieldValue(result,saveCache.fieldName());
        cache.add(id, result);
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
        CacheList cache = customCacheManager.getCache(deleteCache.type());
        String evaluatedKey = SpelExpression.getKeyValue(joinPoint, deleteCache.key());
        Optional<Cache> cacheObject = cache.findByKey(evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(c -> cache.remove(c.getKey()));
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
        CacheList cache = customCacheManager.getCache(updateCache.type());
        Optional<Cache> cacheObject = cache.findByKey(evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(k -> k.setValue(result));
        return cache.add(evaluatedKey, joinPoint.proceed())
                .getValue();
    }
}
