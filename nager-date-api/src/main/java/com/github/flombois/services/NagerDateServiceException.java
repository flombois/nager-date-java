package com.github.flombois.services;

/**
 * Exception thrown when an error occurs while communicating with the Nager.Date API.
 * <p>
 * This is a checked exception that should be caught and handled by code using the
 * Nager Date service APIs. It can wrap underlying causes such as network errors,
 * JSON parsing errors, or API errors.
 * </p>
 *
 * @since 1.0
 */
public class NagerDateServiceException extends Exception {

    /**
     * Constructs a NagerDateServiceException with the specified message.
     *
     * @param message a descriptive message about the error
     */
    public NagerDateServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a NagerDateServiceException with the specified cause.
     *
     * @param cause the underlying exception that caused this error
     */
    public NagerDateServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a NagerDateServiceException with both a message and a cause.
     *
     * @param message a descriptive message about the error
     * @param cause the underlying exception that caused this error
     */
    public NagerDateServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
