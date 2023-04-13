package ru.clevertec.newsmanagement.newsservice.cache.impl;

import lombok.Getter;
import lombok.ToString;
import ru.clevertec.newsmanagement.newsservice.cache.Cache;

@ToString
@Getter
public class LfuCache extends Cache implements Comparable<LfuCache> {

    private int count;

    public LfuCache(String key, Object value) {
        super(key,value);
        count = 1;
    }

    @Override
    public void hit() {
        count++;
    }

    @Override
    public int compareTo(LfuCache o) {
        return o.count - count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
