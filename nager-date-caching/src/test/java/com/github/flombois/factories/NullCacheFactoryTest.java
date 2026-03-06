package com.github.flombois.factories;

import com.github.flombois.caching.caches.NullCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullCacheFactoryTest {

    private final NullCacheFactory factory = new NullCacheFactory();

    @Test
    void createReturnsNullCacheInstance() {
        assertInstanceOf(NullCache.class, factory.create());
    }

    @Test
    void createReturnsNewInstanceEachTime() {
        var first = factory.create();
        var second = factory.create();
        assertNotSame(first, second);
    }
}
