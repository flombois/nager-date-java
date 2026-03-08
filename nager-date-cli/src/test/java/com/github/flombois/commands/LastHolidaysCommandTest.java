package com.github.flombois.commands;

import com.github.flombois.commands.Command.LastHolidaysCommand;
import com.github.flombois.executors.GetLastHolidays;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.printers.PrintableLastHolidayList;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
}
