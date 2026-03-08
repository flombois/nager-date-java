package com.github.flombois.commands;

import com.github.flombois.commands.Command.SharedHolidaysCommand;
import com.github.flombois.executors.GetSharedHolidays;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.printers.PrintableSharedHolidayList;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SharedHolidaysCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    PublicHolidayV3Service publicHolidayV3Service;

    @Test
    void nameReturnsSharedHolidays() {
        assertEquals("shared-holidays", SharedHolidaysCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintableSharedHolidayList() {
        var spy = spy(SharedHolidaysCommand.INSTANCE);
        var result = spy.mapToPrintableRecord(List.of());
        assertInstanceOf(PrintableSharedHolidayList.class, result);
    }

    @Test
    void getServiceExecutorReturnsGetSharedHolidays() {
        var spy = spy(SharedHolidaysCommand.INSTANCE);
        spy.countryCodes = List.of(CountryCode.FR, CountryCode.DE);
        when(servicesFactory.createPublicHolidayV3Service()).thenReturn(publicHolidayV3Service);
        var executor = spy.getServiceExecutor(servicesFactory);
        assertInstanceOf(GetSharedHolidays.class, executor);
    }

    @Test
    void getServiceExecutorRejectsWrongNumberOfCountryCodes() {
        var spy = spy(SharedHolidaysCommand.INSTANCE);
        spy.countryCodes = List.of(CountryCode.FR);
        assertThrows(IllegalArgumentException.class, () -> spy.getServiceExecutor(servicesFactory));
    }

    @Test
    void getServiceExecutorRejectsThreeCountryCodes() {
        var spy = spy(SharedHolidaysCommand.INSTANCE);
        spy.countryCodes = List.of(CountryCode.FR, CountryCode.DE, CountryCode.US);
        assertThrows(IllegalArgumentException.class, () -> spy.getServiceExecutor(servicesFactory));
    }
}
