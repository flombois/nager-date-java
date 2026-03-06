package com.github.flombois.services.v3;

import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;

import java.time.Year;

import com.neovisionaries.i18n.CountryCode;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for accessing public holiday information from the Nager.Date API v3.
 * <p>
 * This interface defines operations for querying public holidays in different countries
 * and checking if today is a public holiday.
 * </p>
 *
 * @since 1.0
 */
public interface PublicHolidayV3Service {

    /**
     * Retrieves all public holidays for a specific country in the current year.
     * <p>
     * This is a convenience method that uses the current year. Delegates to
     * {@link #getPublicHolidays(CountryCode, Year)}.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @return a set of {@link PublicHolidayV3} objects representing the holidays in the current year
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    default Set<PublicHolidayV3> getPublicHolidays(CountryCode countryCode) throws NagerDateServiceException {
        return getPublicHolidays(countryCode, Year.now());
    }

    /**
     * Retrieves all public holidays for a specific country in the current year as an Optional.
     * <p>
     * This is a convenience method that wraps {@link #getPublicHolidays(CountryCode)}
     * in an Optional, returning empty if an error occurs.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @return an Optional containing a set of {@link PublicHolidayV3} objects, or empty on failure
     */
    default Optional<Set<PublicHolidayV3>> getOptionalPublicHolidays(CountryCode countryCode){
        try {
            return Optional.ofNullable(getPublicHolidays(countryCode));
        } catch (NagerDateServiceException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all public holidays for a specific country and year.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year for which to retrieve holidays
     * @return a set of {@link PublicHolidayV3} objects representing the holidays
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    Set<PublicHolidayV3> getPublicHolidays(CountryCode countryCode, Year year) throws NagerDateServiceException;

    /**
     * Retrieves all public holidays for a specific country and year as an Optional.
     * <p>
     * This is a convenience method that wraps {@link #getPublicHolidays(CountryCode, Year)}
     * in an Optional, returning empty if an error occurs.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year for which to retrieve holidays
     * @return an Optional containing a set of {@link PublicHolidayV3} objects, or empty on failure
     */
    default Optional<Set<PublicHolidayV3>> getOptionalPublicHolidays(CountryCode countryCode, Year year){
        try {
            return Optional.ofNullable(getPublicHolidays(countryCode, year));
        } catch (NagerDateServiceException e) {
            return Optional.empty();
        }
    }

    /**
     * Checks whether today is a public holiday in the specified country.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param countyCode the ISO 3166-2 subdivision code (optional)
     * @param offset the timezone offset from UTC in hours (range: -12 to +12) (optional)
     * @return true if today is a public holiday, false otherwise
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    boolean isTodayAPublicHoliday(CountryCode countryCode, String countyCode, int offset) throws NagerDateServiceException;

    /**
     * Checks whether today is a public holiday in the specified country, wrapped in an Optional.
     * <p>
     * This is a convenience method that wraps {@link #isTodayAPublicHoliday(CountryCode, String, byte)}
     * in an Optional, returning empty if an error occurs.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param countyCode the ISO 3166-2 subdivision code (optional)
     * @param offset the timezone offset from UTC in hours (range: -12 to +12) (optional)
     * @return an Optional containing {@code true} if today is a public holiday, {@code false} otherwise,
     *         or empty on failure
     */
    default Optional<Boolean> getOptionalTodayAPublicHoliday(CountryCode countryCode, String countyCode, int offset){
        try {
            return Optional.of(isTodayAPublicHoliday(countryCode, countyCode, offset));
        } catch (NagerDateServiceException e) {
            return Optional.empty();
        }
    }
}
