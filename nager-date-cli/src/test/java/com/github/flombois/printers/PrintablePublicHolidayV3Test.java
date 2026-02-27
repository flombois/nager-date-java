package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.PublicHolidayV3;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintablePublicHolidayV3Test {

    @Test
    void toPlainStringFormatsCorrectly() {
        var holiday = new PublicHolidayV3();
        holiday.setDate(LocalDate.of(2026, 1, 1));
        holiday.setName("New Year's Day");
        holiday.setLocalName("Jour de l'an");
        holiday.setFixed(true);
        holiday.setGlobal(true);

        var printable = new PrintablePublicHolidayV3(holiday);

        assertEquals("2026-01-01 - New Year's Day (Jour de l'an), fixed: yes, global: yes", printable.toPlainString());
    }

    @Test
    void toPlainStringNotFixedNotGlobal() {
        var holiday = new PublicHolidayV3();
        holiday.setDate(LocalDate.of(2026, 4, 5));
        holiday.setName("Easter Sunday");
        holiday.setLocalName("P\u00e2ques");
        holiday.setFixed(false);
        holiday.setGlobal(false);

        var printable = new PrintablePublicHolidayV3(holiday);

        assertEquals("2026-04-05 - Easter Sunday (P\u00e2ques), fixed: no, global: no", printable.toPlainString());
    }

    @Test
    void supportedOutputFormatsReturnsJsonAndPlain() {
        var holiday = new PublicHolidayV3();
        var printable = new PrintablePublicHolidayV3(holiday);
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsWrappedObject() {
        var holiday = new PublicHolidayV3();
        var printable = new PrintablePublicHolidayV3(holiday);
        assertSame(holiday, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintablePublicHolidayV3(null));
    }
}
