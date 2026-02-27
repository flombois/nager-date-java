package com.github.flombois.printers;

import com.github.flombois.OutputFormat;
import com.github.flombois.models.CountryInfo;
import com.github.flombois.models.CountryInfoWithBorders;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.flombois.OutputFormat.JSON;
import static com.github.flombois.OutputFormat.PLAIN;

/**
 * Printable wrapper for a {@link CountryInfoWithBorders} record.
 * <p>
 * Supports JSON and plain text output. Plain text produces a human-readable
 * sentence describing the country, its official name, region, and border countries.
 * </p>
 *
 * @param record the country info with borders to wrap, must not be null
 * @since 1.0
 */
public record PrintableCountryInfoWithBorders(
        CountryInfoWithBorders record) implements PrintableRecord<CountryInfoWithBorders> {

    public PrintableCountryInfoWithBorders {
        Objects.requireNonNull(record);
    }

    @Override
    public Set<OutputFormat> supportedOutputFormats() {
        return Set.of(JSON, PLAIN);
    }

    @Override
    public String toPlainString() {
        return "%s (%s)".formatted(record.getCommonName(), record.getCountryCode()) +
                ", officially %s".formatted(record.getOfficialName()) +
                ", is located in %s.".formatted(record.getRegion()) +
                Optional.ofNullable(record.getBorders())
                        .filter(b -> !b.isEmpty())
                        .map(b -> {
                            var names = b.stream()
                                    .map(CountryInfo::getCommonName)
                                    .collect(Collectors.joining(", "));
                            return " It has %d borders: %s.".formatted(b.size(), names);
                        })
                        .orElse(" It has no borders.");
    }
}
