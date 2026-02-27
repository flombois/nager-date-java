package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.LongWeekendV3;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;
import static com.github.flombois.OutputFormat.TABLE;

/**
 * Printable wrapper for a set of {@link LongWeekendV3} records.
 * <p>
 * Supports JSON and plain text output. Plain text delegates to
 * {@link PrintableLongWeekendV3} for each long weekend, joining them with newlines.
 * </p>
 *
 * @since 1.0
 */
public class PrintableLongWeekendSet implements PrintableRecord<Set<LongWeekendV3>> {

    private final Set<LongWeekendV3> records;

    /**
     * Constructs a PrintableLongWeekendSet wrapping the given set of long weekends.
     *
     * @param records the long weekend set to wrap, must not be null
     * @throws NullPointerException if records is null
     */
    public PrintableLongWeekendSet(Set<LongWeekendV3> records) {
        Objects.requireNonNull(records);
        this.records = records;
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN, TABLE);
    }

    @Override
    public Set<LongWeekendV3> record() {
        return records;
    }

    @Override
    public String toPlainString() {
        return String.join("\n", records.stream()
                .map(PrintableLongWeekendV3::new)
                .map(PrintableLongWeekendV3::toPlainString)
                .toList());
    }

    @Override
    public String toTableString() {
        String[] headers = {"START DATE", "END DATE", "DAYS", "BRIDGE DAY"};
        List<String[]> rows = records.stream()
                .map(lw -> new String[]{
                        String.valueOf(lw.getStartDate()),
                        String.valueOf(lw.getEndDate()),
                        String.valueOf(lw.getDayCount()),
                        lw.isNeedBridgeDay() ? "yes" : "no"
                })
                .toList();
        return TableFormatter.format(headers, rows);
    }
}
