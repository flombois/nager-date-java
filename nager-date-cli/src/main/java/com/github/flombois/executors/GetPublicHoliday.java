package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;

import java.util.Objects;
import java.util.Set;

/**
 * Service executor that retrieves public holidays for a given country and year.
 *
 * @since 1.0
 */
public class GetPublicHoliday implements ServiceExecutor<Set<PublicHolidayV3>> {

    private PublicHolidayV3Service publicHolidayV3Service;

    /**
     * Constructs a GetPublicHoliday executor with the given public holiday service.
     *
     * @param publicHolidayV3Service the public holiday service to delegate to
     */
    public GetPublicHoliday(PublicHolidayV3Service publicHolidayV3Service) {
        this.publicHolidayV3Service = publicHolidayV3Service;
    }

    @Override
    public Set<PublicHolidayV3> callService(Context context) throws NagerDateServiceException {
        return publicHolidayV3Service.getPublicHolidays(context.countryCode(), context.year());
    }

    /**
     * Sets the public holiday service to delegate to.
     *
     * @param publicHolidayV3Service the public holiday service, must not be null
     * @throws NullPointerException if publicHolidayV3Service is null
     */
    public void setPublicHolidayV3Service(PublicHolidayV3Service publicHolidayV3Service) {
        Objects.requireNonNull(publicHolidayV3Service);
        this.publicHolidayV3Service = publicHolidayV3Service;
    }
}
