package com.github.flombois.factories;

import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.services.v3.CountryV3Service;
import com.github.flombois.services.v3.HttpCountryV3Service;
import com.github.flombois.services.v3.HttpLongWeekendV3Service;
import com.github.flombois.services.v3.HttpPublicHolidayV3Service;
import com.github.flombois.services.v3.LongWeekendV3Service;
import com.github.flombois.services.v3.PublicHolidayV3Service;

import java.util.Objects;

/**
 * HTTP-based implementation of {@link ServicesFactory}.
 * <p>
 * Creates service instances that communicate with the Nager.Date API
 * over HTTP using the provided {@link NagerDateHttpClient}.
 * </p>
 *
 * @since 1.0
 */
public class HttpServicesFactory implements ServicesFactory {

    private final NagerDateHttpClient httpClient;

    /**
     * Constructs an HttpServicesFactory with the given HTTP client.
     *
     * @param httpClient the HTTP client to pass to service implementations, must not be null
     * @throws NullPointerException if httpClient is null
     */
    public HttpServicesFactory(NagerDateHttpClient httpClient) {
        Objects.requireNonNull(httpClient);
        this.httpClient = httpClient;
    }



    @Override
    public CountryV3Service createCountryV3Service() {
        return new HttpCountryV3Service(httpClient);
    }

    @Override
    public LongWeekendV3Service createLongWeekendV3Service() {
        return new HttpLongWeekendV3Service(httpClient);
    }

    @Override
    public PublicHolidayV3Service createPublicHolidayV3Service() {
        return new HttpPublicHolidayV3Service(httpClient);
    }
}
