package com.github.flombois.factories;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.NullCache;
import com.github.flombois.factories.caches.CacheFactory;

/**
 * A {@link CacheFactory} that produces {@link NullCache} instances.
 * <p>
 * Used when no caching is desired; all created caches follow the Null Object pattern.
 * </p>
 *
 * @since 1.0
 */
public class NullCacheFactory implements CacheFactory {

    @Override
    public <T> Cache<T> create() {
        return new NullCache<>();
    }
}
