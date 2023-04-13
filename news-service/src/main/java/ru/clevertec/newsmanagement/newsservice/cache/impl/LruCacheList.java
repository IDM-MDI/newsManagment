package ru.clevertec.newsmanagement.newsservice.cache.impl;

import ru.clevertec.newsmanagement.newsservice.cache.Cache;
import ru.clevertec.newsmanagement.newsservice.cache.CacheList;

import java.util.LinkedList;
import java.util.Optional;

/**
 * Implementation of CacheList.
 * @author Dayanch
 */
public class LruCacheList implements CacheList {

    private LinkedList<Cache> caches;
    private final int size;

    public LruCacheList(int size) {
        caches = new LinkedList<>();
        this.size = size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cache add(String key, Object value) {
        return add(new LruCache(key, value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cache add(Cache cache) {
        removeLastIfOutSize();
        caches.addFirst(cache);
        return cache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Cache> remove(String key) {
        Optional<Cache> byKey = findByKey(key);
        byKey.ifPresent(caches::remove);
        return byKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Cache> findByKey(String key) {
        return caches.stream()
                .filter(cache -> cache.getKey().equals(key))
                .findFirst();
    }

    /**
     * Removes the last cache in the list if the size of the list is greater than or equal to the maximum size allowed.
     * This method is called when a new cache is added to the list to ensure that the list does not grow beyond the maximum size.
     */
    private void removeLastIfOutSize() {
        if(caches.size() >= size) {
            caches.removeLast();
        }
    }
}