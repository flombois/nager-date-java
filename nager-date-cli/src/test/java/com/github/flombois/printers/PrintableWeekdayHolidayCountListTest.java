package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.WeekdayHolidayCount;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableWeekdayHolidayCountListTest {

    @Test
    void toPlainStringFormatsCountryAndCount() {
        var records = List.of(
                new WeekdayHolidayCount(CountryCode.FR, 9),
                new WeekdayHolidayCount(CountryCode.DE, 8)
        );
        var printable = new PrintableWeekdayHolidayCountList(records);
        String result = printable.toPlainString();

        assertTrue(result.contains("FR - 9 weekday holidays"));
        assertTrue(result.contains("DE - 8 weekday holidays"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void toTableStringFormatsAsTable() {
        var records = List.of(new WeekdayHolidayCount(CountryCode.FR, 9));
        var printable = new PrintableWeekdayHolidayCountList(records);
        String result = printable.toTableString();

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("COUNTRY"));
        assertTrue(lines[0].contains("WEEKDAY HOLIDAYS"));
        assertTrue(lines[1].startsWith("----"));
        assertTrue(lines[2].contains("FR"));
        assertTrue(lines[2].contains("9"));
    }

    @Test
    void supportedOutputFormatsReturnsJsonPlainAndTable() {
        var printable = new PrintableWeekdayHolidayCountList(List.of());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN, OutputFormat.TABLE), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsOriginalList() {
        List<WeekdayHolidayCount> records = List.of();
        var printable = new PrintableWeekdayHolidayCountList(records);
        assertSame(records, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableWeekdayHolidayCountList(null));
    }
}
