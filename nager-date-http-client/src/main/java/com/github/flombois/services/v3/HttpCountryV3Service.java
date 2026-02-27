package com.github.flombois.services.v3;

import com.github.flombois.ApiCallException;
import com.github.flombois.mappers.JsonMapper;
import com.github.flombois.models.CountryInfo;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.http.NagerDateHttpClient;

import com.neovisionaries.i18n.CountryCode;

import java.util.Set;

/**
 * HTTP implementation of {@link CountryV3Service} using the Nager.Date API v3.
 * <p>
 * This service communicates with the {@code /AvailableCountries} and {@code /CountryInfo}
 * endpoints to retrieve country information.
 * </p>
 *
 * @since 1.0
 */
public class HttpCountryV3Service implements CountryV3Service {

    /** API endpoint path for listing all available countries. */
    public static final String AVAILABLE_COUNTRIES_ENDPOINT = "/AvailableCountries";

    /** API endpoint path for retrieving country information. */
    public static final String COUNTRY_INFO_ENDPOINT = "/CountryInfo";

    private final NagerDateHttpClient client;

    /**
     * Constructs an HttpCountryV3Service with the specified HTTP client.
     *
     * @param client the HTTP client to use for API calls
     */
    public HttpCountryV3Service(NagerDateHttpClient client) {
        this.client = client;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation delegates to {@link #getCountryInfoWithBorders(CountryCode)}.
     * </p>
     */
    @Override
    public CountryInfo getCountryInfo(CountryCode countryCode) throws NagerDateServiceException {
        return getCountryInfoWithBorders(countryCode);
    }

    /** {@inheritDoc} */
    @Override
    public CountryInfoWithBorders getCountryInfoWithBorders(CountryCode countryCode) throws NagerDateServiceException {
        try {
            return client.callApi(COUNTRY_INFO_ENDPOINT + "/" + countryCode.name(), response -> getCountryInfoWithBorders(response.body()));
        } catch (ApiCallException e) {
            throw new NagerDateServiceException("Failed to retrieve country info with borders", e);
        }
    }

    /**
     * Deserializes a JSON byte array into a {@link CountryInfoWithBorders} object.
     *
     * @param data the JSON byte array
     * @return the deserialized country info with borders
     */
    protected CountryInfoWithBorders getCountryInfoWithBorders(byte[] data) {
        return JsonMapper.getInstance().map(data, CountryInfoWithBorders.class);
    }


    /** {@inheritDoc} */
    @Override
    public Set<CountryV3> getAllCountries() throws NagerDateServiceException {
        try {
            return client.callApi(AVAILABLE_COUNTRIES_ENDPOINT, response -> getCountries(response.body()));
        } catch (ApiCallException e) {
            throw new NagerDateServiceException("Failed to retrieve all countries", e);
        }
    }

    /**
     * Deserializes a JSON byte array into a set of {@link CountryV3} objects.
     *
     * @param data the JSON byte array
     * @return the deserialized set of countries
     */
    protected Set<CountryV3> getCountries(byte[] data) {
        return JsonMapper.getInstance().mapToSet(data, CountryV3.class);
    }

}
