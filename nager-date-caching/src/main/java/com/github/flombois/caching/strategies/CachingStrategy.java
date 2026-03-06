package com.github.flombois.caching.strategies;

import com.github.flombois.services.CachedServiceException;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Strategy interface for caching service call results.
 * <p>
 * Defines how cache keys are built and how service calls are intercepted
 * to provide caching behavior.
 * </p>
 *
 * @param <T> the type of values returned by the cached service calls
 * @since 1.0
 */
public interface CachingStrategy<T> {

    /**
     * Builds a cache key from a service name and optional parameters.
     *
     * @param serviceName the fully qualified name of the service, must not be null
     * @param params      optional parameters to include in the key
     * @return the generated cache key string
     * @throws NullPointerException if {@code serviceName} is null
     */
    default String buildKey(String serviceName, Object... params) {
        Objects.requireNonNull(serviceName, "Service name must not be null");
        return serviceName + ":" + Arrays.toString(params);
    }

    /**
     * Executes a service call with caching behavior determined by the implementation.
     *
     * @param key     the cache key identifying the result
     * @param service the service call to execute on a cache miss
     * @return the cached or freshly computed result
     * @throws CachedServiceException if the underlying service call fails
     */
    T cacheCall(String key, Callable<T> service) throws CachedServiceException;
}
