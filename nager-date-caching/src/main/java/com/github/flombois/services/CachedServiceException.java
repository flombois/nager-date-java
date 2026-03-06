package com.github.flombois.services;

/**
 * Exception thrown when a cached service call fails.
 * <p>
 * Wraps exceptions from the underlying service or cache operations
 * as a subtype of {@link NagerDateServiceException}.
 * </p>
 *
 * @since 1.0
 */
public class CachedServiceException extends NagerDateServiceException {

    /**
     * Creates a new exception with the given cause.
     *
     * @param cause the underlying cause
     */
    public CachedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new exception with the given message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public CachedServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
