package com.github.flombois.services.v3;

import com.github.flombois.caching.caches.MapCache;
import com.github.flombois.caching.strategies.DefaultCachingStrategy;
import com.github.flombois.models.v3.LongWeekendV3;
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
class CachedLongWeekendV3ServiceTest {

    @Mock
    private LongWeekendV3Service delegate;

    private CachedLongWeekendV3Service service;

    @BeforeEach
    void setUp() {
        var strategy = new DefaultCachingStrategy<Set<LongWeekendV3>>(new MapCache<>(new HashMap<>()));
        service = new CachedLongWeekendV3Service(strategy, delegate);
    }

    @Test
    void getLongWeekendReturnsDelegateResult() throws NagerDateServiceException {
        Set<LongWeekendV3> expected = Set.of();
        when(delegate.getLongWeekend(CountryCode.FR, Year.of(2026), 1, "")).thenReturn(expected);

        var result = service.getLongWeekend(CountryCode.FR, Year.of(2026), 1, "");
        assertSame(expected, result);
    }

    @Test
    void getLongWeekendCachesResult() throws NagerDateServiceException {
        Set<LongWeekendV3> expected = Set.of();
        when(delegate.getLongWeekend(CountryCode.FR, Year.of(2026), 1, "")).thenReturn(expected);

        service.getLongWeekend(CountryCode.FR, Year.of(2026), 1, "");
        service.getLongWeekend(CountryCode.FR, Year.of(2026), 1, "");

        verify(delegate, times(1)).getLongWeekend(CountryCode.FR, Year.of(2026), 1, "");
    }

    @Test
    void differentParametersAreCachedSeparately() throws NagerDateServiceException {
        Set<LongWeekendV3> france = Set.of();
        Set<LongWeekendV3> germany = Set.of();
        when(delegate.getLongWeekend(CountryCode.FR, Year.of(2026), 1, "")).thenReturn(france);
        when(delegate.getLongWeekend(CountryCode.DE, Year.of(2026), 1, "")).thenReturn(germany);

        assertSame(france, service.getLongWeekend(CountryCode.FR, Year.of(2026), 1, ""));
        assertSame(germany, service.getLongWeekend(CountryCode.DE, Year.of(2026), 1, ""));
        verify(delegate, times(1)).getLongWeekend(CountryCode.FR, Year.of(2026), 1, "");
        verify(delegate, times(1)).getLongWeekend(CountryCode.DE, Year.of(2026), 1, "");
    }
}
