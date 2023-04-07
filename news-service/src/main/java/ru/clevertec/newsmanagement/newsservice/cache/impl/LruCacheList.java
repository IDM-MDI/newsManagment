package ru.clevertec.newsmanagement.newsservice.cache.impl;

import ru.clevertec.newsmanagement.cache.Cache;
import ru.clevertec.newsmanagement.cache.CacheList;

import java.util.LinkedList;
import java.util.Optional;

public class LruCacheList implements CacheList {
    private LinkedList<Cache> caches;
    private final int size;
    public LruCacheList(int size) {
        caches = new LinkedList<>();
        this.size = size;
    }
    @Override
    public Cache add(String key, Object value) {
        return add(new LruCache(key, value));
    }

    @Override
    public Cache add(Cache cache) {
        removeLastIfOutSize();
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
        return new LruCacheList(size);
    }

    private void removeLastIfOutSize() {
        if(caches.size() >= size) {
            caches.removeLast();
        }
    }
}