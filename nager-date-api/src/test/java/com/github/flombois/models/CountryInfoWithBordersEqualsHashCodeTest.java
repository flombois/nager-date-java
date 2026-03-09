package com.github.flombois.models;

import com.neovisionaries.i18n.CountryCode;

import java.util.Set;

class CountryInfoWithBordersEqualsHashCodeTest extends AbstractEqualsHashCodeTest<CountryInfoWithBorders> {

    @Override
    protected CountryInfoWithBorders createInstance() {
        var ci = new CountryInfoWithBorders();
        ci.setCountryCode(CountryCode.FR);
        ci.setCommonName("France");
        ci.setOfficialName("French Republic");
        ci.setRegion("Europe");
        ci.setBorders(Set.of(border(CountryCode.DE, "Germany"), border(CountryCode.ES, "Spain")));
        return ci;
    }

    @Override
    protected CountryInfoWithBorders createEqualInstance() {
        var ci = new CountryInfoWithBorders();
        ci.setCountryCode(CountryCode.FR);
        ci.setCommonName("France");
        ci.setOfficialName("French Republic");
        ci.setRegion("Europe");
        ci.setBorders(Set.of(border(CountryCode.DE, "Germany"), border(CountryCode.ES, "Spain")));
        return ci;
    }

    @Override
    protected CountryInfoWithBorders createThirdEqualInstance() {
        var ci = new CountryInfoWithBorders();
        ci.setCountryCode(CountryCode.FR);
        ci.setCommonName("France");
        ci.setOfficialName("French Republic");
        ci.setRegion("Europe");
        ci.setBorders(Set.of(border(CountryCode.DE, "Germany"), border(CountryCode.ES, "Spain")));
        return ci;
    }

    @Override
    protected CountryInfoWithBorders createDifferentInstance() {
        var ci = new CountryInfoWithBorders();
        ci.setCountryCode(CountryCode.JP);
        ci.setCommonName("Japan");
        ci.setOfficialName("Japan");
        ci.setRegion("Asia");
        ci.setBorders(Set.of());
        return ci;
    }

    private CountryInfo border(CountryCode code, String name) {
        var b = new CountryInfo();
        b.setCountryCode(code);
        b.setCommonName(name);
        b.setOfficialName(name);
        b.setRegion("Europe");
        return b;
    }
}
