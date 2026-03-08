package com.github.flombois.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.github.flombois.Context;
import com.github.flombois.OutputFormat;
import com.github.flombois.executors.GetCountryInfoWithBorders;
import com.github.flombois.executors.GetLastHolidays;
import com.github.flombois.executors.GetLongWeekend;
import com.github.flombois.executors.GetPublicHoliday;
import com.github.flombois.executors.GetSharedHolidays;
import com.github.flombois.executors.GetWeekdayHolidays;
import com.github.flombois.executors.ListAllCountries;
import com.github.flombois.executors.ServiceExecutor;
import com.github.flombois.logging.LoggerConfiguration;
import com.github.flombois.factories.CachedServicesFactory;
import com.github.flombois.factories.HttpServicesFactory;
import com.github.flombois.factories.NullCacheFactory;
import com.github.flombois.factories.ServicesFactory;
import com.github.flombois.factories.caches.CacheFactory;
import com.github.flombois.factories.caches.FileSystemCacheFactory;
import com.github.flombois.factories.caches.HashMapCacheFactory;
import com.github.flombois.http.NagerDateHttpClient;
import com.github.flombois.models.CountryInfoWithBorders;
import com.github.flombois.models.SharedHoliday;
import com.github.flombois.models.WeekdayHolidayCount;
import com.github.flombois.printers.PrintableCountryInfoWithBorders;
import com.github.flombois.models.v3.CountryV3;
import com.github.flombois.models.v3.LongWeekendV3;
import com.github.flombois.models.v3.PublicHolidayV3;
import com.github.flombois.printers.PrintableCountrySet;
import com.github.flombois.printers.PrintableLongWeekendSet;
import com.github.flombois.printers.PrintableLastHolidayList;
import com.github.flombois.printers.PrintablePublicHolidaySet;
import com.github.flombois.printers.PrintableRecord;
import com.github.flombois.printers.PrintableSharedHolidayList;
import com.github.flombois.printers.PrintableWeekdayHolidayCountList;
import com.github.flombois.services.NagerDateServiceException;
import com.neovisionaries.i18n.CountryCode;

import java.nio.file.Path;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Base command class defining global CLI parameters shared across all subcommands.
 * <p>
 * Contains common options such as output format, subdivision, offset, and base URL.
 * Subcommands extend {@link ServiceInvocationCommand} to implement specific API interactions.
 * </p>
 *
 * @since 1.0
 */
public class Command {

    @Parameter(names = {"-h", "--help"}, help = true, description = "Prints this help message")
    public boolean help;

    @Parameter(names = {"-v", "--version"}, description = "Prints the version")
    public boolean version;

    @Parameter(names = {"-f", "--format"}, description = "Output format, available: PLAIN, JSON, TABLE", defaultValueDescription = "PLAIN")
    public OutputFormat outputFormat = OutputFormat.PLAIN;

    @Parameter(names = {"-d", "--subdivision"}, description = "Narrow the calculation to a specific federal state, province, or subdivision (where supported).")
    public String subdivision = "";

    @Parameter(names = {"-o", "--offset"}, description = "Optional. UTC timezone offset in hours (range: -12 to +12).", defaultValueDescription = "default: 0")
    public int offset = 0;

    @Parameter(names = {"-b", "--available-bridge-days"}, description = "The maximum number of bridge days to include when determining long-weekend opportunities.", defaultValueDescription = "min: 1, max: 100, default: 1")
    public int availableBridgeDays = 1;

    @Parameter(names = {"-c", "--cache"}, description = "Enable in-memory caching of API responses")
    public boolean cache = false;

    @Parameter(names = {"--cache-fs"}, description = "Enable filesystem caching of API responses (persists across invocations)")
    public boolean cacheFs = false;

    @Parameter(names = {"--debug"}, description = "Enable debug logging")
    public boolean debug = false;

    @Parameter(names = {"-u", "--url"}, description = "API base url", defaultValueDescription = "Target public API by default (https://date.nager.at)")
    public String baseUrl = "";

    @Parameter(names = {"-y", "--year"}, description = "Year", defaultValueDescription = "default to current year", converter = YearConverter.class)
    public Year year = Year.now();

    @Parameter(names = {"-cc", "--country-code"}, description = "The 2-letter ISO 3166-1 country code (e.g., \"US\", \"GB\").", defaultValueDescription = "default to system set locale country code")
    public CountryCode countryCode = defaultCountryCode();

    // Try to get set country code from system locale, default to US if not set
    private static CountryCode defaultCountryCode() {
        String country = Locale.getDefault().getCountry(); // getDefault and getCountry never return null according to spec
        if (country.isEmpty()) {
            return CountryCode.US;
        }
        return CountryCode.valueOf(country);
    }

    /**
     * Returns the name of this command used for JCommander routing.
     *
     * @return the command name
     */
    public String name() {
        return "root";
    }

    /**
     * Abstract base class for commands that invoke a Nager.Date API service.
     * <p>
     * Implements the Template Method pattern: {@link #execute()} orchestrates
     * the workflow of building a context, executing a service, mapping the result
     * to a printable format, and printing it.
     * </p>
     *
     * @param <T> the type of the service result
     * @since 1.0
     */
    public static abstract class ServiceInvocationCommand<T> extends Command {

        private static final Logger LOGGER = Logger.getLogger(ServiceInvocationCommand.class.getName());

        /**
         * Executes the command by building a context, invoking the service,
         * mapping the result, and printing it in the requested format.
         *
         * @throws NagerDateServiceException if the API call fails
         */
        public void execute() throws NagerDateServiceException {

            // 0: Configure logging
            LoggerConfiguration.configure(debug);

            // 1: Initialize context
            final Context context = buildContext();
            LOGGER.info("Context: countryCode=%s, year=%s, format=%s, subdivision=%s, offset=%d, bridgeDays=%d"
                    .formatted(context.countryCode(), context.year(), context.outputFormat(),
                            context.subdivision(), context.offset(), context.availableBridgeDays()));

            // 2: Execute the service
            final T result = getServiceExecutor(getServicesFactory()).callService(context);

            // 3: Then map and print the result
            printResult(context, mapToPrintableRecord(result));
        }

        /**
         * Creates the services factory used to instantiate API services.
         * <p>
         * Wraps an {@link HttpServicesFactory} with a {@link CachedServicesFactory}.
         * When the {@code --cache} flag is set, uses {@link HashMapCacheFactory} for
         * in-memory caching; otherwise uses {@link NullCacheFactory} (no caching).
         * </p>
         *
         * @return the services factory
         */
        protected ServicesFactory getServicesFactory() {
            CacheFactory cacheFactory;
            if (cacheFs) {
                cacheFactory = new FileSystemCacheFactory(Path.of(".cache"));
                LOGGER.info("Cache: filesystem (.cache/)");
            } else if (cache) {
                cacheFactory = new HashMapCacheFactory();
                LOGGER.info("Cache: in-memory");
            } else {
                cacheFactory = new NullCacheFactory();
                LOGGER.info("Cache: disabled");
            }
            return new CachedServicesFactory(
                    new HttpServicesFactory(newNagerDateHttpClient()),
                    cacheFactory
            );
        }

        /**
         * Creates a new {@link NagerDateHttpClient} based on the provided CLI options.
         * <p>
         * Selects the appropriate constructor depending on whether a base URL
         * has been specified.
         * </p>
         *
         * @return the configured HTTP client
         */
        protected NagerDateHttpClient newNagerDateHttpClient() {
            final boolean hasBaseUrl = !Objects.isNull(baseUrl) && !baseUrl.isBlank();
            NagerDateHttpClient client = hasBaseUrl ? new NagerDateHttpClient(baseUrl) : new NagerDateHttpClient();
            LOGGER.info("Base URL: " + client.getBaseUrl());
            return client;
        }

        /**
         * Builds the execution context from the parsed CLI parameters.
         *
         * @return the command execution context
         */
        protected Context buildContext() {
            return new Context(countryCode, year, outputFormat, subdivision, offset, availableBridgeDays);
        }

        /**
         * Maps the raw service result to a printable record for output formatting.
         *
         * @param result the service result to wrap
         * @return the printable record
         */
        protected abstract PrintableRecord<T> mapToPrintableRecord(T result);

        /**
         * Prints the given printable record using the output format from the context.
         *
         * @param ctx             the execution context containing the output format
         * @param printableRecord the record to print
         */
        protected void printResult(Context ctx, PrintableRecord<T> printableRecord) {
            LOGGER.info("Output format: " + ctx.outputFormat());
            ctx.outputFormat().print(printableRecord);
        }

        /**
         * Returns the service executor responsible for calling the appropriate API service.
         *
         * @param serviceFactory the factory to create service instances
         * @return the service executor
         */
        protected abstract ServiceExecutor<T> getServiceExecutor(ServicesFactory serviceFactory);


    }

    /**
     * Subcommand that lists all available countries from the Nager.Date API.
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "Country related commands")
    public static class ListAllCountriesCommand extends ServiceInvocationCommand<Set<CountryV3>> {

        /** Singleton instance of this command. */
        public static final ListAllCountriesCommand INSTANCE = new ListAllCountriesCommand();

        private ListAllCountriesCommand() {}

        @Override
        public String name() {
            return "countries";
        }

        @Override
        protected PrintableRecord<Set<CountryV3>> mapToPrintableRecord(Set<CountryV3> result) {
            return new PrintableCountrySet(result);
        }

        @Override
        protected ServiceExecutor<Set<CountryV3>> getServiceExecutor(ServicesFactory factory) {
            return new ListAllCountries(factory.createCountryV3Service());
        }


    }

    /**
     * Subcommand that retrieves detailed country information including borders.
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "Country related commands")
    public static class CountryInfoCommand extends ServiceInvocationCommand<CountryInfoWithBorders> {

        /** Singleton instance of this command. */
        public static final CountryInfoCommand INSTANCE = new CountryInfoCommand();

        private CountryInfoCommand() {}

        @Override
        public String name() {
            return "country";
        }

        @Override
        protected PrintableRecord<CountryInfoWithBorders> mapToPrintableRecord(CountryInfoWithBorders result) {
            return new PrintableCountryInfoWithBorders(result);
        }

        @Override
        protected ServiceExecutor<CountryInfoWithBorders> getServiceExecutor(ServicesFactory factory) {
            return new GetCountryInfoWithBorders(factory.createCountryV3Service());
        }


    }

    /**
     * Subcommand that retrieves long weekend opportunities for a given country and year.
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "Long weekend related commands")
    public static class LongWeekendCommand extends ServiceInvocationCommand<Set<LongWeekendV3>> {

        /** Singleton instance of this command. */
        public static final LongWeekendCommand INSTANCE = new LongWeekendCommand();

        private LongWeekendCommand() {}

        @Override
        public String name() {
            return "long-weekend";
        }


        @Override
        protected PrintableRecord<Set<LongWeekendV3>> mapToPrintableRecord(Set<LongWeekendV3> result) {
            return new PrintableLongWeekendSet(result);
        }

        @Override
        protected ServiceExecutor<Set<LongWeekendV3>> getServiceExecutor(ServicesFactory factory) {
            return new GetLongWeekend(factory.createLongWeekendV3Service());
        }

    }

    /**
     * Subcommand that retrieves public holidays for a given country and year.
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "Public holiday related commands")
    public static class PublicHolidayCommand extends ServiceInvocationCommand<Set<PublicHolidayV3>> {

        /** Singleton instance of this command. */
        public static final PublicHolidayCommand INSTANCE = new PublicHolidayCommand();

        private PublicHolidayCommand() {}

        @Override
        public String name() {
            return "public-holiday";
        }


        @Override
        protected PrintableRecord<Set<PublicHolidayV3>> mapToPrintableRecord(Set<PublicHolidayV3> result) {
            return new PrintablePublicHolidaySet(result);
        }

        @Override
        protected ServiceExecutor<Set<PublicHolidayV3>> getServiceExecutor(ServicesFactory factory) {
            return new GetPublicHoliday(factory.createPublicHolidayV3Service());
        }

    }

    /**
     * Subcommand that retrieves the last 3 celebrated public holidays for a country.
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "Return the last 3 celebrated holidays (date and name)")
    public static class LastHolidaysCommand extends ServiceInvocationCommand<List<PublicHolidayV3>> {

        /** Singleton instance of this command. */
        public static final LastHolidaysCommand INSTANCE = new LastHolidaysCommand();

        private LastHolidaysCommand() {}

        @Override
        public String name() {
            return "last-holidays";
        }

        @Override
        protected PrintableRecord<List<PublicHolidayV3>> mapToPrintableRecord(List<PublicHolidayV3> result) {
            return new PrintableLastHolidayList(result);
        }

        @Override
        protected ServiceExecutor<List<PublicHolidayV3>> getServiceExecutor(ServicesFactory factory) {
            return new GetLastHolidays(factory.createPublicHolidayV3Service());
        }
    }

    /**
     * Subcommand that counts weekday-only public holidays for multiple countries.
     * <p>
     * Accepts a comma-separated list of country codes via the {@code -ccs} flag.
     * Results are sorted by count in descending order.
     * </p>
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "For each country, return the number of public holidays not falling on weekends (sorted descending)")
    public static class WeekdayHolidaysCommand extends ServiceInvocationCommand<List<WeekdayHolidayCount>> {

        /** Singleton instance of this command. */
        public static final WeekdayHolidaysCommand INSTANCE = new WeekdayHolidaysCommand();

        @Parameter(names = {"-ccs", "--country-codes"}, description = "Comma-separated list of ISO 3166-1 alpha-2 country codes (e.g., \"FR,DE,US\")",
                converter = CountryCodeListConverter.class, required = true)
        public List<CountryCode> countryCodes;

        private WeekdayHolidaysCommand() {}

        @Override
        public String name() {
            return "weekday-holidays";
        }

        @Override
        protected PrintableRecord<List<WeekdayHolidayCount>> mapToPrintableRecord(List<WeekdayHolidayCount> result) {
            return new PrintableWeekdayHolidayCountList(result);
        }

        @Override
        protected ServiceExecutor<List<WeekdayHolidayCount>> getServiceExecutor(ServicesFactory factory) {
            return new GetWeekdayHolidays(factory.createPublicHolidayV3Service(), countryCodes);
        }
    }

    /**
     * Subcommand that finds public holiday dates shared by two countries.
     * <p>
     * Accepts exactly 2 comma-separated country codes via the {@code -ccs} flag.
     * Returns deduplicated dates with local names from each country, sorted by date.
     * </p>
     *
     * @since 1.0
     */
    @Parameters(commandDescription = "Return the deduplicated list of dates celebrated in both countries (date + local names)")
    public static class SharedHolidaysCommand extends ServiceInvocationCommand<List<SharedHoliday>> {

        /** Singleton instance of this command. */
        public static final SharedHolidaysCommand INSTANCE = new SharedHolidaysCommand();

        @Parameter(names = {"-ccs", "--country-codes"}, description = "Exactly 2 comma-separated ISO 3166-1 alpha-2 country codes (e.g., \"FR,DE\")",
                converter = CountryCodeListConverter.class, required = true)
        public List<CountryCode> countryCodes;

        private SharedHolidaysCommand() {}

        @Override
        public String name() {
            return "shared-holidays";
        }

        @Override
        protected PrintableRecord<List<SharedHoliday>> mapToPrintableRecord(List<SharedHoliday> result) {
            return new PrintableSharedHolidayList(result);
        }

        @Override
        protected ServiceExecutor<List<SharedHoliday>> getServiceExecutor(ServicesFactory factory) {
            if (countryCodes.size() != 2) {
                throw new IllegalArgumentException("Exactly 2 country codes are required, got " + countryCodes.size());
            }
            return new GetSharedHolidays(factory.createPublicHolidayV3Service(),
                    countryCodes.get(0), countryCodes.get(1));
        }
    }

}
