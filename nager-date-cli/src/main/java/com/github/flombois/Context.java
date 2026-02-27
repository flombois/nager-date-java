package com.github.flombois;

import com.github.flombois.http.NagerDateHttpClient;
import com.neovisionaries.i18n.CountryCode;

import java.time.Year;

/**
 * Immutable context record holding all runtime parameters for a CLI command execution.
 * <p>
 * Encapsulates the HTTP client, country code, year, output format, and other
 * optional parameters passed between command components.
 * </p>
 *
 * @param httpClient   the HTTP client used to communicate with the Nager.Date API
 * @param countryCode  the ISO 3166-1 alpha-2 country code
 * @param year         the target year for the query
 * @param outputFormat the desired output format (JSON, PLAIN, or TABLE)
 * @param subdivision  the subdivision code, or empty string for the whole country
 * @param offset       the day offset for public holiday checks
 * @param baseUrl             the base URL for the API, or empty string for the public endpoint
 * @param availableBridgeDays the number of days off that can be used to extend weekends
 * @since 1.0
 */
public record Context(
        NagerDateHttpClient httpClient,
        CountryCode countryCode,
        Year year,
        OutputFormat outputFormat,
        String subdivision,
        int offset,
        String baseUrl,
        int availableBridgeDays
) {
}
