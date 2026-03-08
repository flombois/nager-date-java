package com.github.flombois.commands;

import com.beust.jcommander.IStringConverter;
import com.neovisionaries.i18n.CountryCode;

import java.util.Arrays;
import java.util.List;

/**
 * JCommander converter that parses a comma-separated string of ISO 3166-1 alpha-2
 * country codes into a {@link List} of {@link CountryCode} values.
 * <p>
 * Example input: {@code "FR,DE,US"} produces {@code [CountryCode.FR, CountryCode.DE, CountryCode.US]}.
 * </p>
 *
 * @since 1.0
 */
public class CountryCodeListConverter implements IStringConverter<List<CountryCode>> {

    @Override
    public List<CountryCode> convert(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(CountryCode::valueOf)
                .toList();
    }
}
