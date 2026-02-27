package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPublicHolidayTest {

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Mock
    Context context;

    @Test
    void callServiceDelegatesToGetPublicHolidays() throws NagerDateServiceException {
        Set<PublicHolidayV3> expected = Set.of();
        when(context.countryCode()).thenReturn(CountryCode.US);
        when(context.year()).thenReturn(Year.of(2025));
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.US, Year.of(2025))).thenReturn(expected);

        var executor = new GetPublicHoliday(publicHolidayV3Service);
        var result = executor.callService(context);

        assertSame(expected, result);
        verify(publicHolidayV3Service).getPublicHolidays(CountryCode.US, Year.of(2025));
    }

    @Test
    void setterRejectsNull() {
        var executor = new GetPublicHoliday(publicHolidayV3Service);
        assertThrows(NullPointerException.class, () -> executor.setPublicHolidayV3Service(null));
    }
}
