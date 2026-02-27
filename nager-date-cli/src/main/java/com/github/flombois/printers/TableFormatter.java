package com.github.flombois.printers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility for formatting tabular data with fixed-width columns.
 *
 * @since 1.0
 */
public class TableFormatter {

    private static final int COLUMN_WIDTH = 25;

    private TableFormatter() {}

    /**
     * Formats headers and rows into a fixed-width column table string.
     *
     * @param headers the column headers
     * @param rows    the data rows
     * @return the formatted table string
     */
    public static String format(String[] headers, List<String[]> rows) {
        String separator = Arrays.stream(headers)
                .map(h -> "-".repeat(h.length()))
                .map(s -> String.format("%-" + COLUMN_WIDTH + "s", s))
                .collect(Collectors.joining())
                .stripTrailing();

        return Stream.of(
                        Stream.of(formatRow(headers)),
                        Stream.of(separator),
                        rows.stream().map(TableFormatter::formatRow))
                .flatMap(s -> s)
                .collect(Collectors.joining("\n"));
    }

    private static String formatRow(String[] cells) {
        return Arrays.stream(cells)
                .map(cell -> String.format("%-" + COLUMN_WIDTH + "s", cell != null ? cell : ""))
                .collect(Collectors.joining())
                .stripTrailing();
    }
}
