package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.LongWeekendV3Service;
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
class GetLongWeekendTest {

    @Mock
    LongWeekendV3Service longWeekendV3Service;

    @Mock
    Context context;

    @Test
    void callServiceDelegatesToGetLongWeekend() throws NagerDateServiceException {
        Set<LongWeekendV3> expected = Set.of();
        when(context.countryCode()).thenReturn(CountryCode.DE);
        when(context.year()).thenReturn(Year.of(2025));
        when(longWeekendV3Service.getLongWeekend(CountryCode.DE, Year.of(2025))).thenReturn(expected);

        var executor = new GetLongWeekend(longWeekendV3Service);
        var result = executor.callService(context);

        assertSame(expected, result);
        verify(longWeekendV3Service).getLongWeekend(CountryCode.DE, Year.of(2025));
    }

    @Test
    void setterRejectsNull() {
        var executor = new GetLongWeekend(longWeekendV3Service);
        assertThrows(NullPointerException.class, () -> executor.setLongWeekendV3Service(null));
    }
}
