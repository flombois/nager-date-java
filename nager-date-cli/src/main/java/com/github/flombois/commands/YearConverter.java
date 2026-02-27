package com.github.flombois.commands;

import com.beust.jcommander.IStringConverter;

import java.time.Year;

public class YearConverter implements IStringConverter<Year> {

    @Override
    public Year convert(String value) {
        return Year.parse(value);
    }
}
