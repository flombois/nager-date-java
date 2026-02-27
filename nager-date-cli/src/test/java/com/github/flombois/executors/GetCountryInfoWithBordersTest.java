package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.CountryV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCountryInfoWithBordersTest {

    @Mock
    CountryV3Service countryV3Service;

    @Mock
    Context context;

    @Test
    void callServiceDelegatesToGetCountryInfoWithBorders() throws NagerDateServiceException {
        var expected = new CountryInfoWithBorders();
        when(context.countryCode()).thenReturn(CountryCode.FR);
        when(countryV3Service.getCountryInfoWithBorders(CountryCode.FR)).thenReturn(expected);

        var executor = new GetCountryInfoWithBorders(countryV3Service);
        var result = executor.callService(context);

        assertSame(expected, result);
        verify(countryV3Service).getCountryInfoWithBorders(CountryCode.FR);
    }

    @Test
    void setterRejectsNull() {
        var executor = new GetCountryInfoWithBorders(countryV3Service);
        assertThrows(NullPointerException.class, () -> executor.setCountryV3Service(null));
    }
}
