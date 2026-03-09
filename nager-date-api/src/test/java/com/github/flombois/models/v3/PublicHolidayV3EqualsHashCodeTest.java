package com.github.flombois.models.v3;

import com.github.flombois.models.AbstractEqualsHashCodeTest;
import com.neovisionaries.i18n.CountryCode;

import java.time.LocalDate;

class PublicHolidayV3EqualsHashCodeTest extends AbstractEqualsHashCodeTest<PublicHolidayV3> {

    @Override
    protected PublicHolidayV3 createInstance() {
        var h = new PublicHolidayV3();
        h.setCountryCode(CountryCode.FR);
        h.setDate(LocalDate.of(2026, 1, 1));
        h.setLocalName("Jour de l'An");
        h.setName("New Year's Day");
        h.setFixed(true);
        h.setGlobal(true);
        return h;
    }

    @Override
    protected PublicHolidayV3 createEqualInstance() {
        var h = new PublicHolidayV3();
        h.setCountryCode(CountryCode.FR);
        h.setDate(LocalDate.of(2026, 1, 1));
        h.setLocalName("Jour de l'An");
        h.setName("New Year's Day");
        h.setFixed(true);
        h.setGlobal(true);
        return h;
    }

    @Override
    protected PublicHolidayV3 createThirdEqualInstance() {
        var h = new PublicHolidayV3();
        h.setCountryCode(CountryCode.FR);
        h.setDate(LocalDate.of(2026, 1, 1));
        h.setLocalName("Jour de l'An");
        h.setName("New Year's Day");
        h.setFixed(true);
        h.setGlobal(true);
        return h;
    }

    @Override
    protected PublicHolidayV3 createDifferentInstance() {
        var h = new PublicHolidayV3();
        h.setCountryCode(CountryCode.DE);
        h.setDate(LocalDate.of(2026, 10, 3));
        h.setLocalName("Tag der Deutschen Einheit");
        h.setName("German Unity Day");
        h.setFixed(true);
        h.setGlobal(true);
        return h;
    }
}
