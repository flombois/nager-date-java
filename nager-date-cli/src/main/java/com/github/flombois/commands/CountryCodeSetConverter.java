package com.github.flombois.commands;

import com.beust.jcommander.IStringConverter;
import com.neovisionaries.i18n.CountryCode;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JCommander converter that parses a comma-separated string of ISO 3166-1 alpha-2
 * country codes into a {@link Set} of {@link CountryCode} values, discarding duplicates.
 * <p>
 * Example input: {@code "FR,DE,US"} produces {@code {CountryCode.FR, CountryCode.DE, CountryCode.US}}.
 * Insertion order is preserved via {@link LinkedHashSet}.
 * </p>
 *
 * @since 1.0
 */
public class CountryCodeSetConverter implements IStringConverter<Set<CountryCode>> {

    @Override
    public Set<CountryCode> convert(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(CountryCode::valueOf)
                .collect(Collectors.toSet());
    }
}
