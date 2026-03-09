package com.github.flombois.models;

import com.neovisionaries.i18n.CountryCode;

class CountryInfoEqualsHashCodeTest extends AbstractEqualsHashCodeTest<CountryInfo> {

    @Override
    protected CountryInfo createInstance() {
        var ci = new CountryInfo();
        ci.setCountryCode(CountryCode.FR);
        ci.setCommonName("France");
        ci.setOfficialName("French Republic");
        ci.setRegion("Europe");
        return ci;
    }

    @Override
    protected CountryInfo createEqualInstance() {
        var ci = new CountryInfo();
        ci.setCountryCode(CountryCode.FR);
        ci.setCommonName("France");
        ci.setOfficialName("French Republic");
        ci.setRegion("Europe");
        return ci;
    }

    @Override
    protected CountryInfo createThirdEqualInstance() {
        var ci = new CountryInfo();
        ci.setCountryCode(CountryCode.FR);
        ci.setCommonName("France");
        ci.setOfficialName("French Republic");
        ci.setRegion("Europe");
        return ci;
    }

    @Override
    protected CountryInfo createDifferentInstance() {
        var ci = new CountryInfo();
        ci.setCountryCode(CountryCode.JP);
        ci.setCommonName("Japan");
        ci.setOfficialName("Japan");
        ci.setRegion("Asia");
        return ci;
    }
}
