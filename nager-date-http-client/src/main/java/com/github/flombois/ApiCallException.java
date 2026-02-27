package com.github.flombois;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.flombois.mappers.JsonMapper;
import com.github.flombois.models.ProblemDetails;

import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * Checked exception thrown when an HTTP API call to the Nager.Date service fails.
 * <p>
 * This exception can optionally carry the raw {@link HttpResponse} that caused the failure,
 * allowing callers to inspect the status code and parse structured error details using
 * {@link #getProblemDetails()}.
 * </p>
 *
 * @since 1.0
 */
public class ApiCallException extends Exception {

    private final HttpResponse<byte[]> response;

    /**
     * Constructs an ApiCallException with the specified message and no response.
     *
     * @param message a descriptive message about the failure
     */
    public ApiCallException(String message) {
        super(message);
        this.response = null;
    }

    /**
     * Constructs an ApiCallException wrapping the specified cause and no response.
     *
     * @param cause the underlying exception (e.g., {@link java.io.IOException})
     */
    public ApiCallException(Throwable cause) {
        super(cause);
        this.response = null;
    }

    /**
     * Constructs an ApiCallException with a message and the HTTP response that caused the failure.
     *
     * @param message a descriptive message about the failure
     * @param response the HTTP response associated with this error
     */
    public ApiCallException(String message, HttpResponse<byte[]> response) {
        super(message);
        this.response = response;
    }

    /**
     * Constructs an ApiCallException with the HTTP response that caused the failure.
     *
     * @param response the HTTP response associated with this error
     */
    public ApiCallException(HttpResponse<byte[]> response) {
        this.response = response;
    }

    /**
     * Returns the HTTP response associated with this exception, if available.
     *
     * @return the HTTP response, or {@code null} if this exception was not caused by an HTTP response
     */
    public HttpResponse<?> getResponse() {
        return response;
    }

    /**
     * Attempts to parse the response body as an RFC 7807 {@link ProblemDetails} object.
     *
     * @return the parsed problem details, or {@code null} if no response body is available
     * @throws com.github.flombois.mappers.MappingException if the response body cannot be parsed
     */
    public ProblemDetails getProblemDetails() {
       if(Objects.nonNull(response) && Objects.nonNull(response.body())) {
           return JsonMapper.getInstance().map(response.body(), ProblemDetailsResponse.class);
       }
        return null;
    }

    /**
     * Internal subclass of {@link ProblemDetails} with Jackson annotations for lenient deserialization.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class ProblemDetailsResponse extends ProblemDetails {

    }
}
