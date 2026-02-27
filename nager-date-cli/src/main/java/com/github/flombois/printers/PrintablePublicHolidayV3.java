package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.PublicHolidayV3;

import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;

/**
 * Printable wrapper for a single {@link PublicHolidayV3} record.
 * <p>
 * Supports JSON and plain text output. Plain text formats as
 * {@code "{date} - {name} ({localName}), fixed: {yes/no}, global: {yes/no}"}.
 * </p>
 *
 * @since 1.0
 */
public class PrintablePublicHolidayV3 implements PrintableRecord<PublicHolidayV3> {

    private final PublicHolidayV3 record;

    /**
     * Constructs a PrintablePublicHolidayV3 wrapping the given public holiday.
     *
     * @param record the public holiday record to wrap, must not be null
     * @throws NullPointerException if record is null
     */
    public PrintablePublicHolidayV3(PublicHolidayV3 record) {
        Objects.requireNonNull(record);
        this.record = record;
    }

    @Override
    public String toPlainString() {
        return String.format("%s - %s (%s), fixed: %s, global: %s",
                record.getDate(), record.getName(), record.getLocalName(),
                record.isFixed() ? "yes" : "no",
                record.isGlobal() ? "yes" : "no");
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN);
    }

    @Override
    public PublicHolidayV3 record() {
        return record;
    }
}
