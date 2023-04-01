package ru.clevertec.newsmanagement.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@EqualsAndHashCode
@Getter
@Setter
public abstract class Cache {
    private String key;
    private Object value;

    protected Cache(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public abstract void hit();

}
