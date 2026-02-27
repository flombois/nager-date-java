package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.CountryInfo;
import com.github.flombois.models.CountryInfoWithBorders;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrintableCountryInfoWithBordersTest {

    private CountryInfoWithBorders createCountry(String commonName, CountryCode code,
                                                  String officialName, String region,
                                                  Set<CountryInfo> borders) {
        var country = new CountryInfoWithBorders();
        country.setCommonName(commonName);
        country.setCountryCode(code);
        country.setOfficialName(officialName);
        country.setRegion(region);
        country.setBorders(borders);
        return country;
    }

    private CountryInfo createBorderCountry(String commonName) {
        var info = new CountryInfo();
        info.setCommonName(commonName);
        return info;
    }

    @Test
    void toPlainStringWithBorders() {
        var border = createBorderCountry("Germany");
        var country = createCountry("France", CountryCode.FR,
                "French Republic", "Europe", Set.of(border));

        var printable = new PrintableCountryInfoWithBorders(country);
        String result = printable.toPlainString();

        assertTrue(result.startsWith("France (FR)"));
        assertTrue(result.contains("officially French Republic"));
        assertTrue(result.contains("located in Europe."));
        assertTrue(result.contains("1 borders: Germany."));
    }

    @Test
    void toPlainStringWithNullBorders() {
        var country = createCountry("Iceland", CountryCode.IS,
                "Republic of Iceland", "Europe", null);

        var printable = new PrintableCountryInfoWithBorders(country);
        String result = printable.toPlainString();

        assertTrue(result.endsWith("It has no borders."));
    }

    @Test
    void toPlainStringWithEmptyBorders() {
        var country = createCountry("Iceland", CountryCode.IS,
                "Republic of Iceland", "Europe", Set.of());

        var printable = new PrintableCountryInfoWithBorders(country);
        String result = printable.toPlainString();

        assertTrue(result.endsWith("It has no borders."));
    }

    @Test
    void supportedOutputFormatsReturnsJsonAndPlain() {
        var country = new CountryInfoWithBorders();
        country.setCommonName("Test");
        var printable = new PrintableCountryInfoWithBorders(country);
        assertEquals(Set.of(OutputFormat.JSON, OutputFormat.PLAIN), printable.supportedOutputFormats());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new PrintableCountryInfoWithBorders(null));
    }
}
