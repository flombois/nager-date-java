package com.github.flombois.factories;

import com.github.flombois.services.v3.CountryV3Service;
import com.github.flombois.services.v3.LongWeekendV3Service;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForwardCacheServiceFactoryTest {

    @Mock
    private ServicesFactory delegateFactory;

    @Mock
    private CountryV3Service countryService;

    @Mock
    private LongWeekendV3Service longWeekendService;

    @Mock
    private PublicHolidayV3Service publicHolidayService;

    private ForwardCacheServiceFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ForwardCacheServiceFactory(delegateFactory);
    }

    @Test
    void createCountryV3ServiceDelegatesToUnderlyingFactory() {
        when(delegateFactory.createCountryV3Service()).thenReturn(countryService);
        assertNotNull(factory.createCountryV3Service());
        verify(delegateFactory).createCountryV3Service();
    }

    @Test
    void createLongWeekendV3ServiceDelegatesToUnderlyingFactory() {
        when(delegateFactory.createLongWeekendV3Service()).thenReturn(longWeekendService);
        assertNotNull(factory.createLongWeekendV3Service());
        verify(delegateFactory).createLongWeekendV3Service();
    }

    @Test
    void createPublicHolidayV3ServiceDelegatesToUnderlyingFactory() {
        when(delegateFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayService);
        assertNotNull(factory.createPublicHolidayV3Service());
        verify(delegateFactory).createPublicHolidayV3Service();
    }
}
