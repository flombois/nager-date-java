package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.SharedHoliday;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service executor that finds public holiday dates shared by two countries.
 * <p>
 * Fetches holidays for both countries, intersects by date, and returns a
 * deduplicated list of shared dates with local names from each country,
 * sorted by date ascending.
 * </p>
 *
 * @since 1.0
 */
public class GetSharedHolidays implements ServiceExecutor<List<SharedHoliday>> {

    private final PublicHolidayV3Service publicHolidayV3Service;
    private final CountryCode countryCode1;
    private final CountryCode countryCode2;

    /**
     * Constructs a GetSharedHolidays executor.
     *
     * @param publicHolidayV3Service the public holiday service to delegate to, must not be null
     * @param countryCode1           the first country code, must not be null
     * @param countryCode2           the second country code, must not be null
     * @throws NullPointerException if any argument is null
     */
    public GetSharedHolidays(PublicHolidayV3Service publicHolidayV3Service,
                             CountryCode countryCode1, CountryCode countryCode2) {
        Objects.requireNonNull(publicHolidayV3Service);
        Objects.requireNonNull(countryCode1);
        Objects.requireNonNull(countryCode2);
        this.publicHolidayV3Service = publicHolidayV3Service;
        this.countryCode1 = countryCode1;
        this.countryCode2 = countryCode2;
    }

    @Override
    public List<SharedHoliday> callService(Context context) throws NagerDateServiceException {
        Set<PublicHolidayV3> holidays1 = publicHolidayV3Service.getPublicHolidays(countryCode1, context.year());
        Set<PublicHolidayV3> holidays2 = publicHolidayV3Service.getPublicHolidays(countryCode2, context.year());

        // Index by date for the second country
        Map<LocalDate, String> dateToLocalName2 = holidays2.stream()
                .collect(Collectors.toMap(
                        PublicHolidayV3::getDate,
                        PublicHolidayV3::getLocalName,
                        (a, b) -> a + " / " + b  // merge if multiple holidays on same date
                ));

        return holidays1.stream()
                .filter(h -> dateToLocalName2.containsKey(h.getDate()))
                .map(h -> new SharedHoliday(h.getDate(), h.getLocalName(), dateToLocalName2.get(h.getDate())))
                .sorted(Comparator.comparing(SharedHoliday::date))
                .toList();
    }
}
