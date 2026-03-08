package com.github.flombois.caching.caches;

import java.util.Optional;

/**
 * A {@link Cache} implementation following the Null Object pattern.
 * <p>
 * All read operations result in cache misses and all write operations are no-ops.
 * Useful as a default when no caching is desired.
 * </p>
 *
 * @param <T> the type of values that would be stored in a real cache
 * @since 1.0
 */
public class NullCache<T> implements Cache<T> {

    @Override
    public Optional<CacheEntry<T>> get(String key) {
        return Optional.empty();
    }

    @Override
    public CacheEntry<T> put(String key, CacheEntry<T> entry) {
        return null;
    }

    @Override
    public void evict(String key) {
        // do nothing
    }

    @Override
    public void clear() {
        // do nothing
    }
}
