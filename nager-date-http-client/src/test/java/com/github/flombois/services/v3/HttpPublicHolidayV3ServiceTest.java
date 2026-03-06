package com.github.flombois.services.v3;

import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class HttpPublicHolidayV3ServiceTest {

    private HttpPublicHolidayV3Service service;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wmRuntimeInfo) {
        NagerDateHttpClient client = new NagerDateHttpClient(wmRuntimeInfo.getHttpBaseUrl());
        service = new HttpPublicHolidayV3Service(client);
    }

    @Test
    void getPublicHolidays_returnsHolidays() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/PublicHolidays/2026/FR"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "date": "2026-01-01",
                                    "localName": "Jour de l'an",
                                    "name": "New Year's Day",
                                    "countryCode": "FR",
                                    "fixed": true,
                                    "global": true,
                                    "types": ["Public"]
                                  },
                                  {
                                    "date": "2026-07-14",
                                    "localName": "Fête nationale",
                                    "name": "Bastille Day",
                                    "countryCode": "FR",
                                    "fixed": true,
                                    "global": true,
                                    "types": ["Public"]
                                  }
                                ]
                                """)));

        Set<PublicHolidayV3> holidays = service.getPublicHolidays(CountryCode.FR, Year.of(2026));

        assertEquals(2, holidays.size());
    }

    @Test
    void getPublicHolidays_returnsEmptySet() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/PublicHolidays/2026/FR"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        Set<PublicHolidayV3> holidays = service.getPublicHolidays(CountryCode.FR, Year.of(2026));

        assertTrue(holidays.isEmpty());
    }

    @Test
    void getPublicHolidays_throwsOnNotFound() {
        stubFor(get(urlEqualTo("/PublicHolidays/2026/FR"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(NagerDateServiceException.class,
                () -> service.getPublicHolidays(CountryCode.FR, Year.of(2026)));
    }

    @Test
    void getPublicHolidays_throwsOnServerError() {
        stubFor(get(urlEqualTo("/PublicHolidays/2026/FR"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertThrows(NagerDateServiceException.class,
                () -> service.getPublicHolidays(CountryCode.FR, Year.of(2026)));
    }

    @Test
    void isTodayAPublicHoliday_returnsTrueOn200() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/IsTodayPublicHoliday/FR"))
                .willReturn(aResponse()
                        .withStatus(200)));

        boolean result = service.isTodayAPublicHoliday(CountryCode.FR, null, 127);

        assertTrue(result);
    }

    @Test
    void isTodayAPublicHoliday_returnsFalseOn204() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/IsTodayPublicHoliday/DE"))
                .willReturn(aResponse()
                        .withStatus(204)));

        boolean result = service.isTodayAPublicHoliday(CountryCode.DE, null, 127);

        assertFalse(result);
    }

    @Test
    void isTodayAPublicHoliday_withCountyCode() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/IsTodayPublicHoliday/DE?countyCode=DE-BY"))
                .willReturn(aResponse()
                        .withStatus(200)));

        boolean result = service.isTodayAPublicHoliday(CountryCode.DE, "DE-BY", 127);

        assertTrue(result);
    }

    @Test
    void isTodayAPublicHoliday_withOffset() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/IsTodayPublicHoliday/FR?offset=2"))
                .willReturn(aResponse()
                        .withStatus(200)));

        boolean result = service.isTodayAPublicHoliday(CountryCode.FR, null, 2);

        assertTrue(result);
    }

    @Test
    void isTodayAPublicHoliday_withBothParams() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/IsTodayPublicHoliday/DE?countyCode=DE-BY&offset=1"))
                .willReturn(aResponse()
                        .withStatus(200)));

        boolean result = service.isTodayAPublicHoliday(CountryCode.DE, "DE-BY", 1);

        assertTrue(result);
    }

    @Test
    void isTodayAPublicHoliday_throwsOnServerError() {
        stubFor(get(urlPathMatching("/IsTodayPublicHoliday/.*"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertThrows(NagerDateServiceException.class,
                () -> service.isTodayAPublicHoliday(CountryCode.FR, null, 127));
    }
}
