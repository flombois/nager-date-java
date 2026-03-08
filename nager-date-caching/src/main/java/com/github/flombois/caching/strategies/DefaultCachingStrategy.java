package com.github.flombois.caching.strategies;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.CacheEntry;
import com.github.flombois.services.CachedServiceException;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

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

    private static final Logger LOGGER = Logger.getLogger(DefaultCachingStrategy.class.getName());

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

    /**
     * Creates a {@link CacheEntry} for the given result.
     * <p>
     * Subclasses may override to create specialized entries (e.g., with type information).
     * </p>
     *
     * @param result the value to wrap
     * @return a new cache entry
     */
    protected CacheEntry<T> createEntry(T result) {
        return new CacheEntry<>(result);
    }

    @Override
    public T cacheCall(String key, Callable<T> service) throws CachedServiceException {
        final var cachedValue = cache.get(key);

        try {
            if (cachedValue.isPresent()) {
                LOGGER.info("Cache hit: " + key);
                final CacheEntry<T> entry = cachedValue.get();
                return entry.getValue();
            } else {
                LOGGER.info("Cache miss: " + key);
                final var result = service.call();
                cache.put(key, createEntry(result));
                LOGGER.info("Cache store: " + key);
                return result;
            }
        } catch (Exception e) {
            throw new CachedServiceException(e);
        }
    }


}
