package com.github.flombois.services;

/**
 * Base marker interface for all Nager.Date service implementations.
 * <p>
 * Provides a default {@link #name()} method that returns the fully qualified
 * class name of the implementing service, useful for logging and diagnostics.
 * </p>
 *
 * @since 1.0
 */
public interface NagerDateService {

    /**
     * Returns the name of this service, defaulting to the implementation class name.
     *
     * @return the service name
     */
    default String name() {
        return this.getClass().getName();
    }
}
