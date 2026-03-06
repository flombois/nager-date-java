package com.github.flombois.services.v3;

import com.github.flombois.caching.caches.MapCache;
import com.github.flombois.caching.strategies.DefaultCachingStrategy;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedCountryV3ServiceTest {

    @Mock
    private CountryV3Service delegate;

    private CachedCountryV3Service service;

    @BeforeEach
    void setUp() {
        var countryInfoStrategy = new DefaultCachingStrategy<CountryInfoWithBorders>(new MapCache<>(new HashMap<>()));
        var countrySetStrategy = new DefaultCachingStrategy<Set<CountryV3>>(new MapCache<>(new HashMap<>()));
        service = new CachedCountryV3Service(countryInfoStrategy, countrySetStrategy, delegate);
    }

    @Test
    void getCountryInfoWithBordersReturnsDelegateResult() throws NagerDateServiceException {
        var expected = new CountryInfoWithBorders();
        when(delegate.getCountryInfoWithBorders(CountryCode.FR)).thenReturn(expected);

        var result = service.getCountryInfoWithBorders(CountryCode.FR);
        assertSame(expected, result);
    }

    @Test
    void getCountryInfoWithBordersCachesResult() throws NagerDateServiceException {
        var expected = new CountryInfoWithBorders();
        when(delegate.getCountryInfoWithBorders(CountryCode.FR)).thenReturn(expected);

        service.getCountryInfoWithBorders(CountryCode.FR);
        service.getCountryInfoWithBorders(CountryCode.FR);

        verify(delegate, times(1)).getCountryInfoWithBorders(CountryCode.FR);
    }

    @Test
    void getCountryInfoWithBordersCachesByCountryCode() throws NagerDateServiceException {
        var france = new CountryInfoWithBorders();
        var germany = new CountryInfoWithBorders();
        when(delegate.getCountryInfoWithBorders(CountryCode.FR)).thenReturn(france);
        when(delegate.getCountryInfoWithBorders(CountryCode.DE)).thenReturn(germany);

        assertSame(france, service.getCountryInfoWithBorders(CountryCode.FR));
        assertSame(germany, service.getCountryInfoWithBorders(CountryCode.DE));
        verify(delegate, times(1)).getCountryInfoWithBorders(CountryCode.FR);
        verify(delegate, times(1)).getCountryInfoWithBorders(CountryCode.DE);
    }

    @Test
    void getAllCountriesReturnsDelegateResult() throws NagerDateServiceException {
        Set<CountryV3> expected = Set.of();
        when(delegate.getAllCountries()).thenReturn(expected);

        var result = service.getAllCountries();
        assertSame(expected, result);
    }

    @Test
    void getAllCountriesCachesResult() throws NagerDateServiceException {
        Set<CountryV3> expected = Set.of();
        when(delegate.getAllCountries()).thenReturn(expected);

        service.getAllCountries();
        service.getAllCountries();

        verify(delegate, times(1)).getAllCountries();
    }
}
