package ru.clevertec.newsmanagement.newsservice.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.newsservice.cache.impl.LfuCacheList;
import ru.clevertec.newsmanagement.newsservice.cache.impl.LruCacheList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomCacheManager {
    private Map<Class<?>, CacheList> cache;
    @Value("${cache.type}")
    private String type;
    @Value("${cache.size}")
    private int size;
    public CustomCacheManager() {
        cache = new HashMap<>();
    }
    public CacheList getCache(Class<?> classname) {
        CacheList list = cache.get(classname);
        if(Objects.isNull(list)) {
            list = createNewCache();
            cache.put(classname, list);
        }
        return list;
    }

    private CacheList createNewCache() {
        return "lru".equals(type) ? new LruCacheList(size) : new LfuCacheList(size);
    }

    public Object hitCache(CacheList list, Cache cache) {
        cache.hit();
        list.add(cache);
        return cache.getValue();
    }
}
