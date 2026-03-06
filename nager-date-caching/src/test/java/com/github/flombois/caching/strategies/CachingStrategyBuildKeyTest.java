package com.github.flombois.caching.strategies;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CachingStrategyBuildKeyTest {

    // Use ForwardWithoutCaching as a concrete implementation to test the default buildKey method
    private final CachingStrategy<Object> strategy = new ForwardWithoutCaching<>();

    @Test
    void buildKeyRejectsNullServiceName() {
        assertThrows(NullPointerException.class, () -> strategy.buildKey(null));
    }

    @Test
    void buildKeyWithNoParams() {
        var key = strategy.buildKey("MyService");
        assertEquals("MyService:[]", key);
    }

    @Test
    void buildKeyWithSingleParam() {
        var key = strategy.buildKey("MyService", "param1");
        assertEquals("MyService:[param1]", key);
    }

    @Test
    void buildKeyWithMultipleParams() {
        var key = strategy.buildKey("MyService", "getHolidays", "FR", 2026);
        assertEquals("MyService:[getHolidays, FR, 2026]", key);
    }

    @Test
    void buildKeyWithNullParam() {
        var key = strategy.buildKey("MyService", "method", null);
        assertEquals("MyService:[method, null]", key);
    }

    @Test
    void differentParamsProduceDifferentKeys() {
        var key1 = strategy.buildKey("Service", "FR", 2025);
        var key2 = strategy.buildKey("Service", "DE", 2025);
        assertNotEquals(key1, key2);
    }

    @Test
    void differentServicesProduceDifferentKeys() {
        var key1 = strategy.buildKey("ServiceA", "param");
        var key2 = strategy.buildKey("ServiceB", "param");
        assertNotEquals(key1, key2);
    }
}
