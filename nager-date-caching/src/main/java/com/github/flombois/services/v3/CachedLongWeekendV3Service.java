package com.github.flombois.services.v3;

import com.github.flombois.caching.strategies.CachingStrategy;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.services.CachedService;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;

import java.time.Year;
import java.util.Set;

/**
 * Cached decorator for {@link LongWeekendV3Service}.
 *
 * @since 1.0
 */
public class CachedLongWeekendV3Service extends CachedService<LongWeekendV3Service, Set<LongWeekendV3>>
        implements LongWeekendV3Service {

    /**
     * Creates a new cached long weekend service.
     *
     * @param strategy         the caching strategy for long weekend results
     * @param delegatedService the underlying long weekend service to delegate calls to
     */
    public CachedLongWeekendV3Service(CachingStrategy<Set<LongWeekendV3>> strategy, LongWeekendV3Service delegatedService) {
        super(strategy, delegatedService);
    }

    @Override
    public Set<LongWeekendV3> getLongWeekend(CountryCode countryCode, Year year, int availableBridgeDays, String subdivision) throws NagerDateServiceException {
        final var cacheKey = getStrategy().buildKey(name(), "getLongWeekend", countryCode, year, availableBridgeDays, subdivision);

        return getStrategy().cacheCall(cacheKey, () -> getDelegatedService().getLongWeekend(countryCode, year, availableBridgeDays, subdivision));
    }
}
