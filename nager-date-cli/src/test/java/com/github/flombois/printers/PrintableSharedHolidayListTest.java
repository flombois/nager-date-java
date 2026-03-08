package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.SharedHoliday;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableSharedHolidayListTest {

    @Test
    void toPlainStringFormatsDateAndLocalNames() {
        var records = List.of(
                new SharedHoliday(LocalDate.of(2026, 1, 1), "Jour de l'An", "Neujahr"),
                new SharedHoliday(LocalDate.of(2026, 5, 1), "Fête du Travail", "Tag der Arbeit")
        );
        var printable = new PrintableSharedHolidayList(records);
        String result = printable.toPlainString();

        assertTrue(result.contains("2026-01-01 - Jour de l'An / Neujahr"));
        assertTrue(result.contains("2026-05-01 - Fête du Travail / Tag der Arbeit"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void toTableStringFormatsAsTable() {
        var records = List.of(
                new SharedHoliday(LocalDate.of(2026, 1, 1), "Jour de l'An", "Neujahr")
        );
        var printable = new PrintableSharedHolidayList(records);
        String result = printable.toTableString();

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("DATE"));
        assertTrue(lines[0].contains("LOCAL NAME (1)"));
        assertTrue(lines[0].contains("LOCAL NAME (2)"));
        assertTrue(lines[1].startsWith("----"));
        assertTrue(lines[2].contains("2026-01-01"));
        assertTrue(lines[2].contains("Jour de l'An"));
        assertTrue(lines[2].contains("Neujahr"));
    }

    @Test
    void supportedOutputFormatsReturnsJsonPlainAndTable() {
        var printable = new PrintableSharedHolidayList(List.of());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN, OutputFormat.TABLE), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsOriginalList() {
        List<SharedHoliday> records = List.of();
        var printable = new PrintableSharedHolidayList(records);
        assertSame(records, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableSharedHolidayList(null));
    }
}
