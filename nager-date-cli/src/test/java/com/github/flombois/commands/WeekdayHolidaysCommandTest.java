package com.github.flombois.commands;

import com.github.flombois.commands.Command.WeekdayHolidaysCommand;
import com.github.flombois.executors.GetWeekdayHolidays;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.printers.PrintableWeekdayHolidayCountList;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
}
