package ru.clevertec.newsmanagement.newsservice.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@ToString
@Getter
@Setter
public abstract class Cache {

    private String key;
    private Object value;

    protected Cache(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cache cache = (Cache) o;
        return Objects.equals(key, cache.key) && Objects.equals(value, cache.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode() + key.hashCode();
    }

    public abstract void hit();
}
