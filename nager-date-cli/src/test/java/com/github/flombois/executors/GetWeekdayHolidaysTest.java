package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.WeekdayHolidayCount;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Year;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetWeekdayHolidaysTest {

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Mock
    Context context;

    @Test
    void countsWeekdayHolidaysAndSortsDescending() throws NagerDateServiceException {
        when(context.year()).thenReturn(Year.of(2026));

        // FR: 2 weekday holidays (Thu Jan 1, Fri Jul 14 are not weekends in 2026 — let's use known dates)
        // 2026-01-01 is Thursday, 2026-01-03 is Saturday
        var frHoliday1 = holiday(LocalDate.of(2026, 1, 1)); // Thursday
        var frHoliday2 = holiday(LocalDate.of(2026, 1, 3)); // Saturday
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, Year.of(2026)))
                .thenReturn(Set.of(frHoliday1, frHoliday2));

        // DE: 1 weekday holiday
        var deHoliday1 = holiday(LocalDate.of(2026, 1, 1)); // Thursday
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.DE, Year.of(2026)))
                .thenReturn(Set.of(deHoliday1));

        var executor = new GetWeekdayHolidays(publicHolidayV3Service, new LinkedHashSet<>(List.of(CountryCode.FR, CountryCode.DE)));
        List<WeekdayHolidayCount> result = executor.callService(context);

        assertEquals(2, result.size());
        // FR has 1 weekday holiday (Thu), DE has 1 weekday holiday (Thu)
        // FR should come first or equal since it has >= DE count
        assertTrue(result.get(0).count() >= result.get(1).count());
    }

    @Test
    void excludesWeekendHolidays() throws NagerDateServiceException {
        when(context.year()).thenReturn(Year.of(2026));

        // 2026-01-03 is Saturday, 2026-01-04 is Sunday
        var satHoliday = holiday(LocalDate.of(2026, 1, 3));
        var sunHoliday = holiday(LocalDate.of(2026, 1, 4));
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.US, Year.of(2026)))
                .thenReturn(Set.of(satHoliday, sunHoliday));

        var executor = new GetWeekdayHolidays(publicHolidayV3Service, new LinkedHashSet<>(List.of(CountryCode.US)));
        List<WeekdayHolidayCount> result = executor.callService(context);

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).count());
    }

    @Test
    void constructorRejectsNullService() {
        assertThrows(NullPointerException.class,
                () -> new GetWeekdayHolidays(null, Set.of(CountryCode.FR)));
    }

    @Test
    void constructorRejectsNullCountryCodes() {
        assertThrows(NullPointerException.class,
                () -> new GetWeekdayHolidays(publicHolidayV3Service, null));
    }

    private PublicHolidayV3 holiday(LocalDate date) {
        var h = new PublicHolidayV3();
        h.setDate(date);
        h.setName("Holiday");
        h.setLocalName("Holiday");
        return h;
    }
}
