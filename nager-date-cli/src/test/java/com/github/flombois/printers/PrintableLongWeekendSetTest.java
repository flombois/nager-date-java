package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.LongWeekendV3;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableLongWeekendSetTest {

    @Test
    void toPlainStringJoinsWithNewlines() {
        var lw1 = new LongWeekendV3();
        lw1.setStartDate(LocalDate.of(2026, 5, 1));
        lw1.setEndDate(LocalDate.of(2026, 5, 4));
        lw1.setDayCount(4);
        lw1.setNeedBridgeDay(true);

        var lw2 = new LongWeekendV3();
        lw2.setStartDate(LocalDate.of(2026, 12, 25));
        lw2.setEndDate(LocalDate.of(2026, 12, 28));
        lw2.setDayCount(4);
        lw2.setNeedBridgeDay(false);

        var weekends = new LinkedHashSet<LongWeekendV3>();
        weekends.add(lw1);
        weekends.add(lw2);

        var printable = new PrintableLongWeekendSet(weekends);
        String result = printable.toPlainString();

        assertTrue(result.contains("Long weekend: 2026-05-01 - 2026-05-04 (4 days, bridge day: yes)"));
        assertTrue(result.contains("Long weekend: 2026-12-25 - 2026-12-28 (4 days, bridge day: no)"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void toTableStringFormatsAsTable() {
        var lw1 = new LongWeekendV3();
        lw1.setStartDate(LocalDate.of(2026, 5, 1));
        lw1.setEndDate(LocalDate.of(2026, 5, 4));
        lw1.setDayCount(4);
        lw1.setNeedBridgeDay(true);

        var weekends = new LinkedHashSet<LongWeekendV3>();
        weekends.add(lw1);

        var printable = new PrintableLongWeekendSet(weekends);
        String result = printable.toTableString();

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("START DATE"));
        assertTrue(lines[0].contains("END DATE"));
        assertTrue(lines[0].contains("DAYS"));
        assertTrue(lines[0].contains("BRIDGE DAY"));
        assertTrue(lines[1].startsWith("----------"));
        assertTrue(lines[2].contains("2026-05-01"));
        assertTrue(lines[2].contains("2026-05-04"));
        assertTrue(lines[2].contains("4"));
        assertTrue(lines[2].contains("yes"));
    }

    @Test
    void supportedOutputFormatsReturnsJsonPlainAndTable() {
        var printable = new PrintableLongWeekendSet(Set.of());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN, OutputFormat.TABLE), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsOriginalSet() {
        Set<LongWeekendV3> weekends = Set.of();
        var printable = new PrintableLongWeekendSet(weekends);
        assertSame(weekends, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableLongWeekendSet(null));
    }
}
