package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.LongWeekendV3;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableLongWeekendV3Test {

    @Test
    void toPlainStringFormatsCorrectly() {
        var longWeekend = new LongWeekendV3();
        longWeekend.setStartDate(LocalDate.of(2026, 5, 1));
        longWeekend.setEndDate(LocalDate.of(2026, 5, 4));
        longWeekend.setDayCount(4);
        longWeekend.setNeedBridgeDay(true);

        var printable = new PrintableLongWeekendV3(longWeekend);

        assertEquals("Long weekend: 2026-05-01 - 2026-05-04 (4 days, bridge day: yes)", printable.toPlainString());
    }

    @Test
    void toPlainStringNoBridgeDay() {
        var longWeekend = new LongWeekendV3();
        longWeekend.setStartDate(LocalDate.of(2026, 12, 25));
        longWeekend.setEndDate(LocalDate.of(2026, 12, 28));
        longWeekend.setDayCount(4);
        longWeekend.setNeedBridgeDay(false);

        var printable = new PrintableLongWeekendV3(longWeekend);

        assertEquals("Long weekend: 2026-12-25 - 2026-12-28 (4 days, bridge day: no)", printable.toPlainString());
    }

    @Test
    void supportedOutputFormatsReturnsJsonAndPlain() {
        var longWeekend = new LongWeekendV3();
        var printable = new PrintableLongWeekendV3(longWeekend);
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsWrappedObject() {
        var longWeekend = new LongWeekendV3();
        var printable = new PrintableLongWeekendV3(longWeekend);
        assertSame(longWeekend, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableLongWeekendV3(null));
    }
}
