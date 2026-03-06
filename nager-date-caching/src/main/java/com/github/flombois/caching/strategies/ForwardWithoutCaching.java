package com.github.flombois.caching.strategies;

import com.github.flombois.services.CachedServiceException;

import java.util.concurrent.Callable;

/**
 * A {@link CachingStrategy} that bypasses caching entirely.
 * <p>
 * Every call is forwarded directly to the underlying service without
 * storing or retrieving results from a cache.
 * </p>
 *
 * @param <T> the type of values returned by the service calls
 * @since 1.0
 */
public class ForwardWithoutCaching<T> implements CachingStrategy<T> {

    @Override
    public T cacheCall(String key, Callable<T> service) throws CachedServiceException {
        try {
            return service.call();
        } catch (Exception e) {
            throw new CachedServiceException(e);
        }

    }
}
