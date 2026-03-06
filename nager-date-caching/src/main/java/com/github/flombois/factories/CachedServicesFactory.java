package com.github.flombois.factories;

import com.github.flombois.caching.strategies.CachingStrategy;
import com.github.flombois.caching.strategies.DefaultCachingStrategy;
import com.github.flombois.factories.caches.CacheFactory;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.v3.CachedCountryV3Service;
import com.github.flombois.services.v3.CachedLongWeekendV3Service;
import com.github.flombois.services.v3.CachedPublicHolidayV3Service;
import com.github.flombois.services.v3.CountryV3Service;
import com.github.flombois.services.v3.LongWeekendV3Service;
import com.github.flombois.services.v3.PublicHolidayV3Service;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link ServicesFactory} decorator that wraps services with caching behavior.
 * <p>
 * Delegates service creation to an underlying factory and wraps each service
 * with a cached decorator using strategies created from the provided {@link CacheFactory}.
 * </p>
 *
 * @since 1.0
 */
public class CachedServicesFactory implements ServicesFactory {

    private final ServicesFactory delegateFactory;
    private final CacheFactory cacheFactory;

    /**
     * Creates a new cached services factory.
     *
     * @param delegateFactory the underlying factory to delegate service creation to, must not be null
     * @param cacheFactory    the factory used to create cache instances for strategies, must not be null
     * @throws NullPointerException if either argument is null
     */
    public CachedServicesFactory(ServicesFactory delegateFactory, CacheFactory cacheFactory) {
        Objects.requireNonNull(delegateFactory, "delegateFactory must not be null");
        Objects.requireNonNull(cacheFactory, "cacheFactory must not be null");
        this.delegateFactory = delegateFactory;
        this.cacheFactory = cacheFactory;
    }

    /**
     * Creates a new {@link DefaultCachingStrategy} backed by a cache from the {@link CacheFactory}.
     *
     * @param <T> the type of values handled by the strategy
     * @return a new caching strategy instance
     */
    public <T> CachingStrategy<T> createCachingStrategy() {
        return new DefaultCachingStrategy<>(cacheFactory.create());
    }

    @Override
    public CountryV3Service createCountryV3Service() {
        final CountryV3Service delegate = delegateFactory.createCountryV3Service();
        final CachingStrategy<CountryInfoWithBorders> countryInfoCachingStrategy = createCachingStrategy();
        final CachingStrategy<Set<CountryV3>> countrySetCachingStrategy = createCachingStrategy();
        return new CachedCountryV3Service(countryInfoCachingStrategy, countrySetCachingStrategy, delegate);
    }

    @Override
    public LongWeekendV3Service createLongWeekendV3Service() {
        final LongWeekendV3Service delegate = delegateFactory.createLongWeekendV3Service();
        final CachingStrategy<Set<LongWeekendV3>> cachingStrategy = createCachingStrategy();
        return new CachedLongWeekendV3Service(cachingStrategy, delegate);
    }

    @Override
    public PublicHolidayV3Service createPublicHolidayV3Service() {
        final PublicHolidayV3Service delegate = delegateFactory.createPublicHolidayV3Service();
        final CachingStrategy<Set<PublicHolidayV3>> cachingStrategy = createCachingStrategy();
        return new CachedPublicHolidayV3Service(cachingStrategy, delegate);
    }
}
