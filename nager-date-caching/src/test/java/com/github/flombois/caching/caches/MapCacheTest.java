package com.github.flombois.caching.caches;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MapCacheTest {

    private MapCache<String> cache;

    @BeforeEach
    void setUp() {
        cache = new MapCache<>(new HashMap<>());
    }

    @Test
    void getMissReturnsEmpty() {
        assertTrue(cache.get("unknown").isEmpty());
    }

    @Test
    void putThenGetReturnsValue() {
        cache.put("key", "value");
        var result = cache.get("key");
        assertTrue(result.isPresent());
        assertEquals("value", result.get().getValue());
    }

    @Test
    void putReturnsEntry() {
        var entry = cache.put("key", "value");
        assertNotNull(entry);
        assertEquals("value", entry.getValue());
    }

    @Test
    void putOverwritesExistingEntry() {
        cache.put("key", "first");
        cache.put("key", "second");
        assertEquals("second", cache.get("key").get().getValue());
    }

    @Test
    void evictRemovesEntry() {
        cache.put("key", "value");
        cache.evict("key");
        assertTrue(cache.get("key").isEmpty());
    }

    @Test
    void evictNonExistentKeyIsNoOp() {
        assertDoesNotThrow(() -> cache.evict("missing"));
    }

    @Test
    void clearRemovesAllEntries() {
        cache.put("a", "1");
        cache.put("b", "2");
        cache.clear();
        assertTrue(cache.get("a").isEmpty());
        assertTrue(cache.get("b").isEmpty());
    }

    @Test
    void putIfAbsentStoresOnMiss() {
        var entry = cache.putIfAbsent("key", "value");
        assertEquals("value", entry.getValue());
        assertEquals("value", cache.get("key").get().getValue());
    }

    @Test
    void putIfAbsentReturnsExistingOnHit() {
        cache.put("key", "first");
        var entry = cache.putIfAbsent("key", "second");
        assertEquals("first", entry.getValue());
    }
}
