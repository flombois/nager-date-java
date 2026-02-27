package com.github.flombois.services.v3;

import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.services.NagerDateServiceException;

import java.time.Year;
import java.util.Set;
import com.neovisionaries.i18n.CountryCode;

/**
 * Service interface for accessing long weekend information from the Nager.Date API v3.
 * <p>
 * This interface provides operations for retrieving information about potential long
 * weekends based on public holidays and available bridge days. It includes convenience
 * methods with default parameters for common use cases.
 * </p>
 *
 * @since 1.0
 */
public interface LongWeekendV3Service {

    /** Default number of available bridge days (days off that can be used to extend a weekend). */
    int DEFAULT_AVAILABLE_BRIDGE_DAYS = 1;

    /** Default subdivision code (empty string indicates no specific subdivision). */
    String DEFAULT_SUBDIVISION_CODE = "";

    /**
     * Retrieves long weekend opportunities for a country and year using default parameters.
     * <p>
     * Uses the default number of bridge days (1) and no specific subdivision code.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year for which to retrieve long weekends
     * @return a set of {@link LongWeekendV3} objects containing long weekend information
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    default Set<LongWeekendV3> getLongWeekend(CountryCode countryCode, Year year) throws NagerDateServiceException {
        return getLongWeekend(countryCode, year, DEFAULT_AVAILABLE_BRIDGE_DAYS, DEFAULT_SUBDIVISION_CODE);
    }

    /**
     * Retrieves long weekend opportunities for a country and year with a specified number of bridge days.
     * <p>
     * Uses no specific subdivision code.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year for which to retrieve long weekends
     * @param availableBridgeDays the number of days off that can be used to extend weekends
     * @return a set of {@link LongWeekendV3} objects containing long weekend information
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    default Set<LongWeekendV3> getLongWeekend(CountryCode countryCode, Year year, int availableBridgeDays)
            throws NagerDateServiceException {
        return getLongWeekend(countryCode, year, availableBridgeDays, DEFAULT_SUBDIVISION_CODE);
    }

    /**
     * Retrieves long weekend opportunities for a country, year, and subdivision using default bridge days.
     * <p>
     * Uses the default number of bridge days (1).
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year for which to retrieve long weekends
     * @param subdivisionCode the subdivision or state code (empty string for the whole country)
     * @return a set of {@link LongWeekendV3} objects containing long weekend information
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    default Set<LongWeekendV3> getLongWeekend(CountryCode countryCode, Year year, String subdivisionCode)
            throws NagerDateServiceException {
        return getLongWeekend(countryCode, year, DEFAULT_AVAILABLE_BRIDGE_DAYS, subdivisionCode);
    }

    /**
     * Retrieves long weekend opportunities for a country, year, number of bridge days, and subdivision.
     * <p>
     * This is the main method that all other convenience methods delegate to.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year for which to retrieve long weekends
     * @param availableBridgeDays the number of days off that can be used to extend weekends
     * @param subdivisionCode the subdivision or state code (empty string for the whole country)
     * @return a set of {@link LongWeekendV3} objects containing long weekend information
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    Set<LongWeekendV3> getLongWeekend(CountryCode countryCode, Year year, int availableBridgeDays, String subdivisionCode)
            throws NagerDateServiceException;

}
