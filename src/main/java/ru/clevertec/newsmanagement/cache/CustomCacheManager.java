package ru.clevertec.newsmanagement.cache;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Profile("lru | lfu")
public class CustomCacheManager {
    private Map<Class<?>, CacheList> cache;
    private CacheList bean;
    public CustomCacheManager(CacheList bean) {
        cache = new HashMap<>();
        this.bean = bean;
    }
    public CacheList getCache(Class<?> classname) {
        CacheList list = cache.get(classname);
        if(Objects.isNull(list)) {
            list = bean.createList();
            cache.put(classname, list);
        }
        return list;
    }
    public Object hitCache(CacheList list, Cache cache) {
        cache.hit();
        list.add(cache);
        return cache.getValue();
    }
}
