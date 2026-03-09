package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Service executor that retrieves the last {@value #LIMIT} celebrated public holidays for a country.
 * <p>
 * Always uses the current year (ignoring the {@code -y} flag). For each year,
 * filters holidays on or before today and appends them sorted by date descending.
 * If fewer than {@value #LIMIT} past holidays are found, continues fetching previous
 * years up to a maximum of {@value #MAX_ITERATIONS} iterations.
 * </p>
 *
 * @since 1.0
 */
public class GetLastHolidays implements ServiceExecutor<List<PublicHolidayV3>> {

    private static final int LIMIT = 3;
    private static final int MAX_ITERATIONS = 5;

    private final PublicHolidayV3Service publicHolidayV3Service;

    /**
     * Constructs a GetLastHolidays executor with the given public holiday service.
     *
     * @param publicHolidayV3Service the public holiday service to delegate to, must not be null
     * @throws NullPointerException if publicHolidayV3Service is null
     */
    public GetLastHolidays(PublicHolidayV3Service publicHolidayV3Service) {
        Objects.requireNonNull(publicHolidayV3Service);
        this.publicHolidayV3Service = publicHolidayV3Service;
    }

    @Override
    public List<PublicHolidayV3> callService(Context context) throws NagerDateServiceException {
        final LocalDate today = LocalDate.now();
        final CountryCode countryCode = context.countryCode();
        final List<PublicHolidayV3> result = new ArrayList<>();
        Year year = Year.now();
        int iterations = 0;

        while (result.size() < LIMIT && iterations < MAX_ITERATIONS) {
            // Filter past holidays from this year and add sorted descending
            getPublicHoliday(countryCode, year).stream()
                    .filter(h -> !h.getDate().isAfter(today))
                    .sorted(Comparator.comparing(PublicHolidayV3::getDate).reversed())
                    .limit(LIMIT - result.size())
                    .forEach(result::add);

            year = year.minusYears(1);
            iterations++;
        }

        return result;
    }

    private Set<PublicHolidayV3> getPublicHoliday(CountryCode code, Year year) throws NagerDateServiceException {
        return publicHolidayV3Service.getPublicHolidays(code, year);
    }
}
