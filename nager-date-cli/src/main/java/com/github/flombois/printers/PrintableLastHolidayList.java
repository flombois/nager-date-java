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
 * Printable wrapper for a list of the last celebrated public holidays.
 * <p>
 * Plain text formats each entry as {@code "{date} - {name} ({localName})"}.
 * </p>
 *
 * @since 1.0
 */
public class PrintableLastHolidayList implements PrintableRecord<List<PublicHolidayV3>> {

    private final List<PublicHolidayV3> records;

    /**
     * Constructs a PrintableLastHolidayList wrapping the given list.
     *
     * @param records the ordered list of holidays, must not be null
     * @throws NullPointerException if records is null
     */
    public PrintableLastHolidayList(List<PublicHolidayV3> records) {
        Objects.requireNonNull(records);
        this.records = records;
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN, TABLE);
    }

    @Override
    public List<PublicHolidayV3> record() {
        return records;
    }

    @Override
    public String toPlainString() {
        return String.join("\n", records.stream()
                .map(h -> "%s - %s (%s)".formatted(h.getDate(), h.getName(), h.getLocalName()))
                .toList());
    }

    @Override
    public String toTableString() {
        String[] headers = {"DATE", "NAME", "LOCAL NAME"};
        List<String[]> rows = records.stream()
                .map(h -> new String[]{
                        String.valueOf(h.getDate()),
                        h.getName(),
                        h.getLocalName()
                })
                .toList();
        return TableFormatter.format(headers, rows);
    }
}
