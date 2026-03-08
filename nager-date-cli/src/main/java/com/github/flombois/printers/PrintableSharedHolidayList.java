package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.SharedHoliday;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;
import static com.github.flombois.OutputFormat.TABLE;

/**
 * Printable wrapper for a list of {@link SharedHoliday} records.
 * <p>
 * Plain text formats each entry as {@code "{date} - {localName1} / {localName2}"}.
 * </p>
 *
 * @since 1.0
 */
public class PrintableSharedHolidayList implements PrintableRecord<List<SharedHoliday>> {

    private final List<SharedHoliday> records;

    /**
     * Constructs a PrintableSharedHolidayList wrapping the given list.
     *
     * @param records the ordered list of shared holidays, must not be null
     * @throws NullPointerException if records is null
     */
    public PrintableSharedHolidayList(List<SharedHoliday> records) {
        Objects.requireNonNull(records);
        this.records = records;
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN, TABLE);
    }

    @Override
    public List<SharedHoliday> record() {
        return records;
    }

    @Override
    public String toPlainString() {
        return String.join("\n", records.stream()
                .map(r -> "%s - %s / %s".formatted(r.date(), r.localName1(), r.localName2()))
                .toList());
    }

    @Override
    public String toTableString() {
        String[] headers = {"DATE", "LOCAL NAME (1)", "LOCAL NAME (2)"};
        List<String[]> rows = records.stream()
                .map(r -> new String[]{
                        String.valueOf(r.date()),
                        r.localName1(),
                        r.localName2()
                })
                .toList();
        return TableFormatter.format(headers, rows);
    }
}
