package com.github.flombois.http;

import com.github.flombois.ApiCallException;

import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * Strategy interface for handling HTTP responses from the Nager.Date API.
 * <p>
 * Implementations define how to process responses based on status code. The default
 * {@link #handle(HttpResponse)} method dispatches to {@link #success(HttpResponse)}
 * for 200, {@link #noContent(HttpResponse)} for 204, {@link #badRequest(HttpResponse)}
 * for 400, and {@link #notFound(HttpResponse)} for 404 responses, throwing an
 * {@link ApiCallException} for all other status codes.
 * </p>
 *
 * @param <T> the type of the deserialized response
 * @since 1.0
 */
public interface ResponseHandler<T> {

    /**
     * Handles an HTTP response by dispatching based on the status code.
     *
     * @param response the HTTP response to handle
     * @return the deserialized result
     * @throws ApiCallException if the response is null or has an unexpected status code
     */
    default T handle(HttpResponse<byte[]> response) throws ApiCallException {
        if(Objects.isNull(response)) throw new ApiCallException("Response is null");
        if(response.statusCode() == 200) return success(response);
        if(response.statusCode() == 204) return noContent(response);
        if(response.statusCode() == 400) return badRequest(response);
        if(response.statusCode() == 404) return notFound(response);
        throw new ApiCallException("Unexpected response status code: " + response.statusCode(), response);
    }

    /**
     * Processes a successful (HTTP 200) response.
     *
     * @param response the successful HTTP response
     * @return the deserialized result
     */
    T success(HttpResponse<byte[]> response) throws ApiCallException;

    /**
     * Handles a no-content (HTTP 204) response.
     * <p>
     * By default, throws an {@link ApiCallException} wrapping the response.
     * </p>
     *
     * @param response the 204 HTTP response
     * @return the result, if the implementation chooses to handle 204 gracefully
     * @throws ApiCallException by default
     */
    default T noContent(HttpResponse<byte[]> response) throws ApiCallException {
        throw new ApiCallException(response);
    }

    /**
     * Handles a bad-request (HTTP 400) response.
     * <p>
     * By default, throws an {@link ApiCallException} wrapping the response.
     * </p>
     *
     * @param response the 400 HTTP response
     * @return the result, if the implementation chooses to handle 400 gracefully
     * @throws ApiCallException by default
     */
    default T badRequest(HttpResponse<byte[]> response) throws ApiCallException {
        throw new ApiCallException(response);
    }

    /**
     * Handles a not-found (HTTP 404) response.
     * <p>
     * By default, throws an {@link ApiCallException} wrapping the response.
     * </p>
     *
     * @param response the 404 HTTP response
     * @return the result, if the implementation chooses to handle 404 gracefully
     * @throws ApiCallException by default
     */
    default T notFound(HttpResponse<byte[]> response) throws ApiCallException {
        throw new ApiCallException(response);
    }
}
