package com.github.flombois.services.v3;

import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.models.CountryInfo;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class HttpCountryV3ServiceTest {

    private HttpCountryV3Service service;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wmRuntimeInfo) {
        NagerDateHttpClient client = new NagerDateHttpClient(wmRuntimeInfo.getHttpBaseUrl());
        service = new HttpCountryV3Service(client);
    }

    @Test
    void getAllCountries_returnsCountries() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/AvailableCountries"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {"countryCode": "FR", "name": "France"},
                                  {"countryCode": "DE", "name": "Germany"}
                                ]
                                """)));

        Set<CountryV3> countries = service.getAllCountries();

        assertEquals(2, countries.size());
    }

    @Test
    void getCountryInfo_returnsCountryInfo() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/CountryInfo/FR"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "commonName": "France",
                                  "officialName": "French Republic",
                                  "countryCode": "FR",
                                  "region": "Europe",
                                  "borders": []
                                }
                                """)));

        CountryInfo info = service.getCountryInfo(CountryCode.FR);

        assertEquals("France", info.getCommonName());
        assertEquals("French Republic", info.getOfficialName());
        assertEquals(CountryCode.FR, info.getCountryCode());
        assertEquals("Europe", info.getRegion());
    }

    @Test
    void getCountryInfoWithBorders_returnsBorders() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/CountryInfo/FR"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "commonName": "France",
                                  "officialName": "French Republic",
                                  "countryCode": "FR",
                                  "region": "Europe",
                                  "borders": [
                                    {
                                      "commonName": "Germany",
                                      "officialName": "Federal Republic of Germany",
                                      "countryCode": "DE",
                                      "region": "Europe"
                                    }
                                  ]
                                }
                                """)));

        CountryInfoWithBorders info = service.getCountryInfoWithBorders(CountryCode.FR);

        assertEquals("France", info.getCommonName());
        assertEquals(CountryCode.FR, info.getCountryCode());
        assertNotNull(info.getBorders());
        assertEquals(1, info.getBorders().size());
    }

    @Test
    void getCountryInfoWithBorders_throwsOnNotFound() {
        stubFor(get(urlEqualTo("/CountryInfo/US"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(NagerDateServiceException.class,
                () -> service.getCountryInfoWithBorders(CountryCode.US));
    }

    @Test
    void getAllCountries_throwsOnServerError() {
        stubFor(get(urlEqualTo("/AvailableCountries"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertThrows(NagerDateServiceException.class,
                () -> service.getAllCountries());
    }
}
