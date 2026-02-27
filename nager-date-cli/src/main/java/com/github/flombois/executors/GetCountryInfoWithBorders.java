package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.CountryInfo;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.CountryV3Service;

import java.util.Objects;

/**
 * Service executor that retrieves detailed country information including border countries.
 *
 * @since 1.0
 */
public class GetCountryInfoWithBorders implements ServiceExecutor<CountryInfoWithBorders> {

    private CountryV3Service countryV3Service;

    /**
     * Constructs a GetCountryInfoWithBorders executor with the given country service.
     *
     * @param countryV3Service the country service to delegate to
     */
    public GetCountryInfoWithBorders(CountryV3Service countryV3Service) {
        this.countryV3Service = countryV3Service;
    }

    @Override
    public CountryInfoWithBorders callService(Context context) throws NagerDateServiceException {
        return countryV3Service.getCountryInfoWithBorders(context.countryCode());
    }

    /**
     * Sets the country service to delegate to.
     *
     * @param countryV3Service the country service, must not be null
     * @throws NullPointerException if countryV3Service is null
     */
    public void setCountryV3Service(CountryV3Service countryV3Service) {
        Objects.requireNonNull(countryV3Service);
        this.countryV3Service = countryV3Service;
    }
}
