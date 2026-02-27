package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.PublicHolidayV3;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;
import static com.github.flombois.OutputFormat.TABLE;

/**
 * Printable wrapper for a set of {@link PublicHolidayV3} records.
 * <p>
 * Supports JSON and plain text output. Plain text delegates to
 * {@link PrintablePublicHolidayV3} for each public holiday, joining them with newlines.
 * </p>
 *
 * @since 1.0
 */
public class PrintablePublicHolidaySet implements PrintableRecord<Set<PublicHolidayV3>> {

    private final Set<PublicHolidayV3> records;

    /**
     * Constructs a PrintablePublicHolidaySet wrapping the given set of public holidays.
     *
     * @param records the public holiday set to wrap, must not be null
     * @throws NullPointerException if records is null
     */
    public PrintablePublicHolidaySet(Set<PublicHolidayV3> records) {
        Objects.requireNonNull(records);
        this.records = records;
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN, TABLE);
    }

    @Override
    public Set<PublicHolidayV3> record() {
        return records;
    }

    @Override
    public String toPlainString() {
        return String.join("\n", records.stream()
                .map(PrintablePublicHolidayV3::new)
                .map(PrintablePublicHolidayV3::toPlainString)
                .toList());
    }

    @Override
    public String toTableString() {
        String[] headers = {"DATE", "NAME", "LOCAL NAME", "FIXED", "GLOBAL"};
        List<String[]> rows = records.stream()
                .map(h -> new String[]{
                        String.valueOf(h.getDate()),
                        h.getName(),
                        h.getLocalName(),
                        h.isFixed() ? "yes" : "no",
                        h.isGlobal() ? "yes" : "no"
                })
                .toList();
        return TableFormatter.format(headers, rows);
    }
}
