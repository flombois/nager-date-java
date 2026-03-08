package com.github.flombois.models;

import com.neovisionaries.i18n.CountryCode;

/**
 * Holds the count of public holidays that do not fall on weekends for a given country.
 *
 * @param countryCode the ISO 3166-1 alpha-2 country code
 * @param count       the number of public holidays falling on weekdays
 * @since 1.0
 */
public record WeekdayHolidayCount(CountryCode countryCode, int count) {}
