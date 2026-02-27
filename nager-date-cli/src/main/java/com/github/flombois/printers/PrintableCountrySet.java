package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.CountryV3;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;
import static com.github.flombois.OutputFormat.TABLE;

/**
 * Printable wrapper for a set of {@link CountryV3} records.
 * <p>
 * Supports JSON and plain text output. Plain text delegates to
 * {@link PrintableCountryV3} for each country, joining them with newlines.
 * </p>
 *
 * @since 1.0
 */
public class PrintableCountrySet implements PrintableRecord<Set<CountryV3>> {

    private final Set<CountryV3> records;

    /**
     * Constructs a PrintableCountrySet wrapping the given set of countries.
     *
     * @param records the country set to wrap, must not be null
     * @throws NullPointerException if records is null
     */
    public PrintableCountrySet(Set<CountryV3> records) {
        Objects.requireNonNull(records);
        this.records = records;
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN, TABLE);
    }

    @Override
    public Set<CountryV3> record() {
        return records;
    }

    @Override
    public String toPlainString() {
        return String.join("\n", records.stream()
                .map(PrintableCountryV3::new)
                .map(PrintableCountryV3::toPlainString)
                .toList());
    }

    @Override
    public String toTableString() {
        String[] headers = {"ISO CODE", "NAME"};
        List<String[]> rows = records.stream()
                .map(c -> new String[]{
                        String.valueOf(c.getCountryCode()),
                        c.getName()
                })
                .toList();
        return TableFormatter.format(headers, rows);
    }
}
