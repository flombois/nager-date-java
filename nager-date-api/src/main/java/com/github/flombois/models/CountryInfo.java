package com.github.flombois.models;

import com.neovisionaries.i18n.CountryCode;

import java.util.Objects;

/**
 * Represents information about a country.
 * <p>
 * This class contains metadata about a country including its common name, official name,
 * ISO country code, and region. It is designed as a non-record class to allow for extension
 * by subclasses (e.g., {@link CountryInfoWithBorders}).
 * </p>
 *
 * @since 1.0
 */
public class CountryInfo {

    private String commonName;
    private CountryCode countryCode;
    private String officialName;
    private String region;


    /**
     * Returns the common name of the country.
     *
     * @return the common name
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * Sets the common name of the country.
     *
     * @param commonName the common name
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * Returns the ISO 3166-1 alpha-2 country code.
     *
     * @return the country code
     */
    public CountryCode getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the ISO 3166-1 alpha-2 country code.
     *
     * @param countryCode the country code
     */
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Returns the official name of the country.
     *
     * @return the official name
     */
    public String getOfficialName() {
        return officialName;
    }

    /**
     * Sets the official name of the country.
     *
     * @param officialName the official name
     */
    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    /**
     * Returns the geographical region of the country.
     *
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the geographical region of the country.
     *
     * @param region the region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryInfo that)) return false;
        return Objects.equals(countryCode, that.countryCode)
                && Objects.equals(commonName, that.commonName)
                && Objects.equals(officialName, that.officialName)
                && Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, commonName, officialName, region);
    }
}
