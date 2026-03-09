package com.github.flombois.commands;

import com.beust.jcommander.IStringConverter;

import java.time.Year;

/**
 * JCommander converter that parses a year string into a {@link Year} value.
 *
 * @since 1.0
 */
public class YearConverter implements IStringConverter<Year> {

    @Override
    public Year convert(String value) {
        return Year.parse(value);
    }
}
