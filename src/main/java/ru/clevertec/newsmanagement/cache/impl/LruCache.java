package ru.clevertec.newsmanagement.cache.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.clevertec.newsmanagement.cache.Cache;

@ToString
@EqualsAndHashCode(callSuper = false)
public class LruCache extends Cache implements Comparable<LruCache> {
    private int hit;
    public LruCache(String key, Object value) {
        super(key,value);
        hit = 1;
    }
    @Override
    public void hit() {
        hit++;
    }
    @Override
    public int compareTo(LruCache o) {
        return o.hit - hit;
    }
}
