package com.github.flombois.services.v3;

import com.github.flombois.http.NagerDateHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NagerDateServiceNameTest {

    @Mock
    NagerDateHttpClient httpClient;

    @Test
    void httpCountryV3ServiceReturnsFullyQualifiedName() {
        var service = new HttpCountryV3Service(httpClient);
        assertEquals("com.github.flombois.services.v3.HttpCountryV3Service", service.name());
    }

    @Test
    void httpLongWeekendV3ServiceReturnsFullyQualifiedName() {
        var service = new HttpLongWeekendV3Service(httpClient);
        assertEquals("com.github.flombois.services.v3.HttpLongWeekendV3Service", service.name());
    }

    @Test
    void httpPublicHolidayV3ServiceReturnsFullyQualifiedName() {
        var service = new HttpPublicHolidayV3Service(httpClient);
        assertEquals("com.github.flombois.services.v3.HttpPublicHolidayV3Service", service.name());
    }
}
