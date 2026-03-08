package com.github.flombois.caching.caches;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * A {@link CacheEntry} that carries type information for serialization/deserialization.
 * <p>
 * Used by {@link FileSystemCache} to persist and reconstruct generic types correctly.
 * </p>
 *
 * @param <T> the type of the cached value
 * @since 1.0
 */
public class TypedCacheEntry<T> extends CacheEntry<T> {

    private final Type type;

    /**
     * Creates a new typed cache entry.
     *
     * @param value the value to cache, must not be null
     * @param type  the type descriptor for the cached value, must not be null
     * @throws NullPointerException if {@code value} or {@code type} is null
     */
    public TypedCacheEntry(T value, Type type) {
        super(value);
        Objects.requireNonNull(type, "Type must not be null");
        this.type = type;
    }

    /**
     * Returns the type descriptor for the cached value.
     *
     * @return the type descriptor, never null
     */
    public Type getType() {
        return type;
    }
}
