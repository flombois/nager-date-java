package com.github.flombois.factories.caches;

import com.github.flombois.caching.caches.MapCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashMapCacheFactoryTest {

    private final HashMapCacheFactory factory = new HashMapCacheFactory();

    @Test
    void createReturnsMapCacheInstance() {
        assertInstanceOf(MapCache.class, factory.create());
    }

    @Test
    void createReturnsNewInstanceEachTime() {
        var first = factory.create();
        var second = factory.create();
        assertNotSame(first, second);
    }
}
