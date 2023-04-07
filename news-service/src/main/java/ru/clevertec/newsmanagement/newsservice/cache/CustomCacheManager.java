package ru.clevertec.newsmanagement.newsservice.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.newsservice.cache.impl.LfuCacheList;
import ru.clevertec.newsmanagement.newsservice.cache.impl.LruCacheList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is a custom cache manager that manages a map of cache lists. It provides methods for retrieving a cache list
 * based on a given class, creating a new cache list if necessary, and hitting a cache.
 * The type of cache list created is determined by the "cache.type" property, and the size of the cache list is determined
 * by the "cache.size" property.
 * @author Dayanch
 */
@Component
public class CustomCacheManager {

    /**
     * A map of cache lists, with the class as the key and the cache list as the value.
     */
    private Map<Class<?>, CacheList> cache;

    /**
     * The type of cache list to create.
     */
    @Value("${cache.type}")
    private String type;

    /**
     * The maximum size of the cache list.
     */
    @Value("${cache.size}")
    private int size;

    public CustomCacheManager() {
        cache = new HashMap<>();
    }


    /**
     * Returns the cache list for the given class. If the cache list does not exist, creates a new one
     * and adds it to the cache map.
     * @param classname the class to retrieve the cache list for
     * @return the cache list for the given class
     */
    public CacheList getCache(Class<?> classname) {
        CacheList list = cache.get(classname);
        if(Objects.isNull(list)) {
            list = createNewCache();
            cache.put(classname, list);
        }
        return list;
    }

    /**
     * Creates a new cache list based on the "cache.type" property and the "cache.size" property.
     * If "cache.type" is "lru", returns an LruCacheList with the given size.
     * If "cache.type" is "lfu", returns an LfuCacheList with the given size.
     * @return a new cache list based on the "cache.type" property and the "cache.size" property
     */
    private CacheList createNewCache() {
        return "lru".equals(type) ? new LruCacheList(size) : new LfuCacheList(size);
    }

    /**
     * Hits the given cache by incrementing its hit count, adds it to the given cache list, and returns its value.
     * @param list the cache list to add the cache to
     * @param cache the cache to hit
     * @return the value of the given cache
     */
    public Object hitCache(CacheList list, Cache cache) {
        cache.hit();
        list.add(cache);
        return cache.getValue();
    }
}
