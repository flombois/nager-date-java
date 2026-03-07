package com.github.flombois.caching.strategies;

import com.github.flombois.caching.caches.CacheEntry;
import com.github.flombois.caching.caches.NullCache;
import com.github.flombois.caching.caches.TypedCacheEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemCachingStrategyTest {

    private final FileSystemCachingStrategy<Object> strategy =
            new FileSystemCachingStrategy<>(new NullCache<>(), Object.class);

    @Test
    void buildKeyReturnsHexString() {
        String key = strategy.buildKey("MyService", "param1");
        assertTrue(key.matches("^[0-9a-f]{64}$"), "Key should be a 64-char hex string (SHA-256)");
    }

    @Test
    void sameInputProducesSameHash() {
        String key1 = strategy.buildKey("MyService", "getHolidays", "FR", 2026);
        String key2 = strategy.buildKey("MyService", "getHolidays", "FR", 2026);
        assertEquals(key1, key2);
    }

    @Test
    void differentInputsProduceDifferentHashes() {
        String key1 = strategy.buildKey("MyService", "FR", 2025);
        String key2 = strategy.buildKey("MyService", "DE", 2025);
        assertNotEquals(key1, key2);
    }

    @Test
    void differentServicesProduceDifferentHashes() {
        String key1 = strategy.buildKey("ServiceA", "param");
        String key2 = strategy.buildKey("ServiceB", "param");
        assertNotEquals(key1, key2);
    }

    @Test
    void buildKeyRejectsNullServiceName() {
        assertThrows(NullPointerException.class, () -> strategy.buildKey(null));
    }

    @Test
    void createEntryReturnsTypedCacheEntry() {
        CacheEntry<Object> entry = strategy.createEntry("value");
        assertInstanceOf(TypedCacheEntry.class, entry);
    }

    @Test
    void createEntryCarriesCorrectType() {
        CacheEntry<Object> entry = strategy.createEntry("value");
        TypedCacheEntry<Object> typed = (TypedCacheEntry<Object>) entry;
        assertEquals(Object.class, typed.getType());
    }

    @Test
    void createEntryCarriesCorrectValue() {
        CacheEntry<Object> entry = strategy.createEntry("hello");
        assertEquals("hello", entry.getValue());
    }
}
