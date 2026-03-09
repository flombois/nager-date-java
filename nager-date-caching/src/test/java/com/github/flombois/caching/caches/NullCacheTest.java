package com.github.flombois.caching.caches;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullCacheTest {

    private final NullCache<String> cache = new NullCache<>();

    @Test
    void getAlwaysReturnsEmpty() {
        assertTrue(cache.get("any-key").isEmpty());
    }

    @Test
    void putReturnsACacheEntryWithNullValue() {
        final var entry = new CacheEntry<>("value");
        assertEquals(entry, cache.put("key",entry));
    }

    @Test
    void getAfterPutStillReturnsEmpty() {
        cache.put("key", new CacheEntry<>("value"));
        assertTrue(cache.get("key").isEmpty());
    }

    @Test
    void evictIsNoOp() {
        assertDoesNotThrow(() -> cache.evict("key"));
    }

    @Test
    void clearIsNoOp() {
        assertDoesNotThrow(cache::clear);
    }
}
