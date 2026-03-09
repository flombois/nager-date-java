package com.github.flombois.commands;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class YearConverterTest {

    private final YearConverter converter = new YearConverter();

    @Test
    void convertsValidYear() {
        assertEquals(Year.of(2026), converter.convert("2026"));
    }

    @Test
    void convertsMinYear() {
        assertEquals(Year.of(1), converter.convert("1"));
    }

    @Test
    void convertsMaxYear() {
        assertEquals(Year.of(9999), converter.convert("9999"));
    }

    @Test
    void throwsOnInvalidFormat() {
        assertThrows(DateTimeParseException.class, () -> converter.convert("not-a-year"));
    }

    @Test
    void throwsOnEmptyString() {
        assertThrows(DateTimeParseException.class, () -> converter.convert(""));
    }

    @Test
    void throwsOnDecimalYear() {
        assertThrows(DateTimeParseException.class, () -> converter.convert("2026.5"));
    }
}
