package com.github.flombois.services.v3;

import com.github.flombois.caching.strategies.CachingStrategy;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.CachedService;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;

import java.time.Year;
import java.util.Set;

/**
 * Cached decorator for {@link PublicHolidayV3Service}.
 * <p>
 * Caches {@link #getPublicHolidays(CountryCode, Year)} results.
 * The {@link #isTodayAPublicHoliday(CountryCode, String, int)} method
 * bypasses the cache and delegates directly to the underlying service.
 * </p>
 *
 * @since 1.0
 */
public class CachedPublicHolidayV3Service extends CachedService<PublicHolidayV3Service, Set<PublicHolidayV3>>
        implements PublicHolidayV3Service {

    /**
     * Creates a new cached public holiday service.
     *
     * @param strategy         the caching strategy for public holiday results
     * @param delegatedService the underlying public holiday service to delegate calls to
     */
    public CachedPublicHolidayV3Service(CachingStrategy<Set<PublicHolidayV3>> strategy, PublicHolidayV3Service delegatedService) {
        super(strategy, delegatedService);
    }

    @Override
    public Set<PublicHolidayV3> getPublicHolidays(CountryCode countryCode, Year year) throws NagerDateServiceException {
        final var cacheKey = getStrategy().buildKey(name(), "getPublicHolidays", countryCode, year);
        return getStrategy().cacheCall(cacheKey, () -> getDelegatedService().getPublicHolidays(countryCode, year));
    }

    @Override
    public boolean isTodayAPublicHoliday(CountryCode countryCode, String countyCode, int offset) throws NagerDateServiceException {
        // Exceptionally, don't cache this one as a service call should not be expensive
        // TODO: Implement caching for this method later if necessary
        return getDelegatedService().isTodayAPublicHoliday(countryCode, countyCode, offset);
    }
}
