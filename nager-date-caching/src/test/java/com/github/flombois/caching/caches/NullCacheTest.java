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
    void putReturnsNull() {
        assertNull(cache.put("key", new CacheEntry<>("value")));
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
