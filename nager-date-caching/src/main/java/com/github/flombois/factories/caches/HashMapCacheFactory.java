package com.github.flombois.factories.caches;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.MapCache;

import java.util.HashMap;

/**
 * A {@link CacheFactory} that creates {@link MapCache} instances backed by {@link HashMap}.
 * <p>
 * Used for in-memory, non-persistent caching within a single application invocation.
 * </p>
 *
 * @since 1.0
 */
public class HashMapCacheFactory implements CacheFactory {

    @Override
    public <T> Cache<T> create() {
        return new MapCache<>(new HashMap<>());
    }
}
