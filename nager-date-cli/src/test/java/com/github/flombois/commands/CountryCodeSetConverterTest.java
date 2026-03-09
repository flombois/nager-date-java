package com.github.flombois.commands;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CountryCodeSetConverterTest {

    private final CountryCodeSetConverter converter = new CountryCodeSetConverter();

    @Test
    void convertsSingleCountryCode() {
        Set<CountryCode> result = converter.convert("FR");
        assertEquals(Set.of(CountryCode.FR), result);
    }

    @Test
    void convertsMultipleCountryCodes() {
        Set<CountryCode> result = converter.convert("FR,DE,US");
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Set.of(CountryCode.FR, CountryCode.DE, CountryCode.US)));
    }

    @Test
    void deduplicatesDuplicateCodes() {
        Set<CountryCode> result = converter.convert("FR,DE,FR");
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Set.of(CountryCode.FR, CountryCode.DE)));
    }

    @Test
    void trimsWhitespace() {
        Set<CountryCode> result = converter.convert(" FR , DE , US ");
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Set.of(CountryCode.FR, CountryCode.DE, CountryCode.US)));
    }

    @Test
    void throwsOnInvalidCountryCode() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("INVALID"));
    }
}
