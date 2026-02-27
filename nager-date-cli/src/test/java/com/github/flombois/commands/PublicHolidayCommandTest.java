package com.github.flombois.commands;

import com.github.flombois.commands.Command.PublicHolidayCommand;
import com.github.flombois.executors.GetPublicHoliday;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.printers.PrintablePublicHolidaySet;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicHolidayCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Test
    void nameReturnsPublicHoliday() {
        assertEquals("public-holiday", PublicHolidayCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintablePublicHolidaySet() {
        var spy = spy(PublicHolidayCommand.INSTANCE);
        Set<PublicHolidayV3> data = Set.of();
        var result = spy.mapToPrintableRecord(data);
        assertInstanceOf(PrintablePublicHolidaySet.class, result);
    }

    @Test
    void getServiceExecutorReturnsGetPublicHoliday() {
        var spy = spy(PublicHolidayCommand.INSTANCE);
        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);

        var executor = spy.getServiceExecutor(servicesFactory);

        assertInstanceOf(GetPublicHoliday.class, executor);
    }

    @Test
    void executeFullFlow() throws NagerDateServiceException {
        var spy = spy(PublicHolidayCommand.INSTANCE);

        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);
        doReturn(servicesFactory).when(spy).getServicesFactory();

        var holiday = new PublicHolidayV3();
        holiday.setDate(LocalDate.of(2026, 1, 1));
        holiday.setName("New Year's Day");
        holiday.setLocalName("Jour de l'an");
        holiday.setFixed(true);
        holiday.setGlobal(true);

        when(publicHolidayV3Service.getPublicHolidays(any(CountryCode.class), any(Year.class)))
                .thenReturn(Set.of(holiday));

        spy.execute();

        verify(publicHolidayV3Service).getPublicHolidays(any(CountryCode.class), any(Year.class));
    }
}
