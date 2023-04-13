package ru.clevertec.newsmanagement.newsservice.cache.impl;

import ru.clevertec.newsmanagement.newsservice.cache.Cache;
import ru.clevertec.newsmanagement.newsservice.cache.CacheList;

import java.util.LinkedList;
import java.util.Optional;

/**
 * Implementation of CacheList.
 * @author Dayanch
 */
public class LfuCacheList implements CacheList {

    private LinkedList<Cache> caches;

    private final int size;

    public LfuCacheList(int size) {
        caches = new LinkedList<>();
        this.size = size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cache add(String key, Object value) {
        return add(new LfuCache(key, value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cache add(Cache lfuCache) {
        removeLastIfOutSize();
        LfuCache cache = (LfuCache) lfuCache;
        Optional<Cache> found = findByCount((LfuCache) lfuCache);
        found.ifPresentOrElse(
                c -> addByIndex(cache, c),
                () -> caches.addFirst(cache));
        return lfuCache;
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
     * Searches the list for the cache with the same count as the given LfuCache, and returns an optional containing the
     * cache if it is found, or an empty optional if no such cache exists.
     * @param lfuCache the LfuCache to compare counts with
     * @return an optional containing the cache with the same count as the given LfuCache, or an empty optional if no such cache exists
     */
    private Optional<Cache> findByCount(LfuCache lfuCache) {
        return caches.stream()
                .filter(cache -> ((LfuCache) cache).getCount() == lfuCache.getCount())
                .findFirst();
    }

    /**
     * Adds the given LfuCache to the list at the same index as the given Cache.
     * @param lfuCache the LfuCache to add to the list
     * @param cache the Cache to determine the index to add the LfuCache at
     */
    private void addByIndex(LfuCache lfuCache, Cache cache) {
        int index = caches.indexOf(cache);
        caches.add(index, lfuCache);
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