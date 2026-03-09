package com.github.flombois.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NagerDateHttpClientTest {

    @Test
    void defaultConstructorUsesPublicEndpoint() {
        var client = new NagerDateHttpClient();
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }

    @Test
    void customBaseUrlIsUsed() {
        var client = new NagerDateHttpClient("http://localhost:8080");
        assertEquals("http://localhost:8080", client.getBaseUrl());
    }

    @Test
    void httpClientIsNotNull() {
        var client = new NagerDateHttpClient();
        assertNotNull(client.getHttpClient());
    }

    @Test
    void newHttpGetRequestBuildsCorrectUri() {
        var client = new NagerDateHttpClient("http://localhost:8080");
        var request = client.newHttpGetRequest("/PublicHolidays/2026/FR");
        assertEquals("http://localhost:8080/PublicHolidays/2026/FR", request.uri().toString());
    }

    @Test
    void newHttpGetRequestUsesGetMethod() {
        var client = new NagerDateHttpClient();
        var request = client.newHttpGetRequest("/test");
        assertEquals("GET", request.method());
    }

    @Test
    void newHttpGetRequestSetsAcceptJsonHeader() {
        var client = new NagerDateHttpClient();
        var request = client.newHttpGetRequest("/test");
        assertTrue(request.headers().firstValue("Accept").isPresent());
        assertEquals("application/json", request.headers().firstValue("Accept").get());
    }

    @Test
    void newHttpGetRequestHasTimeout() {
        var client = new NagerDateHttpClient();
        var request = client.newHttpGetRequest("/test");
        assertTrue(request.timeout().isPresent());
    }
}
