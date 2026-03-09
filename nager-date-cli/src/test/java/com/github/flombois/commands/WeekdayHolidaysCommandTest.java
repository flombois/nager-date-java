package com.github.flombois.commands;

import com.github.flombois.commands.Command.WeekdayHolidaysCommand;
import com.github.flombois.executors.GetWeekdayHolidays;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.printers.PrintableWeekdayHolidayCountList;
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
class WeekdayHolidaysCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Test
    void nameReturnsWeekdayHolidays() {
        assertEquals("weekday-holidays", WeekdayHolidaysCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintableWeekdayHolidayCountList() {
        var spy = spy(WeekdayHolidaysCommand.INSTANCE);
        var result = spy.mapToPrintableRecord(List.of());
        assertInstanceOf(PrintableWeekdayHolidayCountList.class, result);
    }

    @Test
    void getServiceExecutorReturnsGetWeekdayHolidays() {
        var spy = spy(WeekdayHolidaysCommand.INSTANCE);
        spy.countryCodes = "FR,DE";
        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);
        var executor = spy.getServiceExecutor(servicesFactory);
        assertInstanceOf(GetWeekdayHolidays.class, executor);
    }

    @Test
    void executeFullFlow() throws NagerDateServiceException {
        var spy = spy(WeekdayHolidaysCommand.INSTANCE);
        spy.countryCodes = "FR,DE";

        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);
        doReturn(servicesFactory).when(spy).getServicesFactory();

        var holiday = new PublicHolidayV3();
        holiday.setDate(LocalDate.of(2026, 1, 1));
        holiday.setName("New Year's Day");
        holiday.setLocalName("Jour de l'an");

        when(publicHolidayV3Service.getPublicHolidays(any(CountryCode.class), any(Year.class)))
                .thenReturn(Set.of(holiday));

        spy.execute();

        verify(publicHolidayV3Service, times(2)).getPublicHolidays(any(CountryCode.class), any(Year.class));
    }
}
