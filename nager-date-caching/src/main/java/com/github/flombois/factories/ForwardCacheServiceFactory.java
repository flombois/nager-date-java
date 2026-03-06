package com.github.flombois.factories;

/**
 * A convenience {@link CachedServicesFactory} that uses {@link NullCacheFactory},
 * effectively forwarding all service calls without caching.
 * <p>
 * Useful as a drop-in replacement when caching should be disabled
 * while retaining the decorator structure.
 * </p>
 *
 * @since 1.0
 */
public class ForwardCacheServiceFactory extends CachedServicesFactory {

    /**
     * Creates a new forwarding factory that delegates to the given factory without caching.
     *
     * @param delegateFactory the underlying factory to delegate service creation to
     */
    public ForwardCacheServiceFactory(ServicesFactory delegateFactory) {
        super(delegateFactory, new NullCacheFactory());
    }
}
