package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.CountryV3;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableCountrySetTest {

    @Test
    void toPlainStringJoinsCountriesWithNewlines() {
        var country1 = new CountryV3();
        country1.setName("France");
        country1.setCountryCode(CountryCode.FR);

        var country2 = new CountryV3();
        country2.setName("Germany");
        country2.setCountryCode(CountryCode.DE);

        // Use LinkedHashSet for predictable iteration order
        var countries = new LinkedHashSet<CountryV3>();
        countries.add(country1);
        countries.add(country2);

        var printable = new PrintableCountrySet(countries);
        String result = printable.toPlainString();

        assertTrue(result.contains("Country: France, ISO Code: FR"));
        assertTrue(result.contains("Country: Germany, ISO Code: DE"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void toTableStringFormatsAsTable() {
        var country1 = new CountryV3();
        country1.setName("France");
        country1.setCountryCode(CountryCode.FR);

        var countries = new LinkedHashSet<CountryV3>();
        countries.add(country1);

        var printable = new PrintableCountrySet(countries);
        String result = printable.toTableString();

        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("ISO CODE"));
        assertTrue(lines[0].contains("NAME"));
        assertTrue(lines[1].startsWith("--------"));
        assertTrue(lines[2].contains("FR"));
        assertTrue(lines[2].contains("France"));
    }

    @Test
    void supportedOutputFormatsReturnsJsonPlainAndTable() {
        var printable = new PrintableCountrySet(Set.of());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN, OutputFormat.TABLE), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsOriginalSet() {
        Set<CountryV3> countries = Set.of();
        var printable = new PrintableCountrySet(countries);
        assertSame(countries, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableCountrySet(null));
    }
}
