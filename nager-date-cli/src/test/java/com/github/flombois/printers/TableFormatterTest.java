package com.github.flombois.printers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableFormatterTest {

    @Test
    void formatWithHeadersAndRows() {
        String[] headers = {"NAME", "AGE"};
        List<String[]> rows = List.of(
                new String[]{"Alice", "30"},
                new String[]{"Bob", "25"}
        );

        String result = TableFormatter.format(headers, rows);

        String[] lines = result.split("\n");
        assertEquals(4, lines.length);
        assertTrue(lines[0].startsWith("NAME"));
        assertTrue(lines[0].contains("AGE"));
        assertTrue(lines[1].startsWith("----"));
        assertTrue(lines[2].startsWith("Alice"));
        assertTrue(lines[3].startsWith("Bob"));
    }

    @Test
    void formatSeparatorMatchesHeaderLength() {
        String[] headers = {"ISO CODE", "NAME"};
        List<String[]> rows = List.of();

        String result = TableFormatter.format(headers, rows);
        String[] lines = result.split("\n");

        assertEquals(2, lines.length);
        // "ISO CODE" is 8 chars, so separator starts with 8 dashes
        assertTrue(lines[1].startsWith("--------"));
        assertFalse(lines[1].startsWith("---------"));
        // "NAME" is 4 chars, separator should end with 4 dashes (after padding)
        assertTrue(lines[1].stripTrailing().endsWith("----"));
    }

    @Test
    void formatWithEmptyRows() {
        String[] headers = {"COL1", "COL2"};
        List<String[]> rows = List.of();

        String result = TableFormatter.format(headers, rows);

        String[] lines = result.split("\n");
        assertEquals(2, lines.length);
        assertTrue(lines[0].startsWith("COL1"));
        assertTrue(lines[1].startsWith("----"));
    }

    @Test
    void formatHandlesNullCells() {
        String[] headers = {"A", "B"};
        var rows = new ArrayList<String[]>();
        rows.add(new String[]{null, "value"});

        String result = TableFormatter.format(headers, rows);

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[1].startsWith("-"));
        assertTrue(lines[2].contains("value"));
    }

    @Test
    void formatColumnsAreFixedWidth() {
        String[] headers = {"X", "Y"};
        var rows = new ArrayList<String[]>();
        rows.add(new String[]{"short", "data"});

        String result = TableFormatter.format(headers, rows);
        String[] lines = result.split("\n");

        // The header "X" should be padded to 25 chars before "Y"
        int yIndex = lines[0].indexOf("Y");
        assertEquals(25, yIndex);
    }
}
