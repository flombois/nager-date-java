package com.github.flombois.models.v3;

import com.github.flombois.models.HolidayType;

import java.time.LocalDate;
import java.time.Year;
import com.neovisionaries.i18n.CountryCode;
import java.util.Set;

/**
 * Represents a public holiday from the Nager.Date API v3.
 * <p>
 * This model contains detailed information about a public holiday in a specific country,
 * including its date, names in different languages, holiday types, and applicability details.
 * </p>
 *
 * @since 1.0
 */
public class PublicHolidayV3 {

    private CountryCode countryCode;
    private LocalDate date;
    private String localName;
    private String name;
    private Set<HolidayType> types;
    private Set<String> counties;
    private boolean fixed;
    private boolean global;
    private Year launchYear;

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
     * Returns the date of the public holiday.
     *
     * @return the holiday date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the public holiday.
     *
     * @param date the holiday date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the name of the holiday in the local language.
     *
     * @return the local name
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Sets the name of the holiday in the local language.
     *
     * @param localName the local name
     */
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    /**
     * Returns the English name of the holiday.
     *
     * @return the holiday name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the English name of the holiday.
     *
     * @param name the holiday name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the set of holiday types (e.g., PUBLIC, BANK, SCHOOL).
     *
     * @return the holiday types
     */
    public Set<HolidayType> getTypes() {
        return types;
    }

    /**
     * Sets the set of holiday types.
     *
     * @param types the holiday types
     */
    public void setTypes(Set<HolidayType> types) {
        this.types = types;
    }

    /**
     * Returns the set of county or subdivision codes where this holiday applies.
     *
     * @return the county codes, or {@code null} if the holiday is nationwide
     */
    public Set<String> getCounties() {
        return counties;
    }

    /**
     * Sets the set of county or subdivision codes where this holiday applies.
     *
     * @param counties the county codes
     */
    public void setCounties(Set<String> counties) {
        this.counties = counties;
    }

    /**
     * Returns whether this holiday falls on the same date every year.
     *
     * @return {@code true} if the holiday date is fixed, {@code false} otherwise
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Sets whether this holiday falls on the same date every year.
     *
     * @param fixed {@code true} if the holiday date is fixed
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    /**
     * Returns whether this holiday applies to the entire country.
     *
     * @return {@code true} if the holiday is observed nationwide, {@code false} if regional
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Sets whether this holiday applies to the entire country.
     *
     * @param global {@code true} if the holiday is observed nationwide
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * Returns the year this holiday was first observed.
     *
     * @return the launch year, or {@code null} if unknown
     */
    public Year getLaunchYear() {
        return launchYear;
    }

    /**
     * Sets the year this holiday was first observed.
     *
     * @param launchYear the launch year
     */
    public void setLaunchYear(Year launchYear) {
        this.launchYear = launchYear;
    }

}
