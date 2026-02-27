package com.github.flombois.factories;

import com.github.flombois.http.NagerDateHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HttpServicesFactoryTest {

    @Mock
    NagerDateHttpClient httpClient;

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new HttpServicesFactory(null));
    }

    @Test
    void createCountryV3ServiceReturnsNonNull() {
        var factory = new HttpServicesFactory(httpClient);
        assertNotNull(factory.createCountryV3Service());
    }

    @Test
    void createLongWeekendV3ServiceReturnsNonNull() {
        var factory = new HttpServicesFactory(httpClient);
        assertNotNull(factory.createLongWeekendV3Service());
    }

    @Test
    void createPublicHolidayV3ServiceReturnsNonNull() {
        var factory = new HttpServicesFactory(httpClient);
        assertNotNull(factory.createPublicHolidayV3Service());
    }
}
