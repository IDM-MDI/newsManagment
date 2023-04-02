package ru.clevertec.newsmanagement.cache.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.cache.Cache;
import ru.clevertec.newsmanagement.cache.CacheList;

import java.util.LinkedList;
import java.util.Optional;

@Component
@Profile("lru")
public class LruCacheList implements CacheList {
    private LinkedList<Cache> caches;

    public LruCacheList() {
        caches = new LinkedList<>();
    }
    @Override
    public Cache add(String key, Object value) {
        return add(new LruCache(key, value));
    }

    @Override
    public Cache add(Cache cache) {
        caches.addFirst(cache);
        return cache;
    }

    @Override
    public Optional<Cache> remove(String key) {
        Optional<Cache> byKey = findByKey(key);
        byKey.ifPresent(caches::remove);
        return byKey;
    }

    @Override
    public Optional<Cache> findByKey(String key) {
        return caches.stream()
                .filter(cache -> cache.getKey().equals(key))
                .findFirst();
    }

    @Override
    public CacheList createList() {
        return new LruCacheList();
    }
}