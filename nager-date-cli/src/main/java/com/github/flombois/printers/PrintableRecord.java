package com.github.flombois.printers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.flombois.OutputFormat;

import java.util.Set;

/**
 * Interface for wrapping a data record with output formatting capabilities.
 * <p>
 * Implementations declare which {@link OutputFormat}s they support and provide
 * conversion methods for each format (JSON, plain text, table).
 * </p>
 *
 * @param <T> the type of the underlying data record
 * @since 1.0
 */
public interface PrintableRecord<T> {

    /**
     * Returns the set of output formats supported by this printable record.
     *
     * @return the supported output formats
     */
    Set<OutputFormat> supportedOutputFormats();

    /**
     * Checks whether the given output format is supported.
     *
     * @param outputFormat the format to check
     * @return {@code true} if supported, {@code false} otherwise
     */
    default boolean isOutputFormatSupported(OutputFormat outputFormat) {
        return supportedOutputFormats().contains(outputFormat);
    }

    /** @return {@code true} if JSON output is supported */
    default boolean isJsonPrintable() {
        return isOutputFormatSupported(OutputFormat.JSON);
    }

    /** @return {@code true} if plain text output is supported */
    default boolean isPlainPrintable() {
        return isOutputFormatSupported(OutputFormat.PLAIN);
    }

    /** @return {@code true} if table output is supported */
    default boolean isTablePrintable() {
        return isOutputFormatSupported(OutputFormat.TABLE);
    }

    /**
     * Serializes the underlying record to a JSON string.
     *
     * @param objectMapper the Jackson ObjectMapper to use
     * @return the JSON string representation
     * @throws RuntimeException if serialization fails
     */
    default String toJsonString(ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(record());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the underlying record to a plain text string.
     *
     * @return the plain text representation
     * @throws UnsupportedOperationException if not implemented
     */
    default String toPlainString() {
        throw new UnsupportedOperationException();
    }

    /**
     * Converts the underlying record to a table-formatted string.
     *
     * @return the table representation
     * @throws UnsupportedOperationException if not implemented
     */
    default String toTableString() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the underlying data record.
     *
     * @return the wrapped record
     */
    T record();

}
