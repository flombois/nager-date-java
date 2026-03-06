package com.github.flombois.caching.caches;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheEntryTest {

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new CacheEntry<>(null));
    }

    @Test
    void getValueReturnsStoredValue() {
        var entry = new CacheEntry<>("hello");
        assertEquals("hello", entry.getValue());
    }

    @Test
    void getValueReturnsComplexObject() {
        var list = java.util.List.of(1, 2, 3);
        var entry = new CacheEntry<>(list);
        assertSame(list, entry.getValue());
    }
}
