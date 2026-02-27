package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.PublicHolidayV3;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintablePublicHolidaySetTest {

    @Test
    void toPlainStringJoinsWithNewlines() {
        var h1 = new PublicHolidayV3();
        h1.setDate(LocalDate.of(2026, 1, 1));
        h1.setName("New Year's Day");
        h1.setLocalName("Jour de l'an");
        h1.setFixed(true);
        h1.setGlobal(true);

        var h2 = new PublicHolidayV3();
        h2.setDate(LocalDate.of(2026, 7, 14));
        h2.setName("Bastille Day");
        h2.setLocalName("F\u00eate nationale");
        h2.setFixed(true);
        h2.setGlobal(true);

        var holidays = new LinkedHashSet<PublicHolidayV3>();
        holidays.add(h1);
        holidays.add(h2);

        var printable = new PrintablePublicHolidaySet(holidays);
        String result = printable.toPlainString();

        assertTrue(result.contains("2026-01-01 - New Year's Day (Jour de l'an), fixed: yes, global: yes"));
        assertTrue(result.contains("2026-07-14 - Bastille Day (F\u00eate nationale), fixed: yes, global: yes"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void toTableStringFormatsAsTable() {
        var h1 = new PublicHolidayV3();
        h1.setDate(LocalDate.of(2026, 1, 1));
        h1.setName("New Year's Day");
        h1.setLocalName("Jour de l'an");
        h1.setFixed(true);
        h1.setGlobal(true);

        var holidays = new LinkedHashSet<PublicHolidayV3>();
        holidays.add(h1);

        var printable = new PrintablePublicHolidaySet(holidays);
        String result = printable.toTableString();

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("DATE"));
        assertTrue(lines[0].contains("NAME"));
        assertTrue(lines[0].contains("LOCAL NAME"));
        assertTrue(lines[0].contains("FIXED"));
        assertTrue(lines[0].contains("GLOBAL"));
        assertTrue(lines[1].startsWith("----"));
        assertTrue(lines[2].contains("2026-01-01"));
        assertTrue(lines[2].contains("New Year's Day"));
        assertTrue(lines[2].contains("Jour de l'an"));
        assertTrue(lines[2].contains("yes"));
    }

    @Test
    void supportedOutputFormatsReturnsJsonPlainAndTable() {
        var printable = new PrintablePublicHolidaySet(Set.of());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN, OutputFormat.TABLE), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsOriginalSet() {
        Set<PublicHolidayV3> holidays = Set.of();
        var printable = new PrintablePublicHolidaySet(holidays);
        assertSame(holidays, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintablePublicHolidaySet(null));
    }
}
