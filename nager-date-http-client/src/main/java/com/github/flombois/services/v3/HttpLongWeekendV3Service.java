package com.github.flombois.services.v3;

import com.github.flombois.ApiCallException;
import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.mappers.JsonMapper;
import com.github.flombois.mappers.MappingException;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;

import java.time.Year;
import java.util.Objects;
import java.util.Set;

/**
 * HTTP implementation of {@link LongWeekendV3Service} using the Nager.Date API v3.
 * <p>
 * This service communicates with the {@code /LongWeekend/{year}/{countryCode}} endpoint
 * to retrieve long weekend information, supporting optional query parameters for
 * bridge days and subdivision codes.
 * </p>
 *
 * @since 1.0
 */
public class HttpLongWeekendV3Service implements LongWeekendV3Service {

    /** API endpoint path for retrieving long weekend information. */
    public static final String LONG_WEEKEND_ENDPOINT = "/LongWeekend";

    private final NagerDateHttpClient client;

    /**
     * Constructs an HttpLongWeekendV3Service with the specified HTTP client.
     *
     * @param client the HTTP client to use for API calls
     */
    public HttpLongWeekendV3Service(NagerDateHttpClient client) {
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public Set<LongWeekendV3> getLongWeekend(CountryCode countryCode, Year year, int availableBridgeDays, String subdivisionCode)
            throws NagerDateServiceException {
        try {
            return client.callApi(getUri(countryCode, year, availableBridgeDays, subdivisionCode), response -> getLongWeekendV3Set(response.body()));
        } catch (ApiCallException e) {
            throw new NagerDateServiceException("Failed to retrieve long weekend data", e);
        }
    }

    /**
     * Deserializes a JSON byte array into a set of {@link LongWeekendV3} objects.
     *
     * @param body the JSON byte array
     * @return the deserialized set of long weekend data
     * @throws MappingException if deserialization fails
     */
    protected Set<LongWeekendV3> getLongWeekendV3Set(byte[] body) throws MappingException {
        return JsonMapper.getInstance().mapToSet(body, LongWeekendV3.class);
    }

    /**
     * Builds the request URI for the long weekend endpoint.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year to query
     * @param availableBridgeDays the number of available bridge days
     * @param subdivisionCode the subdivision code, or empty string for the whole country
     * @return the constructed URI path with query parameters
     */
    protected String getUri(CountryCode countryCode, Year year, int availableBridgeDays, String subdivisionCode) {
        return LONG_WEEKEND_ENDPOINT + "/" + year.getValue() + "/" + countryCode.name() +
                toQueryParam(availableBridgeDays, subdivisionCode);
    }

    /**
     * Builds the query parameter string based on the provided values.
     * <p>
     * Returns an empty string if no parameters are applicable, or a query string
     * starting with {@code ?} containing the relevant parameters.
     * </p>
     *
     * @param availableBridgeDays the number of available bridge days (0 or less means no parameter)
     * @param subdivisionCode the subdivision code (null or blank means no parameter)
     * @return the query parameter string
     */
    protected String toQueryParam(final int availableBridgeDays, final String subdivisionCode) {
        final boolean bridgeDaysParam = availableBridgeDays > 0;
        final boolean subdivisionParam = Objects.nonNull(subdivisionCode) && !subdivisionCode.isBlank();

        final boolean noParams = !bridgeDaysParam && !subdivisionParam;
        final boolean onlyBridgeDays = bridgeDaysParam && !subdivisionParam;
        final boolean onlySubdivision = !bridgeDaysParam && subdivisionParam;

        if(noParams) return "";
        if(onlySubdivision) return String.format("?subdivisionCode=%s", subdivisionCode);
        if(onlyBridgeDays) return String.format("?availableBridgeDays=%s", availableBridgeDays);

        // Then both
        return String.format("?availableBridgeDays=%s&subdivisionCode=%s", availableBridgeDays, subdivisionCode);
    }
}
