package com.github.flombois.caching.strategies;

import com.github.flombois.caching.caches.MapCache;
import com.github.flombois.services.CachedServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCachingStrategyTest {

    private DefaultCachingStrategy<String> strategy;
    private MapCache<String> cache;

    @BeforeEach
    void setUp() {
        cache = new MapCache<>(new HashMap<>());
        strategy = new DefaultCachingStrategy<>(cache);
    }

    @Test
    void constructorRejectsNullCache() {
        assertThrows(NullPointerException.class, () -> new DefaultCachingStrategy<>(null));
    }

    @Test
    void cacheMissCallsServiceAndReturnsResult() throws CachedServiceException {
        var result = strategy.cacheCall("key", () -> "value");
        assertEquals("value", result);
    }

    @Test
    void cacheMissStoresResultInCache() throws CachedServiceException {
        strategy.cacheCall("key", () -> "value");
        assertTrue(cache.get("key").isPresent());
        assertEquals("value", cache.get("key").get().getValue());
    }

    @Test
    void cacheHitReturnsStoredValue() throws CachedServiceException {
        strategy.cacheCall("key", () -> "first");
        var result = strategy.cacheCall("key", () -> "second");
        assertEquals("first", result);
    }

    @Test
    void cacheHitDoesNotCallServiceAgain() throws CachedServiceException {
        var callCount = new AtomicInteger(0);
        strategy.cacheCall("key", () -> {
            callCount.incrementAndGet();
            return "value";
        });
        strategy.cacheCall("key", () -> {
            callCount.incrementAndGet();
            return "other";
        });
        assertEquals(1, callCount.get());
    }

    @Test
    void differentKeysAreCachedSeparately() throws CachedServiceException {
        strategy.cacheCall("key1", () -> "value1");
        strategy.cacheCall("key2", () -> "value2");
        assertEquals("value1", strategy.cacheCall("key1", () -> "ignored"));
        assertEquals("value2", strategy.cacheCall("key2", () -> "ignored"));
    }

    @Test
    void serviceExceptionIsWrappedInCachedServiceException() {
        var exception = assertThrows(CachedServiceException.class,
                () -> strategy.cacheCall("key", () -> { throw new RuntimeException("boom"); }));
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("boom", exception.getCause().getMessage());
    }

    @Test
    void checkedExceptionIsWrappedInCachedServiceException() {
        var exception = assertThrows(CachedServiceException.class,
                () -> strategy.cacheCall("key", () -> { throw new Exception("checked"); }));
        assertEquals("checked", exception.getCause().getMessage());
    }
}
