package com.github.flombois.commands;

import com.github.flombois.commands.Command.ListAllCountriesCommand;
import com.github.flombois.http.NagerDateHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceInvocationCommandTest {

    @Test
    void newNagerDateHttpClientWithNoBaseUrlUsesPublicEndpoint() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "";
        command.key = "";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithBaseUrlOnly() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "https://custom.api.com";
        command.key = "";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals("https://custom.api.com", client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithBaseUrlAndKey() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "https://custom.api.com";
        command.key = "my-api-key";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals("https://custom.api.com", client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithBlankBaseUrlUsesPublicEndpoint() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "   ";
        command.key = "some-key";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithNullBaseUrlUsesPublicEndpoint() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = null;
        command.key = "";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }
}
