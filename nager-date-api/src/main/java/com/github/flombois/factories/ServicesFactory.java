package com.github.flombois.factories;

import com.github.flombois.services.v3.CountryV3Service;
import com.github.flombois.services.v3.LongWeekendV3Service;
import com.github.flombois.services.v3.PublicHolidayV3Service;

/**
 * Abstract factory for creating Nager.Date API v3 service instances.
 * <p>
 * Implementations provide concrete service instances backed by different
 * transport mechanisms (e.g., HTTP).
 * </p>
 *
 * @since 1.0
 */
public interface ServicesFactory {

    /**
     * Creates a country service instance.
     *
     * @return the country service
     */
    CountryV3Service createCountryV3Service();

    /**
     * Creates a long weekend service instance.
     *
     * @return the long weekend service
     */
    LongWeekendV3Service createLongWeekendV3Service();

    /**
     * Creates a public holiday service instance.
     *
     * @return the public holiday service
     */
    PublicHolidayV3Service createPublicHolidayV3Service();
}
