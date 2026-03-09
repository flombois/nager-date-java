package com.github.flombois.executors;

import com.github.flombois.Context;
import com.github.flombois.models.WeekdayHolidayCount;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.services.NagerDateServiceException;
import com.github.flombois.services.v3.PublicHolidayV3Service;
import com.neovisionaries.i18n.CountryCode;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Service executor that counts weekday-only public holidays for multiple countries.
 * <p>
 * For each provided country code, fetches public holidays for the configured year,
 * excludes those falling on Saturday or Sunday, counts the remainder, and returns
 * the results sorted by count in descending order.
 * </p>
 *
 * @since 1.0
 */
public class GetWeekdayHolidays implements ServiceExecutor<List<WeekdayHolidayCount>> {

    private static final Predicate<PublicHolidayV3> isWeekday = h -> {
        final DayOfWeek dayOfWeek = h.getDate().getDayOfWeek();
        return !(DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek));
    };


    private final PublicHolidayV3Service publicHolidayV3Service;
    private final Set<CountryCode> countryCodes;

    /**
     * Constructs a GetWeekdayHolidays executor.
     *
     * @param publicHolidayV3Service the public holiday service to delegate to, must not be null
     * @param countryCodes           the set of country codes to query, must not be null
     * @throws NullPointerException if any argument is null
     */
    public GetWeekdayHolidays(PublicHolidayV3Service publicHolidayV3Service, Set<CountryCode> countryCodes) {
        Objects.requireNonNull(publicHolidayV3Service);
        Objects.requireNonNull(countryCodes);
        this.publicHolidayV3Service = publicHolidayV3Service;
        this.countryCodes = countryCodes;
    }

    @Override
    public List<WeekdayHolidayCount> callService(Context context) throws NagerDateServiceException {
        final List<WeekdayHolidayCount> results = new ArrayList<>();

        for (CountryCode countryCode : countryCodes) {
            Set<PublicHolidayV3> holidays = publicHolidayV3Service.getPublicHolidays(countryCode, context.year());
            int weekdayCount = (int) holidays.stream().filter(isWeekday).count();
            results.add(new WeekdayHolidayCount(countryCode, weekdayCount));
        }

        results.sort(Comparator.comparingInt(WeekdayHolidayCount::count).reversed());
        return results;
    }

}
