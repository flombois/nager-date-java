package com.github.flombois.models.v3;

import com.neovisionaries.i18n.CountryCode;

import java.util.Objects;

/**
 * Represents a country from the Nager.Date API v3.
 * <p>
 * This model contains basic country information including its common name and
 * ISO 3166-1 alpha-2 country code.
 * </p>
 *
 * @since 1.0
 */
public class CountryV3 {

    private CountryCode countryCode;
    private String name;

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
     * Returns the common name of the country.
     *
     * @return the country name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the common name of the country.
     *
     * @param name the country name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryV3 that)) return false;
        return Objects.equals(countryCode, that.countryCode) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, name);
    }

}
