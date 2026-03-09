package com.github.flombois.models;

import java.util.Objects;
import java.util.Set;

import com.neovisionaries.i18n.CountryCode;

/**
 * Represents information about a country including its neighboring countries.
 * <p>
 * This class extends {@link CountryInfo} and adds information about the countries
 * that share a border with it. It can be constructed with or without border information.
 * </p>
 *
 * @since 1.0
 */
public class CountryInfoWithBorders extends CountryInfo {

    private Set<CountryInfo> borders;

    /**
     * Returns the set of neighboring countries that share a border with this country.
     *
     * @return the set of border countries
     */
    public Set<CountryInfo> getBorders() {
        return borders;
    }

    /**
     * Sets the set of neighboring countries that share a border with this country.
     *
     * @param borders the set of border countries
     */
    public void setBorders(Set<CountryInfo> borders) {
        this.borders = borders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryInfoWithBorders that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(borders, that.borders);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(borders);
    }
}
