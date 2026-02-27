package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.LongWeekendV3Service;

import java.util.Objects;
import java.util.Set;

/**
 * Service executor that retrieves long weekend opportunities for a given country and year.
 *
 * @since 1.0
 */
public class GetLongWeekend implements ServiceExecutor<Set<LongWeekendV3>> {

    private LongWeekendV3Service longWeekendV3Service;

    /**
     * Constructs a GetLongWeekend executor with the given long weekend service.
     *
     * @param longWeekendV3Service the long weekend service to delegate to
     */
    public GetLongWeekend(LongWeekendV3Service longWeekendV3Service) {
        this.longWeekendV3Service = longWeekendV3Service;
    }

    @Override
    public Set<LongWeekendV3> callService(Context context) throws NagerDateServiceException {
        return longWeekendV3Service.getLongWeekend(context.countryCode(), context.year());
    }

    /**
     * Sets the long weekend service to delegate to.
     *
     * @param longWeekendV3Service the long weekend service, must not be null
     * @throws NullPointerException if longWeekendV3Service is null
     */
    public void setLongWeekendV3Service(LongWeekendV3Service longWeekendV3Service) {
        Objects.requireNonNull(longWeekendV3Service);
        this.longWeekendV3Service = longWeekendV3Service;
    }


}
