package com.github.flombois.commands;

import com.github.flombois.commands.Command.CountryInfoCommand;
import com.github.flombois.executors.GetCountryInfoWithBorders;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.printers.PrintableCountryInfoWithBorders;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.CountryV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryInfoCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    CountryV3Service countryV3Service;

    @Test
    void nameReturnsCountry() {
        assertEquals("country", CountryInfoCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintableCountryInfoWithBorders() {
        var spy = spy(CountryInfoCommand.INSTANCE);
        var country = new CountryInfoWithBorders();
        country.setCommonName("Test");
        var result = spy.mapToPrintableRecord(country);
        assertInstanceOf(PrintableCountryInfoWithBorders.class, result);
    }

    @Test
    void getServiceExecutorReturnsGetCountryInfoWithBorders() {
        var spy = spy(CountryInfoCommand.INSTANCE);
        when(servicesFactory.createCountryV3Service()).thenReturn(countryV3Service);

        var executor = spy.getServiceExecutor(servicesFactory);

        assertInstanceOf(GetCountryInfoWithBorders.class, executor);
    }

    @Test
    void executeFullFlow() throws NagerDateServiceException {
        var spy = spy(CountryInfoCommand.INSTANCE);

        when(servicesFactory.createCountryV3Service()).thenReturn(countryV3Service);
        doReturn(servicesFactory).when(spy).getServicesFactory();

        var country = new CountryInfoWithBorders();
        country.setCommonName("France");
        country.setCountryCode(CountryCode.FR);
        country.setOfficialName("French Republic");
        country.setRegion("Europe");
        when(countryV3Service.getCountryInfoWithBorders(any())).thenReturn(country);

        spy.execute();

        verify(countryV3Service).getCountryInfoWithBorders(any());
    }
}
