package ru.clevertec.newsmanagement.cache.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.cache.Cache;
import ru.clevertec.newsmanagement.cache.CacheManager;

import java.util.SortedSet;

@Component
@Profile("lru")
public class LruCacheManager extends CacheManager {

    @Override
    public Object createCache(SortedSet<Cache> set, String key, Object value) {
        set.add(new LruCache(key, value));
        return value;
    }
}
