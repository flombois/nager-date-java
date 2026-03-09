# nager-date-cli

JCommander-based CLI application and entry point for the project. Produces a fat JAR with all dependencies shaded.

## Dependencies

| Dependency | Scope | Purpose |
|---|---|---|
| `nager-date-http-client` | compile | HTTP service implementations |
| `nager-date-caching` | compile | Caching decorator layer |
| `jcommander` | compile | CLI argument parsing |
| `jackson-databind` | compile | JSON output formatting |
| `jackson-datatype-jsr310` | compile | Java date/time support |
| `mockito` | test | Unit test mocking |

## Package Structure

```
com.github.flombois
├── App                               # Main entry point, JCommander dispatcher
├── Context                           # Record holding command execution parameters
├── OutputFormat                      # Enum: JSON, PLAIN, TABLE output strategies
├── commands/
│   ├── Command                       # Base class with global CLI options
│   │   ├── ServiceInvocationCommand<T> # Template Method: context -> service -> map -> print
│   │   ├── ListAllCountriesCommand   # `countries` subcommand
│   │   ├── CountryInfoCommand        # `country` subcommand
│   │   ├── LongWeekendCommand        # `long-weekend` subcommand
│   │   ├── PublicHolidayCommand      # `public-holiday` subcommand
│   │   ├── LastHolidaysCommand       # `last-holidays` subcommand
│   │   ├── WeekdayHolidaysCommand    # `weekday-holidays` subcommand
│   │   └── SharedHolidaysCommand     # `shared-holidays` subcommand
│   ├── CountryCodeSetConverter        # JCommander converter for comma-separated country codes
│   └── YearConverter                 # JCommander year string converter
├── executors/
│   ├── ServiceExecutor               # Functional interface for service invocation
│   ├── ListAllCountries              # Delegates to CountryV3Service.getAllCountries()
│   ├── GetCountryInfoWithBorders     # Delegates to CountryV3Service.getCountryInfoWithBorders()
│   ├── GetPublicHoliday              # Delegates to PublicHolidayV3Service.getPublicHolidays()
│   ├── GetLongWeekend                # Delegates to LongWeekendV3Service.getLongWeekend()
│   ├── GetLastHolidays               # Retrieves last 3 celebrated holidays for a country
│   ├── GetWeekdayHolidays            # Counts holidays not on weekends per country (sorted desc)
│   └── GetSharedHolidays             # Finds deduplicated holiday dates shared by 2 countries
├── models/
│   ├── SharedHoliday                 # Record for shared holiday output (date + local names)
│   └── WeekdayHolidayCount           # Record for weekday holiday count output (country + count)
└── printers/
    ├── PrintableRecord               # Interface for format-aware output
    ├── PrintableCountryV3             # Single country (JSON, PLAIN)
    ├── PrintableCountrySet            # Country set (JSON, PLAIN, TABLE)
    ├── PrintableCountryInfoWithBorders # Country with borders (JSON, PLAIN)
    ├── PrintablePublicHolidayV3       # Single holiday (JSON, PLAIN)
    ├── PrintablePublicHolidaySet      # Holiday set (JSON, PLAIN, TABLE)
    ├── PrintableLongWeekendV3         # Single long weekend (JSON, PLAIN)
    ├── PrintableLongWeekendSet        # Long weekend set (JSON, PLAIN, TABLE)
    ├── PrintableLastHolidayList       # Last 3 holidays list (JSON, PLAIN, TABLE)
    ├── PrintableWeekdayHolidayCountList # Weekday holiday counts (JSON, PLAIN, TABLE)
    ├── PrintableSharedHolidayList     # Shared holidays list (JSON, PLAIN, TABLE)
    └── TableFormatter                 # Fixed-width column table formatting utility
```

## Design

### Template Method Pattern

`ServiceInvocationCommand.execute()` defines the fixed execution flow:

1. Build `Context` from CLI arguments
2. Call service via `getServiceExecutor()`
3. Map result to `PrintableRecord` via `mapToPrintableRecord()`
4. Print using the selected `OutputFormat`

Subcommands override `getServiceExecutor()` and `mapToPrintableRecord()`.

### Cache Selection

The `--cache` and `--cache-fs` flags control caching behavior:

| Flags | Factory | Behavior |
|---|---|---|
| Neither | `NullCacheFactory` | No caching |
| `--cache` | `HashMapCacheFactory` | In-memory (per invocation) |
| `--cache-fs` | `FileSystemCacheFactory` | Persistent JSON files in `.cache/` |
| Both | `FileSystemCacheFactory` | `--cache-fs` takes priority |

### Debug Logging

The `--debug` flag enables `INFO`-level logging via `java.util.logging`. When enabled, the CLI logs:

- Resolved context (country code, year, format, subdivision, offset, bridge days)
- Cache factory selection (disabled, in-memory, filesystem)
- Base URL being used (public endpoint or custom)
- Output format used

Without `--debug`, only `SEVERE`-level messages are logged (e.g., corrupt cache files).

### Output Formats

The `OutputFormat` enum supports three formats via the `-f` flag:

- **PLAIN** - Human-readable key-value output
- **JSON** - Jackson-serialized JSON
- **TABLE** - Fixed-width column table (only for collection results)

## Testing

```bash
mvn test -pl nager-date-cli
```
