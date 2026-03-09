package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.SharedHoliday;
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
class GetSharedHolidaysTest {

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Mock
    Context context;

    @Test
    void findsSharedDatesBetweenTwoCountries() throws NagerDateServiceException {
        when(context.year()).thenReturn(Year.of(2026));

        var frNewYear = holiday(LocalDate.of(2026, 1, 1), "Jour de l'An");
        var frBastille = holiday(LocalDate.of(2026, 7, 14), "Fête nationale");
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, Year.of(2026)))
                .thenReturn(Set.of(frNewYear, frBastille));

        var deNewYear = holiday(LocalDate.of(2026, 1, 1), "Neujahr");
        var deUnity = holiday(LocalDate.of(2026, 10, 3), "Tag der Deutschen Einheit");
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.DE, Year.of(2026)))
                .thenReturn(Set.of(deNewYear, deUnity));

        var executor = new GetSharedHolidays(publicHolidayV3Service, CountryCode.FR, CountryCode.DE);
        List<SharedHoliday> result = executor.callService(context);

        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2026, 1, 1), result.get(0).date());
        assertEquals("Jour de l'An", result.get(0).localName1());
        assertEquals("Neujahr", result.get(0).localName2());
    }

    @Test
    void returnsEmptyWhenNoSharedDates() throws NagerDateServiceException {
        when(context.year()).thenReturn(Year.of(2026));

        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, Year.of(2026)))
                .thenReturn(Set.of(holiday(LocalDate.of(2026, 7, 14), "Fête nationale")));
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.DE, Year.of(2026)))
                .thenReturn(Set.of(holiday(LocalDate.of(2026, 10, 3), "Tag der Deutschen Einheit")));

        var executor = new GetSharedHolidays(publicHolidayV3Service, CountryCode.FR, CountryCode.DE);
        List<SharedHoliday> result = executor.callService(context);

        assertTrue(result.isEmpty());
    }

    @Test
    void resultIsSortedByDateAscending() throws NagerDateServiceException {
        when(context.year()).thenReturn(Year.of(2026));

        var frH1 = holiday(LocalDate.of(2026, 5, 1), "Fête du Travail");
        var frH2 = holiday(LocalDate.of(2026, 1, 1), "Jour de l'An");
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.FR, Year.of(2026)))
                .thenReturn(Set.of(frH1, frH2));

        var deH1 = holiday(LocalDate.of(2026, 5, 1), "Tag der Arbeit");
        var deH2 = holiday(LocalDate.of(2026, 1, 1), "Neujahr");
        when(publicHolidayV3Service.getPublicHolidays(CountryCode.DE, Year.of(2026)))
                .thenReturn(Set.of(deH1, deH2));

        var executor = new GetSharedHolidays(publicHolidayV3Service, CountryCode.FR, CountryCode.DE);
        List<SharedHoliday> result = executor.callService(context);

        assertEquals(2, result.size());
        assertTrue(result.get(0).date().isBefore(result.get(1).date()));
    }

    @Test
    void constructorRejectsEqualCountryCodes() {
        assertThrows(IllegalArgumentException.class,
                () -> new GetSharedHolidays(publicHolidayV3Service, CountryCode.FR, CountryCode.FR));
    }

    @Test
    void constructorRejectsNullArguments() {
        assertThrows(NullPointerException.class,
                () -> new GetSharedHolidays(null, CountryCode.FR, CountryCode.DE));
        assertThrows(NullPointerException.class,
                () -> new GetSharedHolidays(publicHolidayV3Service, null, CountryCode.DE));
        assertThrows(NullPointerException.class,
                () -> new GetSharedHolidays(publicHolidayV3Service, CountryCode.FR, null));
    }

    private PublicHolidayV3 holiday(LocalDate date, String localName) {
        var h = new PublicHolidayV3();
        h.setDate(date);
        h.setName(localName);
        h.setLocalName(localName);
        return h;
    }
}
