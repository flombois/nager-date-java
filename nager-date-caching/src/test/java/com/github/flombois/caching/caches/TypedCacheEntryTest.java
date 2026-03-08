package com.github.flombois.caching.caches;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypedCacheEntryTest {

    @Test
    void constructorRejectsNullValue() {
        assertThrows(NullPointerException.class, () -> new TypedCacheEntry<>(null, String.class));
    }

    @Test
    void constructorRejectsNullType() {
        assertThrows(NullPointerException.class, () -> new TypedCacheEntry<>("value", null));
    }

    @Test
    void getValueReturnsStoredValue() {
        var entry = new TypedCacheEntry<>("hello", String.class);
        assertEquals("hello", entry.getValue());
    }

    @Test
    void getTypeReturnsStoredType() {
        var entry = new TypedCacheEntry<>("hello", String.class);
        assertEquals(String.class, entry.getType());
    }

    @Test
    void isInstanceOfCacheEntry() {
        var entry = new TypedCacheEntry<>("hello", String.class);
        assertInstanceOf(CacheEntry.class, entry);
    }
}
