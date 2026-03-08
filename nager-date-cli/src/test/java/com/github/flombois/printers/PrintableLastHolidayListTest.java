package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.PublicHolidayV3;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableLastHolidayListTest {

    @Test
    void toPlainStringFormatsDateNameLocalName() {
        var h1 = holiday(LocalDate.of(2026, 3, 1), "Holiday One", "Fête Un");
        var h2 = holiday(LocalDate.of(2026, 1, 1), "New Year", "Jour de l'An");

        var printable = new PrintableLastHolidayList(List.of(h1, h2));
        String result = printable.toPlainString();

        assertTrue(result.contains("2026-03-01 - Holiday One (Fête Un)"));
        assertTrue(result.contains("2026-01-01 - New Year (Jour de l'An)"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void toTableStringFormatsAsTable() {
        var h1 = holiday(LocalDate.of(2026, 1, 1), "New Year", "Jour de l'An");

        var printable = new PrintableLastHolidayList(List.of(h1));
        String result = printable.toTableString();

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("DATE"));
        assertTrue(lines[0].contains("NAME"));
        assertTrue(lines[0].contains("LOCAL NAME"));
        assertTrue(lines[1].startsWith("----"));
        assertTrue(lines[2].contains("2026-01-01"));
        assertTrue(lines[2].contains("New Year"));
    }

    @Test
    void supportedOutputFormatsReturnsJsonPlainAndTable() {
        var printable = new PrintableLastHolidayList(List.of());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN, OutputFormat.TABLE), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsOriginalList() {
        List<PublicHolidayV3> holidays = List.of();
        var printable = new PrintableLastHolidayList(holidays);
        assertSame(holidays, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableLastHolidayList(null));
    }

    private PublicHolidayV3 holiday(LocalDate date, String name, String localName) {
        var h = new PublicHolidayV3();
        h.setDate(date);
        h.setName(name);
        h.setLocalName(localName);
        return h;
    }
}
