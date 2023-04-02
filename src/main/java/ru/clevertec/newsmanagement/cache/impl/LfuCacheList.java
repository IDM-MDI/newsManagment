package ru.clevertec.newsmanagement.cache.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.cache.Cache;
import ru.clevertec.newsmanagement.cache.CacheList;

import java.util.LinkedList;
import java.util.Optional;

@Component
@Profile("lfu")
public class LfuCacheList implements CacheList {
    private LinkedList<Cache> caches;

    public LfuCacheList() {
        caches = new LinkedList<>();
    }

    @Override
    public Cache add(String key, Object value) {
        return add(new LfuCache(key, value));
    }

    @Override
    public Cache add(Cache lfuCache) {
        LfuCache cache = (LfuCache) lfuCache;
        Optional<Cache> found = findByCount((LfuCache) lfuCache);
        found.ifPresentOrElse(
                c -> addByIndex(cache, c),
                () -> caches.addFirst(cache));
        return lfuCache;
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
        return new LfuCacheList();
    }

    private Optional<Cache> findByCount(LfuCache lfuCache) {
        return caches.stream()
                .filter(cache -> ((LfuCache) cache).getCount() == lfuCache.getCount())
                .findFirst();
    }

    private void addByIndex(LfuCache lfuCache, Cache cache) {
        int index = caches.indexOf(cache);
        caches.add(index, lfuCache);
    }
}