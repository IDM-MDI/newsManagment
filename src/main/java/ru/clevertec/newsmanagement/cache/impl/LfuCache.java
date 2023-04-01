package ru.clevertec.newsmanagement.cache.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.clevertec.newsmanagement.cache.Cache;

@ToString
@EqualsAndHashCode(callSuper = false)
public class LfuCache extends Cache implements Comparable<LfuCache>{
    private long lastAccessedTime;

    public LfuCache(String key, Object value) {
        super(key,value);
        lastAccessedTime = System.currentTimeMillis();
    }
    @Override
    public void hit() {
        lastAccessedTime = System.currentTimeMillis();
    }
    @Override
    public int compareTo(LfuCache o) {
        return Math.toIntExact(o.lastAccessedTime - lastAccessedTime);
    }
}
