package com.github.flombois.commands;

import com.github.flombois.commands.Command.ListAllCountriesCommand;
import com.github.flombois.factories.CachedServicesFactory;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.http.NagerDateHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceInvocationCommandTest {

    @AfterEach
    void resetCommand() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "";
        command.cache = false;
        command.cacheFs = false;
    }

    @Test
    void newNagerDateHttpClientWithNoBaseUrlUsesPublicEndpoint() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithBaseUrl() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "https://custom.api.com";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals("https://custom.api.com", client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithBlankBaseUrlUsesPublicEndpoint() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = "   ";

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }

    @Test
    void newNagerDateHttpClientWithNullBaseUrlUsesPublicEndpoint() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.baseUrl = null;

        NagerDateHttpClient client = command.newNagerDateHttpClient();

        assertNotNull(client);
        assertEquals(NagerDateHttpClient.PUBLIC_V3_ENDPOINT, client.getBaseUrl());
    }

    @Test
    void getServicesFactoryWithCacheEnabledReturnsCachedServicesFactory() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.cache = true;
        command.baseUrl = "";

        ServicesFactory factory = command.getServicesFactory();

        assertInstanceOf(CachedServicesFactory.class, factory);
    }

    @Test
    void getServicesFactoryWithCacheDisabledReturnsCachedServicesFactory() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.cache = false;
        command.baseUrl = "";

        ServicesFactory factory = command.getServicesFactory();

        assertInstanceOf(CachedServicesFactory.class, factory);
    }

    @Test
    void getServicesFactoryWithCacheFsReturnsCachedServicesFactory() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.cacheFs = true;
        command.cache = false;
        command.baseUrl = "";

        ServicesFactory factory = command.getServicesFactory();

        assertInstanceOf(CachedServicesFactory.class, factory);
    }

    @Test
    void getServicesFactoryWithCacheFsTakesPriorityOverCache() {
        var command = ListAllCountriesCommand.INSTANCE;
        command.cacheFs = true;
        command.cache = true;
        command.baseUrl = "";

        ServicesFactory factory = command.getServicesFactory();

        assertInstanceOf(CachedServicesFactory.class, factory);
    }
}
