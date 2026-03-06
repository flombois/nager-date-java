package com.github.flombois.caching.strategies;

import com.github.flombois.services.CachedServiceException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ForwardWithoutCachingTest {

    private final ForwardWithoutCaching<String> strategy = new ForwardWithoutCaching<>();

    @Test
    void alwaysCallsServiceAndReturnsResult() throws CachedServiceException {
        var result = strategy.cacheCall("key", () -> "value");
        assertEquals("value", result);
    }

    @Test
    void alwaysCallsServiceEvenWithSameKey() throws CachedServiceException {
        var callCount = new AtomicInteger(0);
        strategy.cacheCall("key", () -> {
            callCount.incrementAndGet();
            return "value";
        });
        strategy.cacheCall("key", () -> {
            callCount.incrementAndGet();
            return "value";
        });
        assertEquals(2, callCount.get());
    }

    @Test
    void wrapsServiceExceptionInCachedServiceException() {
        var exception = assertThrows(CachedServiceException.class,
                () -> strategy.cacheCall("key", () -> { throw new RuntimeException("boom"); }));
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("boom", exception.getCause().getMessage());
    }
}
