package com.github.flombois.factories;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.MapCache;
import com.github.flombois.factories.caches.CacheFactory;
import com.github.flombois.services.v3.CachedCountryV3Service;
import com.github.flombois.services.v3.CachedLongWeekendV3Service;
import com.github.flombois.services.v3.CachedPublicHolidayV3Service;
import com.github.flombois.services.v3.CountryV3Service;
import com.github.flombois.services.v3.LongWeekendV3Service;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedServicesFactoryTest {

    @Mock
    private ServicesFactory delegateFactory;

    @Mock
    private CountryV3Service countryService;

    @Mock
    private LongWeekendV3Service longWeekendService;

    @Mock
    private PublicHolidayV3Service publicHolidayService;

    private CachedServicesFactory factory;

    @BeforeEach
    void setUp() {
        CacheFactory cacheFactory = new CacheFactory() {
            @Override
            public <T> Cache<T> create() {
                return new MapCache<>(new HashMap<>());
            }
        };
        factory = new CachedServicesFactory(delegateFactory, cacheFactory);
    }

    @Test
    void constructorRejectsNullDelegateFactory() {
        assertThrows(NullPointerException.class,
                () -> new CachedServicesFactory(null, new NullCacheFactory()));
    }

    @Test
    void constructorRejectsNullCacheFactory() {
        assertThrows(NullPointerException.class,
                () -> new CachedServicesFactory(delegateFactory, null));
    }

    @Test
    void createCountryV3ServiceReturnsCachedWrapper() {
        when(delegateFactory.createCountryV3Service()).thenReturn(countryService);
        var result = factory.createCountryV3Service();
        assertInstanceOf(CachedCountryV3Service.class, result);
    }

    @Test
    void createLongWeekendV3ServiceReturnsCachedWrapper() {
        when(delegateFactory.createLongWeekendV3Service()).thenReturn(longWeekendService);
        var result = factory.createLongWeekendV3Service();
        assertInstanceOf(CachedLongWeekendV3Service.class, result);
    }

    @Test
    void createPublicHolidayV3ServiceReturnsCachedWrapper() {
        when(delegateFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayService);
        var result = factory.createPublicHolidayV3Service();
        assertInstanceOf(CachedPublicHolidayV3Service.class, result);
    }

    @Test
    void createCountryV3ServiceDelegatesToUnderlyingFactory() {
        when(delegateFactory.createCountryV3Service()).thenReturn(countryService);
        factory.createCountryV3Service();
        verify(delegateFactory).createCountryV3Service();
    }

    @Test
    void createLongWeekendV3ServiceDelegatesToUnderlyingFactory() {
        when(delegateFactory.createLongWeekendV3Service()).thenReturn(longWeekendService);
        factory.createLongWeekendV3Service();
        verify(delegateFactory).createLongWeekendV3Service();
    }

    @Test
    void createPublicHolidayV3ServiceDelegatesToUnderlyingFactory() {
        when(delegateFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayService);
        factory.createPublicHolidayV3Service();
        verify(delegateFactory).createPublicHolidayV3Service();
    }
}
