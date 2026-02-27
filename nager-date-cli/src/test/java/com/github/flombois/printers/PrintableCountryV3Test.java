package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.v3.CountryV3;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableCountryV3Test {

    @Test
    void toPlainStringFormatsCorrectly() {
        var country = new CountryV3();
        country.setName("France");
        country.setCountryCode(CountryCode.FR);

        var printable = new PrintableCountryV3(country);

        assertEquals("Country: France, ISO Code: FR", printable.toPlainString());
    }

    @Test
    void supportedOutputFormatsReturnsJsonAndPlain() {
        var printable = new PrintableCountryV3(new CountryV3());
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN), printable.supportedOutputFormats());
    }

    @Test
    void recordReturnsWrappedObject() {
        var country = new CountryV3();
        var printable = new PrintableCountryV3(country);
        assertSame(country, printable.record());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableCountryV3(null));
    }
}
