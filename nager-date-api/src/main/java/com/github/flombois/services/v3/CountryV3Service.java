package com.github.flombois.services.v3;

import com.neovisionaries.i18n.CountryCode;
import java.util.Optional;
import java.util.Set;

import com.github.flombois.models.CountryInfo;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.NagerDateService;
import com.github.flombois.services.NagerDateServiceException;

/**
 * Service interface for accessing country information from the Nager.Date API v3.
 * <p>
 * This interface defines operations for retrieving information about countries,
 * including their borders and other metadata. It provides both checked exception
 * versions and Optional-based versions of the methods.
 * </p>
 *
 * @since 1.0
 */
public interface CountryV3Service extends NagerDateService {

    /**
     * Retrieves basic country information for the specified country.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @return a {@link CountryInfo} object containing country details
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    default CountryInfo getCountryInfo(CountryCode countryCode) throws NagerDateServiceException {
        return getCountryInfoWithBorders(countryCode);
    }

    /**
     * Retrieves basic country information as an Optional for the specified country.
     * <p>
     * This is a convenience method that wraps {@link #getCountryInfo(CountryCode)}
     * in an Optional, returning empty if an error occurs.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @return an Optional containing the {@link CountryInfo}, or empty on failure
     */
    default Optional<CountryInfo> getOptionalCountryInfo(CountryCode countryCode) {
        try {
            return Optional.ofNullable(getCountryInfo(countryCode));
        } catch (NagerDateServiceException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves country information including information about neighboring countries.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @return a {@link CountryInfoWithBorders} object containing country details and border information
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    CountryInfoWithBorders getCountryInfoWithBorders(CountryCode countryCode)
            throws NagerDateServiceException;

    /**
     * Retrieves country information including borders as an Optional.
     * <p>
     * This is a convenience method that wraps {@link #getCountryInfoWithBorders(CountryCode)}
     * in an Optional, returning empty if an error occurs.
     * </p>
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @return an Optional containing the {@link CountryInfoWithBorders}, or empty on failure
     */
    default Optional<CountryInfoWithBorders> getOptionalCountryInfoWithBorders(CountryCode countryCode) {
        try {
            return Optional.ofNullable(getCountryInfoWithBorders(countryCode));
        } catch (NagerDateServiceException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves information about all available countries.
     *
     * @return a set of {@link CountryV3} objects representing all available countries
     * @throws NagerDateServiceException if an error occurs while communicating with the API
     */
    Set<CountryV3> getAllCountries() throws NagerDateServiceException;
}
