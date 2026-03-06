package com.github.flombois.services.v3;

import com.github.flombois.caching.strategies.CachingStrategy;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.CachedService;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;

import java.util.Set;

/**
 * Cached decorator for {@link CountryV3Service}.
 * <p>
 * Uses two separate {@link CachingStrategy} instances because the service
 * has methods returning different types: {@link CountryInfoWithBorders} and {@link Set}&lt;{@link CountryV3}&gt;.
 * </p>
 *
 * @since 1.0
 */
public class CachedCountryV3Service extends CachedService<CountryV3Service, CountryInfoWithBorders>
        implements CountryV3Service {

    private final CachingStrategy<Set<CountryV3>> countrySetStrategy;

    /**
     * Creates a new cached country service.
     *
     * @param countryInfoStrategy the caching strategy for {@link CountryInfoWithBorders} results
     * @param countrySetStrategy  the caching strategy for {@link Set}&lt;{@link CountryV3}&gt; results
     * @param delegatedService    the underlying country service to delegate calls to
     */
    public CachedCountryV3Service(CachingStrategy<CountryInfoWithBorders> countryInfoStrategy,
                                  CachingStrategy<Set<CountryV3>> countrySetStrategy,
                                  CountryV3Service delegatedService) {
        super(countryInfoStrategy, delegatedService);
        this.countrySetStrategy = countrySetStrategy;
    }

    @Override
    public CountryInfoWithBorders getCountryInfoWithBorders(CountryCode countryCode) throws NagerDateServiceException {
        
        // First build the key
        final var cacheKey = getStrategy().buildKey(name(), "getCountryInfoWithBorders", countryCode);
        
        // Then let the cache strategy handle the call
        return  getStrategy().cacheCall(cacheKey, () -> getDelegatedService().getCountryInfoWithBorders(countryCode));
    }

    @Override
    public Set<CountryV3> getAllCountries() throws NagerDateServiceException {
        // First build the key
        final var cacheKey = countrySetStrategy.buildKey(name(), "getAllCountries");

        // Then let the cache strategy handle the call
        return countrySetStrategy.cacheCall(cacheKey, () -> getDelegatedService().getAllCountries());
    }
}
