package com.github.flombois.mappers;

/**
 * Runtime exception thrown when JSON deserialization fails.
 * <p>
 * This exception wraps the underlying I/O or parsing errors that occur
 * during JSON mapping operations in {@link JsonMapper}.
 * </p>
 *
 * @since 1.0
 * @see JsonMapper
 */
public class MappingException extends RuntimeException {

    /**
     * Constructs a MappingException with the specified message.
     *
     * @param message a descriptive message about the mapping failure
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * Constructs a MappingException with the specified cause.
     *
     * @param cause the underlying exception that caused the mapping failure
     */
    public MappingException(Throwable cause) {
        super(cause);
    }
}
