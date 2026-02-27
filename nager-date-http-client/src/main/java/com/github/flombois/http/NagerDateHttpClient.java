package com.github.flombois.http;

import com.github.flombois.ApiCallException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP client for the Nager.Date REST API.
 * <p>
 * This client wraps the standard {@link HttpClient} and provides methods for
 * building and executing GET requests against the Nager.Date API. It handles
 * request construction, timeouts, and error wrapping.
 * </p>
 *
 * @since 1.0
 */
public class NagerDateHttpClient {

    /** The public base URL of the Nager.Date service. */
    public static final String PUBLIC_BASE_URL = "https://date.nager.at";

    /** The path prefix for API v3 endpoints. */
    public static final String V3_PATH = "/api/v3";

    /** The full public URL for the Nager.Date API v3 endpoint. */
    public static final String PUBLIC_V3_ENDPOINT = PUBLIC_BASE_URL + V3_PATH;

    private final String baseUrl;
    private final String key; // TODO check how key is used to authenticate
    private final HttpClient httpClient;

    /**
     * Constructs a client targeting the public Nager.Date API v3 endpoint.
     */
    public NagerDateHttpClient() {
        this(PUBLIC_V3_ENDPOINT);
    }

    /**
     * Constructs a client targeting a custom base URL.
     *
     * @param baseUrl the base URL for API requests (e.g., for a self-hosted instance)
     */
    public NagerDateHttpClient(String baseUrl) {
        this(baseUrl, null);
    }


    public NagerDateHttpClient(String baseUrl, String key) {
        this.baseUrl = baseUrl;
        this.key = key;
        this.httpClient = HttpClient.newBuilder().build();
    }

    /**
     * Returns the configured base URL.
     *
     * @return the base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns the underlying {@link HttpClient} instance.
     *
     * @return the HTTP client
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Creates a new HTTP GET request for the specified path with a 30-second timeout.
     *
     * @param path the API path to append to the base URL
     * @return the constructed {@link HttpRequest}
     */
    public HttpRequest newHttpGetRequest(String path) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .build();
    }

    /**
     * Executes an API call and processes the response using the provided handler.
     *
     * @param <T> the expected return type
     * @param path the API path to call
     * @param responseHandler the handler used to process the HTTP response
     * @return the deserialized response
     * @throws ApiCallException if a network error occurs or the response handler throws
     */
    public <T> T callApi(String path, ResponseHandler<T> responseHandler) throws ApiCallException {
        try {
            final var response = httpClient.send(newHttpGetRequest(path), HttpResponse.BodyHandlers.ofByteArray());
            return responseHandler.handle(response);
        } catch (IOException | InterruptedException e) {
            throw new ApiCallException(e);
        }
    }

}
