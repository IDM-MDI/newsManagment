package ru.clevertec.newsmanagement.newsservice.cache;

import java.util.Optional;

public interface CacheList {
    Cache add(String key, Object value);
    Cache add(Cache cache);
    Optional<Cache> remove(String key);
    Optional<Cache> findByKey(String key);
    CacheList createList();
}
