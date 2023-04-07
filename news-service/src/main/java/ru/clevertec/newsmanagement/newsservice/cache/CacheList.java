package ru.clevertec.newsmanagement.newsservice.cache;

import java.util.Optional;

/**
 * This interface defines the behavior of a cache list, which is a list of caches that can be added to, removed from,
 * and searched.
 * @author Dayanch
 */
public interface CacheList {

    /**
     * Adds a cache to the list with the given key and value.
     * @param key the key to associate with the cache
     * @param value the value to store in the cache
     * @return the cache that was added to the list
     */
    Cache add(String key, Object value);

    /**
     * Adds the given cache to the list.
     * @param cache the cache to add to the list
     * @return the cache that was added to the list
     */
    Cache add(Cache cache);

    /**
     * Removes the cache with the given key from the list, if it exists.
     * @param key the key of the cache to remove
     * @return an optional containing the cache that was removed, or an empty optional if no cache was removed
     */
    Optional<Cache> remove(String key);

    /**
     * Searches the list for a cache with the given key.
     * @param key the key to search for
     * @return an optional containing the cache with the given key, or an empty optional if no such cache exists
     */
    Optional<Cache> findByKey(String key);
}
