package com.github.flombois.services;

import com.github.flombois.caching.strategies.CachingStrategy;

import java.util.Objects;

/**
 * Abstract base class for cached service decorators.
 * <p>
 * Holds a reference to the delegated service and the primary {@link CachingStrategy}
 * used to cache its results. Subclasses implement the service interface and use the
 * strategy to intercept calls with caching behavior.
 * </p>
 *
 * @param <S> the type of the delegated service
 * @param <T> the type of the primary cached return value
 * @since 1.0
 */
public abstract class CachedService<S, T> {

    private final S delegatedService;
    private final CachingStrategy<T> strategy;

    /**
     * Creates a new cached service decorator.
     *
     * @param cachingStrategy  the caching strategy for the primary return type, must not be null
     * @param delegatedService the underlying service to delegate calls to, must not be null
     * @throws NullPointerException if either argument is null
     */
    public CachedService(CachingStrategy<T> cachingStrategy, S delegatedService) {
        Objects.requireNonNull(cachingStrategy);
        Objects.requireNonNull(delegatedService);
        this.strategy = cachingStrategy;
        this.delegatedService = delegatedService;
    }

    /**
     * Returns the underlying delegated service.
     *
     * @return the delegated service instance
     */
    public S getDelegatedService() {
        return delegatedService;
    }

    /**
     * Returns the caching strategy for the primary return type.
     *
     * @return the caching strategy
     */
    public CachingStrategy<T> getStrategy() {
        return strategy;
    }
}
