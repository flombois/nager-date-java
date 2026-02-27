package com.github.flombois.services.v3;

import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class HttpLongWeekendV3ServiceTest {

    private HttpLongWeekendV3Service service;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wmRuntimeInfo) {
        NagerDateHttpClient client = new NagerDateHttpClient(wmRuntimeInfo.getHttpBaseUrl());
        service = new HttpLongWeekendV3Service(client);
    }

    @Test
    void getLongWeekend_returnsLongWeekend() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/LongWeekend/2026/FR"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [{
                                  "startDate": "2026-05-01",
                                  "endDate": "2026-05-04",
                                  "dayCount": 4,
                                  "needBridgeDay": true,
                                  "bridgeDays": ["2026-05-02"]
                                }]
                                """)));

        Set<LongWeekendV3> results = service.getLongWeekend(CountryCode.FR, Year.of(2026), 0, "");

        assertEquals(1, results.size());
        LongWeekendV3 result = results.iterator().next();
        assertEquals(4, result.getDayCount());
        assertTrue(result.isNeedBridgeDay());
        assertEquals(LocalDate.of(2026, 5, 1), result.getStartDate());
        assertEquals(LocalDate.of(2026, 5, 4), result.getEndDate());
        assertArrayEquals(new String[]{"2026-05-02"}, result.getBridgeDays());
    }

    @Test
    void getLongWeekend_withBridgeDaysParam() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/LongWeekend/2026/DE?availableBridgeDays=2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [{
                                  "startDate": "2026-10-31",
                                  "endDate": "2026-11-03",
                                  "dayCount": 4,
                                  "needBridgeDay": false,
                                  "bridgeDays": []
                                }]
                                """)));

        Set<LongWeekendV3> results = service.getLongWeekend(CountryCode.DE, Year.of(2026), 2, "");

        assertEquals(1, results.size());
        LongWeekendV3 result = results.iterator().next();
        assertEquals(4, result.getDayCount());
        assertFalse(result.isNeedBridgeDay());
    }

    @Test
    void getLongWeekend_withSubdivisionCode() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/LongWeekend/2026/DE?subdivisionCode=DE-BY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [{
                                  "startDate": "2026-06-04",
                                  "endDate": "2026-06-07",
                                  "dayCount": 4,
                                  "needBridgeDay": true,
                                  "bridgeDays": ["2026-06-05"]
                                }]
                                """)));

        Set<LongWeekendV3> results = service.getLongWeekend(CountryCode.DE, Year.of(2026), 0, "DE-BY");

        assertEquals(1, results.size());
        assertEquals(4, results.iterator().next().getDayCount());
    }

    @Test
    void getLongWeekend_withBothParams() throws NagerDateServiceException {
        stubFor(get(urlEqualTo("/LongWeekend/2026/DE?availableBridgeDays=1&subdivisionCode=DE-BY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [{
                                  "startDate": "2026-06-04",
                                  "endDate": "2026-06-07",
                                  "dayCount": 4,
                                  "needBridgeDay": true,
                                  "bridgeDays": ["2026-06-05"]
                                }]
                                """)));

        Set<LongWeekendV3> results = service.getLongWeekend(CountryCode.DE, Year.of(2026), 1, "DE-BY");

        assertEquals(1, results.size());
        assertEquals(4, results.iterator().next().getDayCount());
    }

    @Test
    void getLongWeekend_throwsOnNotFound() {
        stubFor(get(urlPathMatching("/LongWeekend/.*"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(NagerDateServiceException.class,
                () -> service.getLongWeekend(CountryCode.FR, Year.of(2026), 0, ""));
    }

    @Test
    void getLongWeekend_throwsOnServerError() {
        stubFor(get(urlPathMatching("/LongWeekend/.*"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertThrows(NagerDateServiceException.class,
                () -> service.getLongWeekend(CountryCode.FR, Year.of(2026), 0, ""));
    }
}
