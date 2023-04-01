package ru.clevertec.newsmanagement.cache.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.cache.Cache;
import ru.clevertec.newsmanagement.cache.CacheManager;

import java.util.SortedSet;

@Component
@Profile("lfu")
public class LfuCacheManager extends CacheManager {

    @Override
    public Object createCache(SortedSet<Cache> set, String key, Object value) {
        set.add(new LfuCache(key,value));
        return value;
    }
}
