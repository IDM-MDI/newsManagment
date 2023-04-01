package ru.clevertec.newsmanagement.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class CacheManager {
    private Map<Class<?>, SortedSet<Cache>> cache;
    protected CacheManager() {
        cache = new HashMap<>();
    }
    public SortedSet<Cache> getCache(Class<?> classname) {
        SortedSet<Cache> caches = cache.get(classname);
        if(Objects.isNull(caches)) {
            caches = new TreeSet<>();
            cache.put(classname, caches);
        }
        return caches;
    }
    public Optional<Cache> findByKey(SortedSet<Cache> set ,String key) {
        return set.stream()
                .filter(obj -> obj.getKey().equals(key))
                .findAny();
    }
    public Object getValue(Cache cache) {
        cache.hit();
        return cache.getValue();
    }

    public abstract Object createCache(SortedSet<Cache> set, String key, Object value);
}
