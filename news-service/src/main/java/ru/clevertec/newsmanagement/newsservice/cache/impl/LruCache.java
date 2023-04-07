package ru.clevertec.newsmanagement.newsservice.cache.impl;

import lombok.Getter;
import lombok.ToString;
import ru.clevertec.newsmanagement.newsservice.cache.Cache;

import java.util.Objects;

@ToString
@Getter
public class LruCache extends Cache implements Comparable<LruCache> {

    private long lastAccessedTime;

    public LruCache(String key, Object value) {
        super(key,value);
        lastAccessedTime = System.currentTimeMillis();
    }

    @Override
    public void hit() {
        lastAccessedTime = System.currentTimeMillis();
    }

    @Override
    public int compareTo(LruCache o) {
        return Math.toIntExact(o.lastAccessedTime - lastAccessedTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
