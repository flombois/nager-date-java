package com.github.flombois.commands;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryCodeListConverterTest {

    private final CountryCodeListConverter converter = new CountryCodeListConverter();

    @Test
    void convertsParsesCommaSeparatedCodes() {
        List<CountryCode> result = converter.convert("FR,DE,US");
        assertEquals(List.of(CountryCode.FR, CountryCode.DE, CountryCode.US), result);
    }

    @Test
    void convertHandlesWhitespace() {
        List<CountryCode> result = converter.convert("FR , DE , US");
        assertEquals(List.of(CountryCode.FR, CountryCode.DE, CountryCode.US), result);
    }

    @Test
    void convertSingleCode() {
        List<CountryCode> result = converter.convert("FR");
        assertEquals(List.of(CountryCode.FR), result);
    }

    @Test
    void convertThrowsOnInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("XX"));
    }
}
