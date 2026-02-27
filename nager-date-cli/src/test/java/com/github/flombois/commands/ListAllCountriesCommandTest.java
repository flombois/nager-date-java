package com.github.flombois.commands;

import com.github.flombois.commands.Command.ListAllCountriesCommand;
import com.github.flombois.executors.ListAllCountries;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.printers.PrintableCountrySet;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.CountryV3Service;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAllCountriesCommandTest {

    @Mock
    ServicesFactory servicesFactory;

    @Mock
    CountryV3Service countryV3Service;

    @Test
    void nameReturnsCountries() {
        assertEquals("countries", ListAllCountriesCommand.INSTANCE.name());
    }

    @Test
    void mapToPrintableRecordReturnsPrintableCountrySet() {
        var spy = spy(ListAllCountriesCommand.INSTANCE);
        Set<CountryV3> data = Set.of();
        var result = spy.mapToPrintableRecord(data);
        assertInstanceOf(PrintableCountrySet.class, result);
    }

    @Test
    void getServiceExecutorReturnsListAllCountries() {
        var spy = spy(ListAllCountriesCommand.INSTANCE);
        when(servicesFactory.createCountryV3Service()).thenReturn(countryV3Service);

        var executor = spy.getServiceExecutor(servicesFactory);

        assertInstanceOf(ListAllCountries.class, executor);
    }

    @Test
    void executeFullFlow() throws NagerDateServiceException {
        var spy = spy(ListAllCountriesCommand.INSTANCE);

        when(servicesFactory.createCountryV3Service()).thenReturn(countryV3Service);
        doReturn(servicesFactory).when(spy).getServicesFactory();

        var country = new CountryV3();
        country.setName("France");
        country.setCountryCode(CountryCode.FR);
        when(countryV3Service.getAllCountries()).thenReturn(Set.of(country));

        spy.execute();

        verify(countryV3Service).getAllCountries();
    }
}
