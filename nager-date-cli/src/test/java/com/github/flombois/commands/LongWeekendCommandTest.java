package com.github.flombois.commands;

import com.github.flombois.commands.Command.LongWeekendCommand;
import com.github.flombois.executors.GetLongWeekend;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.printers.PrintableLongWeekendSet;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.LongWeekendV3Service;
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
class LongWeekendCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    LongWeekendV3Service longWeekendV3Service;

    @Test
    void nameReturnsLongWeekend() {
        assertEquals("long-weekend", LongWeekendCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintableLongWeekendSet() {
        var spy = spy(LongWeekendCommand.INSTANCE);
        Set<LongWeekendV3> data = Set.of();
        var result = spy.mapToPrintableRecord(data);
        assertInstanceOf(PrintableLongWeekendSet.class, result);
    }

    @Test
    void getServiceExecutorReturnsGetLongWeekend() {
        var spy = spy(LongWeekendCommand.INSTANCE);
        when(servicesFactory.createLongWeekendV3Service()).thenReturn(longWeekendV3Service);

        var executor = spy.getServiceExecutor(servicesFactory);

        assertInstanceOf(GetLongWeekend.class, executor);
    }

    @Test
    void executeFullFlow() throws NagerDateServiceException {
        var spy = spy(LongWeekendCommand.INSTANCE);

        when(servicesFactory.createLongWeekendV3Service()).thenReturn(longWeekendV3Service);
        doReturn(servicesFactory).when(spy).getServicesFactory();

        var longWeekend = new LongWeekendV3();
        longWeekend.setStartDate(LocalDate.of(2026, 5, 1));
        longWeekend.setEndDate(LocalDate.of(2026, 5, 4));
        longWeekend.setDayCount(4);
        longWeekend.setNeedBridgeDay(true);

        when(longWeekendV3Service.getLongWeekend(any(CountryCode.class), any(Year.class), anyInt(), anyString()))
                .thenReturn(Set.of(longWeekend));

        spy.execute();

        verify(longWeekendV3Service).getLongWeekend(any(CountryCode.class), any(Year.class), anyInt(), anyString());
    }
}
