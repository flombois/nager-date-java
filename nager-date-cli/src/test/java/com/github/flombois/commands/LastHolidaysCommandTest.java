package com.github.flombois.commands;

import com.github.flombois.commands.Command.LastHolidaysCommand;
import com.github.flombois.executors.GetLastHolidays;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.printers.PrintableLastHolidayList;
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
class LastHolidaysCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Test
    void nameReturnsLastHolidays() {
        assertEquals("last-holidays", LastHolidaysCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintableLastHolidayList() {
        var spy = spy(LastHolidaysCommand.INSTANCE);
        var result = spy.mapToPrintableRecord(List.of());
        assertInstanceOf(PrintableLastHolidayList.class, result);
    }

    @Test
    void getServiceExecutorReturnsGetLastHolidays() {
        var spy = spy(LastHolidaysCommand.INSTANCE);
        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);
        var executor = spy.getServiceExecutor(servicesFactory);
        assertInstanceOf(GetLastHolidays.class, executor);
    }

    @Test
    void executeFullFlow() throws NagerDateServiceException {
        var spy = spy(LastHolidaysCommand.INSTANCE);

        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);
        doReturn(servicesFactory).when(spy).getServicesFactory();

        var holiday = new PublicHolidayV3();
        holiday.setDate(LocalDate.of(2026, 1, 1));
        holiday.setName("New Year's Day");
        holiday.setLocalName("Jour de l'an");

        when(publicHolidayV3Service.getPublicHolidays(any(CountryCode.class), any(Year.class)))
                .thenReturn(Set.of(holiday));

        spy.execute();

        verify(publicHolidayV3Service, atLeastOnce()).getPublicHolidays(any(CountryCode.class), any(Year.class));
    }
}
