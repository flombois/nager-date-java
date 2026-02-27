package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.LongWeekendV3;

import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;

/**
 * Printable wrapper for a single {@link LongWeekendV3} record.
 * <p>
 * Supports JSON and plain text output. Plain text formats as
 * {@code "Long weekend: {startDate} - {endDate} ({dayCount} days, bridge day: {yes/no})"}.
 * </p>
 *
 * @since 1.0
 */
public class PrintableLongWeekendV3 implements PrintableRecord<LongWeekendV3> {

    private final LongWeekendV3 record;

    /**
     * Constructs a PrintableLongWeekendV3 wrapping the given long weekend.
     *
     * @param record the long weekend record to wrap, must not be null
     * @throws NullPointerException if record is null
     */
    public PrintableLongWeekendV3(LongWeekendV3 record) {
        Objects.requireNonNull(record);
        this.record = record;
    }

    @Override
    public String toPlainString() {
        return String.format("Long weekend: %s - %s (%d days, bridge day: %s)",
                record.getStartDate(), record.getEndDate(), record.getDayCount(),
                record.isNeedBridgeDay() ? "yes" : "no");
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN);
    }

    @Override
    public LongWeekendV3 record() {
        return record;
    }
}
