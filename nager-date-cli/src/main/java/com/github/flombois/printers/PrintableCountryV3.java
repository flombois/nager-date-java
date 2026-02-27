package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.CountryV3;

import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;

/**
 * Printable wrapper for a single {@link CountryV3} record.
 * <p>
 * Supports JSON and plain text output. Plain text formats as
 * {@code "Country: {name}, ISO Code: {code}"}.
 * </p>
 *
 * @since 1.0
 */
public class PrintableCountryV3 implements PrintableRecord<CountryV3> {

    private final CountryV3 record;

    /**
     * Constructs a PrintableCountryV3 wrapping the given country.
     *
     * @param record the country record to wrap, must not be null
     * @throws NullPointerException if record is null
     */
    public PrintableCountryV3(CountryV3 record) {
        Objects.requireNonNull(record);
        this.record = record;
    }

    @Override
    public String toPlainString() {
        return String.format("Country: %s, ISO Code: %s", record.getName(), record.getCountryCode());
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN);
    }

    @Override
    public CountryV3 record() {
        return record;
    }
}
