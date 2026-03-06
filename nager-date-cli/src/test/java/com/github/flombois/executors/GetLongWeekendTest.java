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
    void callServiceForwardsAllContextParameters() throws NagerDateServiceException {
        Set<LongWeekendV3> expected = Set.of();
        when(context.countryCode()).thenReturn(CountryCode.DE);
        when(context.year()).thenReturn(Year.of(2025));
        when(context.availableBridgeDays()).thenReturn(3);
        when(context.subdivision()).thenReturn("DE-BY");
        when(longWeekendV3Service.getLongWeekend(CountryCode.DE, Year.of(2025), 3, "DE-BY")).thenReturn(expected);

        var executor = new GetLongWeekend(longWeekendV3Service);
        var result = executor.callService(context);

        assertSame(expected, result);
        verify(longWeekendV3Service).getLongWeekend(CountryCode.DE, Year.of(2025), 3, "DE-BY");
    }

    @Test
    void callServiceForwardsDefaultValues() throws NagerDateServiceException {
        Set<LongWeekendV3> expected = Set.of();
        when(context.countryCode()).thenReturn(CountryCode.US);
        when(context.year()).thenReturn(Year.of(2026));
        when(context.availableBridgeDays()).thenReturn(1);
        when(context.subdivision()).thenReturn("");
        when(longWeekendV3Service.getLongWeekend(CountryCode.US, Year.of(2026), 1, "")).thenReturn(expected);

        var executor = new GetLongWeekend(longWeekendV3Service);
        var result = executor.callService(context);

        assertSame(expected, result);
        verify(longWeekendV3Service).getLongWeekend(CountryCode.US, Year.of(2026), 1, "");
    }

    @Test
    void setterRejectsNull() {
        var executor = new GetLongWeekend(longWeekendV3Service);
        assertThrows(NullPointerException.class, () -> executor.setLongWeekendV3Service(null));
    }
}
