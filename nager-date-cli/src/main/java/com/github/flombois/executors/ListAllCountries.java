package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.CountryV3Service;

import java.util.Objects;
import java.util.Set;

/**
 * Service executor that retrieves all available countries from the Nager.Date API.
 *
 * @since 1.0
 */
public class ListAllCountries implements ServiceExecutor<Set<CountryV3>> {

    private CountryV3Service countryV3Service;

    /**
     * Constructs a ListAllCountries executor with the given country service.
     *
     * @param countryV3Service the country service to delegate to
     */
    public ListAllCountries(CountryV3Service countryV3Service) {
        setCountryV3Service(countryV3Service);
    }

    @Override
    public Set<CountryV3> callService(Context context) throws NagerDateServiceException {
        return countryV3Service.getAllCountries();
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
