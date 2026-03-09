package com.github.flombois.models.v3;

import com.github.flombois.models.AbstractEqualsHashCodeTest;
import com.neovisionaries.i18n.CountryCode;

class CountryV3EqualsHashCodeTest extends AbstractEqualsHashCodeTest<CountryV3> {

    @Override
    protected CountryV3 createInstance() {
        var c = new CountryV3();
        c.setCountryCode(CountryCode.FR);
        c.setName("France");
        return c;
    }

    @Override
    protected CountryV3 createEqualInstance() {
        var c = new CountryV3();
        c.setCountryCode(CountryCode.FR);
        c.setName("France");
        return c;
    }

    @Override
    protected CountryV3 createThirdEqualInstance() {
        var c = new CountryV3();
        c.setCountryCode(CountryCode.FR);
        c.setName("France");
        return c;
    }

    @Override
    protected CountryV3 createDifferentInstance() {
        var c = new CountryV3();
        c.setCountryCode(CountryCode.DE);
        c.setName("Germany");
        return c;
    }
}
