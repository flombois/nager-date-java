package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.CountryV3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAllCountriesTest {

    @Mock
    CountryV3Service countryV3Service;

    @Mock
    Context context;

    @Test
    void callServiceDelegatesToGetAllCountries() throws NagerDateServiceException {
        Set<CountryV3> expected = Set.of(new CountryV3());
        when(countryV3Service.getAllCountries()).thenReturn(expected);

        var executor = new ListAllCountries(countryV3Service);
        Set<CountryV3> result = executor.callService(context);

        assertSame(expected, result);
        verify(countryV3Service).getAllCountries();
    }

    @Test
    void constructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new ListAllCountries(null));
    }

    @Test
    void setterRejectsNull() {
        var executor = new ListAllCountries(countryV3Service);
        assertThrows(NullPointerException.class, () -> executor.setCountryV3Service(null));
    }
}
