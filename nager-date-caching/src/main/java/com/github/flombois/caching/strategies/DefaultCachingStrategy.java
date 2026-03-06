package com.github.flombois.caching.strategies;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.CacheEntry;
import com.github.flombois.services.CachedServiceException;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * A {@link CachingStrategy} implementing the cache-aside pattern.
 * <p>
 * On a cache hit, returns the cached value directly. On a cache miss,
 * delegates to the service, stores the result, and returns it.
 * </p>
 *
 * @param <T> the type of values returned by the cached service calls
 * @since 1.0
 */
public class DefaultCachingStrategy<T> implements CachingStrategy<T> {

    private final Cache<T> cache;

    /**
     * Creates a new strategy backed by the given cache.
     *
     * @param cache the cache to use for storing and retrieving results, must not be null
     * @throws NullPointerException if {@code cache} is null
     */
    public DefaultCachingStrategy(Cache<T> cache) {
        Objects.requireNonNull(cache);
        this.cache = cache;
    }


    @Override
    public T cacheCall(String key, Callable<T> service) throws CachedServiceException {
        final var cachedValue = cache.get(key);

        try {
            if (cachedValue.isPresent()) {
                // Cache Hit
                final CacheEntry<T> entry = cachedValue.get();
                // Check entry status and return value
                return entry.getValue();
            } else {
                // Cache miss
                final var result = service.call();
                cache.put(key, result);
                return result;
            }
        } catch (Exception e) {
            throw new CachedServiceException(e);
        }
    }


}
