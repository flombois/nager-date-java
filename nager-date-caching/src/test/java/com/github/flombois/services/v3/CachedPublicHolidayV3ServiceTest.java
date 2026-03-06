package com.github.flombois.services.v3;

import com.github.flombois.caching.caches.MapCache;
import com.github.flombois.caching.strategies.DefaultCachingStrategy;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedPublicHolidayV3ServiceTest {

    @Mock
    private PublicHolidayV3Service delegate;

    private CachedPublicHolidayV3Service service;

    @BeforeEach
    void setUp() {
        var strategy = new DefaultCachingStrategy<Set<PublicHolidayV3>>(new MapCache<>(new HashMap<>()));
        service = new CachedPublicHolidayV3Service(strategy, delegate);
    }

    @Test
    void getPublicHolidaysReturnsDelegateResult() throws NagerDateServiceException {
        Set<PublicHolidayV3> expected = Set.of();
        when(delegate.getPublicHolidays(CountryCode.FR, Year.of(2026))).thenReturn(expected);

        var result = service.getPublicHolidays(CountryCode.FR, Year.of(2026));
        assertSame(expected, result);
    }

    @Test
    void getPublicHolidaysCachesResult() throws NagerDateServiceException {
        Set<PublicHolidayV3> expected = Set.of();
        when(delegate.getPublicHolidays(CountryCode.FR, Year.of(2026))).thenReturn(expected);

        service.getPublicHolidays(CountryCode.FR, Year.of(2026));
        service.getPublicHolidays(CountryCode.FR, Year.of(2026));

        verify(delegate, times(1)).getPublicHolidays(CountryCode.FR, Year.of(2026));
    }

    @Test
    void differentParametersAreCachedSeparately() throws NagerDateServiceException {
        Set<PublicHolidayV3> y2025 = Set.of();
        Set<PublicHolidayV3> y2026 = Set.of();
        when(delegate.getPublicHolidays(CountryCode.FR, Year.of(2025))).thenReturn(y2025);
        when(delegate.getPublicHolidays(CountryCode.FR, Year.of(2026))).thenReturn(y2026);

        assertSame(y2025, service.getPublicHolidays(CountryCode.FR, Year.of(2025)));
        assertSame(y2026, service.getPublicHolidays(CountryCode.FR, Year.of(2026)));
    }

    @Test
    void isTodayAPublicHolidayAlwaysDelegatesToService() throws NagerDateServiceException {
        when(delegate.isTodayAPublicHoliday(CountryCode.FR, "", 0)).thenReturn(true);

        assertTrue(service.isTodayAPublicHoliday(CountryCode.FR, "", 0));
        assertTrue(service.isTodayAPublicHoliday(CountryCode.FR, "", 0));

        verify(delegate, times(2)).isTodayAPublicHoliday(CountryCode.FR, "", 0);
    }

    @Test
    void isTodayAPublicHolidayIsNotCached() throws NagerDateServiceException {
        when(delegate.isTodayAPublicHoliday(CountryCode.FR, "", 0))
                .thenReturn(true)
                .thenReturn(false);

        assertTrue(service.isTodayAPublicHoliday(CountryCode.FR, "", 0));
        assertFalse(service.isTodayAPublicHoliday(CountryCode.FR, "", 0));
    }
}
