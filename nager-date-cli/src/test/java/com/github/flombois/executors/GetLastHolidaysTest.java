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

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLastHolidaysTest {

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Mock
    Context context;

    @Test
    void returnsLast3HolidaysBeforeToday() throws NagerDateServiceException {
        when(context.countryCode()).thenReturn(CountryCode.FR);

        var h1 = holiday(LocalDate.now().minusDays(30), "Holiday 1");
        var h2 = holiday(LocalDate.now().minusDays(20), "Holiday 2");
        var h3 = holiday(LocalDate.now().minusDays(10), "Holiday 3");
        var h4 = holiday(LocalDate.now().minusDays(5), "Holiday 4");
        var future = holiday(LocalDate.now().plusDays(10), "Future Holiday");

        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, Year.now()))
                .thenReturn(Set.of(h1, h2, h3, h4, future));

        var executor = new GetLastHolidays(publicHolidayV3Service);
        List<PublicHolidayV3> result = executor.callService(context);

        assertEquals(3, result.size());
        // Should be sorted descending by date
        assertTrue(result.get(0).getDate().isAfter(result.get(1).getDate()));
        assertTrue(result.get(1).getDate().isAfter(result.get(2).getDate()));
        // Future holiday should not be included
        assertFalse(result.stream().anyMatch(h -> h.getName().equals("Future Holiday")));
    }

    @Test
    void fetchesPreviousYearWhenNotEnoughHolidays() throws NagerDateServiceException {
        when(context.countryCode()).thenReturn(CountryCode.FR);
        Year currentYear = Year.now();

        var h1 = holiday(LocalDate.now().minusDays(5), "Recent Holiday");

        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, currentYear))
                .thenReturn(Set.of(h1));
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, currentYear.minusYears(1)))
                .thenReturn(Set.of(
                        holiday(LocalDate.now().minusYears(1).minusDays(10), "Last Year 1"),
                        holiday(LocalDate.now().minusYears(1).minusDays(20), "Last Year 2")
                ));

        var executor = new GetLastHolidays(publicHolidayV3Service);
        List<PublicHolidayV3> result = executor.callService(context);

        assertEquals(3, result.size());
        verify(publicHolidayV3Service).getPublicHolidays(CountryCode.FR, currentYear.minusYears(1));
    }

    @Test
    void excludesFutureHolidays() throws NagerDateServiceException {
        when(context.countryCode()).thenReturn(CountryCode.FR);

        var future1 = holiday(LocalDate.now().plusDays(1), "Tomorrow");
        var future2 = holiday(LocalDate.now().plusDays(30), "Next Month");

        when(publicHolidayV3Service.getPublicHolidays(any(), any()))
                .thenReturn(Set.of(future1, future2));

        var executor = new GetLastHolidays(publicHolidayV3Service);
        List<PublicHolidayV3> result = executor.callService(context);

        assertTrue(result.isEmpty() || result.size() <= 3);
        result.forEach(h -> assertFalse(h.getDate().isAfter(LocalDate.now())));
    }

    @Test
    void includesTodaysHoliday() throws NagerDateServiceException {
        when(context.countryCode()).thenReturn(CountryCode.FR);

        var todayHoliday = holiday(LocalDate.now(), "Today's Holiday");
        var yesterday = holiday(LocalDate.now().minusDays(1), "Yesterday");
        var lastWeek = holiday(LocalDate.now().minusDays(7), "Last Week");

        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, Year.now()))
                .thenReturn(Set.of(todayHoliday, yesterday, lastWeek));

        var executor = new GetLastHolidays(publicHolidayV3Service);
        List<PublicHolidayV3> result = executor.callService(context);

        assertEquals(3, result.size());
        assertEquals(LocalDate.now(), result.get(0).getDate());
    }

    @Test
    void stopsAfterMaxIterations() throws NagerDateServiceException {
        when(context.countryCode()).thenReturn(CountryCode.FR);

        // All years return only future holidays — loop should stop after 5 iterations
        when(publicHolidayV3Service.getPublicHolidays(any(), any()))
                .thenReturn(Set.of(holiday(LocalDate.now().plusDays(1), "Future")));

        var executor = new GetLastHolidays(publicHolidayV3Service);
        List<PublicHolidayV3> result = executor.callService(context);

        assertTrue(result.isEmpty());
        verify(publicHolidayV3Service, times(5)).getPublicHolidays(any(), any());
    }

    @Test
    void returnsEmptyWhenNoHolidaysExist() throws NagerDateServiceException {
        when(context.countryCode()).thenReturn(CountryCode.FR);

        when(publicHolidayV3Service.getPublicHolidays(any(), any()))
                .thenReturn(Set.of());

        var executor = new GetLastHolidays(publicHolidayV3Service);
        List<PublicHolidayV3> result = executor.callService(context);

        assertTrue(result.isEmpty());
        verify(publicHolidayV3Service, times(5)).getPublicHolidays(any(), any());
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new GetLastHolidays(null));
    }

    private PublicHolidayV3 holiday(LocalDate date, String name) {
        var h = new PublicHolidayV3();
        h.setDate(date);
        h.setName(name);
        h.setLocalName(name + " (local)");
        return h;
    }
}
