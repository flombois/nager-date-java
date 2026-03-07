package com.github.flombois.caching.caches;

import java.util.Objects;

/**
 * A wrapper around a cached value. Null values are not permitted.
 *
 * @param <T> the type of the cached value
 * @since 1.0
 */
public class CacheEntry<T> {

    private final T value;

    /**
     * Creates a new cache entry wrapping the given value.
     *
     * @param value the value to cache, must not be null
     * @throws NullPointerException if {@code value} is null
     */
    public CacheEntry(T value) {
        Objects.requireNonNull(value, "Cached value must not be null");
        this.value = value;
    }

    /**
     * Returns the cached value.
     *
     * @return the cached value, never null
     */
    public T getValue() {
        return value;
    }
}
