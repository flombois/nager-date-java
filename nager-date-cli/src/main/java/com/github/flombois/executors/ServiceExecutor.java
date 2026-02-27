package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.services.NagerDateServiceException;

/**
 * Functional interface for executing a Nager.Date API service call.
 * <p>
 * Each implementation wraps a specific service method and extracts the
 * necessary parameters from the provided {@link Context}.
 * </p>
 *
 * @param <T> the type of the service result
 * @since 1.0
 */
public interface ServiceExecutor<T> {

    /**
     * Invokes the underlying service using parameters from the given context.
     *
     * @param context the execution context containing request parameters
     * @return the service result
     * @throws NagerDateServiceException if the API call fails
     */
    T callService(Context context) throws NagerDateServiceException;
}
