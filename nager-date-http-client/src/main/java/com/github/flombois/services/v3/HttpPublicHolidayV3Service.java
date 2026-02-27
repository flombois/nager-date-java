package com.github.flombois.services.v3;

import com.github.flombois.ApiCallException;
import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.http.ResponseHandler;
import com.github.flombois.mappers.JsonMapper;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;

import java.net.http.HttpResponse;
import java.time.Year;
import java.util.Objects;
import java.util.Set;

/**
 * HTTP implementation of {@link PublicHolidayV3Service} using the Nager.Date API v3.
 * <p>
 * This service communicates with the public holiday endpoints to retrieve
 * holiday information for specific countries and years.
 * </p>
 *
 * @since 1.0
 */
public class HttpPublicHolidayV3Service implements PublicHolidayV3Service {

    /** API endpoint path for retrieving public holidays. */
    public static final String PUBLIC_HOLIDAY_ENDPOINT = "/PublicHolidays";

    /** API endpoint path for checking if today is a public holiday. */
    public static final String IS_TODAY_PUBLIC_HOLIDAY_ENDPOINT = "/IsTodayPublicHoliday";

    private final NagerDateHttpClient client;

    /**
     * Constructs an HttpPublicHolidayV3Service with the specified HTTP client.
     *
     * @param client the HTTP client to use for API calls
     */
    public HttpPublicHolidayV3Service(NagerDateHttpClient client) {
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public Set<PublicHolidayV3> getPublicHolidays(CountryCode countryCode, Year year) throws NagerDateServiceException {
        try {
            return client.callApi(getPublicHolidayUri(countryCode, year), response -> getPublicHolidayV3(response.body()));
        } catch (ApiCallException e) {
            throw new NagerDateServiceException("Failed to retrieve public holidays", e);
        }
    }

    /**
     * Builds the request URI for the public holiday endpoint.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param year the year to query
     * @return the constructed URI path
     */
    protected String getPublicHolidayUri(CountryCode countryCode, Year year) {
        return PUBLIC_HOLIDAY_ENDPOINT + "/" + year + "/" + countryCode.name();
    }

    /**
     * Deserializes a JSON byte array into a set of {@link PublicHolidayV3} objects.
     *
     * @param data the JSON byte array
     * @return the deserialized set of public holidays
     */
    protected Set<PublicHolidayV3> getPublicHolidayV3(byte[] data) {
        return JsonMapper.getInstance().mapToSet(data, PublicHolidayV3.class);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTodayAPublicHoliday(CountryCode countryCode, String countyCode, byte offset) throws NagerDateServiceException {
        try {
            return client.callApi(getIsTodayPublicHolidayUri(countryCode, countyCode, offset), new ResponseHandler<>() {
                @Override
                public Boolean success(HttpResponse<byte[]> response) {
                    return true;
                }

                @Override
                public Boolean noContent(HttpResponse<byte[]> response) {
                    return false;
                }
            });
        } catch (ApiCallException e) {
            throw new NagerDateServiceException("Failed to check if today is a public holiday", e);
        }

    }

    /**
     * Builds the request URI for the "is today a public holiday" endpoint.
     *
     * @param countryCode the ISO 3166-1 alpha-2 country code
     * @param countyCode the subdivision code, or null/blank for the whole country
     * @param offset the timezone offset from UTC in hours
     * @return the constructed URI path with query parameters
     */
    protected String getIsTodayPublicHolidayUri(CountryCode countryCode, String countyCode, byte offset) {
        return IS_TODAY_PUBLIC_HOLIDAY_ENDPOINT + "/" + countryCode.name() + toQueryParam(countyCode, offset);
    }

    /**
     * Builds the query parameter string based on the provided values.
     * <p>
     * Returns an empty string if no parameters are applicable, or a query string
     * starting with {@code ?} containing the relevant parameters.
     * </p>
     *
     * @param countyCode the subdivision code (null or blank means no parameter)
     * @param offset the timezone offset from UTC in hours (outside -12 to +12 means no parameter)
     * @return the query parameter string
     */
    protected String toQueryParam(final String countyCode, final int offset) {
        final boolean countyCodeParam = Objects.nonNull(countyCode) && !countyCode.isBlank();
        final boolean offsetParam = offset >= -12 && offset <= 12;


        final boolean noParams = !countyCodeParam && !offsetParam;
        final boolean onlyCountyCode = countyCodeParam && !offsetParam;
        final boolean onlyOffset = !countyCodeParam && offsetParam;

        if(noParams) return "";
        if(onlyCountyCode) return String.format("?countyCode=%s", countyCode);
        if(onlyOffset) return String.format("?offset=%s", offset);

        // Then both
        return String.format("?countyCode=%s&offset=%s", countyCode, offset);
    }
}
