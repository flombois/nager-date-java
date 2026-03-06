package com.github.flombois.factories.caches;

import com.github.flombois.caching.caches.Cache;

/**
 * Abstract factory for creating {@link Cache} instances.
 *
 * @since 1.0
 */
public interface CacheFactory {

    /**
     * Creates a new {@link Cache} instance.
     *
     * @param <T> the type of values stored in the cache
     * @return a new cache instance
     */
    <T> Cache<T> create();
}
