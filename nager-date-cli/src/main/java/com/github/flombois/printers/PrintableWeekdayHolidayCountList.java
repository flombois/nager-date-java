package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.WeekdayHolidayCount;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;
import static com.github.flombois.OutputFormat.TABLE;

/**
 * Printable wrapper for a list of {@link WeekdayHolidayCount} records.
 * <p>
 * Plain text formats each entry as {@code "{countryCode} - {count} weekday holidays"}.
 * </p>
 *
 * @since 1.0
 */
public class PrintableWeekdayHolidayCountList implements PrintableRecord<List<WeekdayHolidayCount>> {

    private final List<WeekdayHolidayCount> records;

    /**
     * Constructs a PrintableWeekdayHolidayCountList wrapping the given list.
     *
     * @param records the ordered list of weekday holiday counts, must not be null
     * @throws NullPointerException if records is null
     */
    public PrintableWeekdayHolidayCountList(List<WeekdayHolidayCount> records) {
        Objects.requireNonNull(records);
        this.records = records;
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN, TABLE);
    }

    @Override
    public List<WeekdayHolidayCount> record() {
        return records;
    }

    @Override
    public String toPlainString() {
        return String.join("\n", records.stream()
                .map(r -> "%s - %d weekday holidays".formatted(r.countryCode(), r.count()))
                .toList());
    }

    @Override
    public String toTableString() {
        String[] headers = {"COUNTRY", "WEEKDAY HOLIDAYS"};
        List<String[]> rows = records.stream()
                .map(r -> new String[]{
                        String.valueOf(r.countryCode()),
                        String.valueOf(r.count())
                })
                .toList();
        return TableFormatter.format(headers, rows);
    }
}
