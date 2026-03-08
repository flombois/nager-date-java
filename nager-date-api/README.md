# nager-date-api

Core API module defining service interfaces, data models, and factory contracts. This module has **zero external dependencies** and serves as the foundation for all other modules.

## Package Structure

```
com.github.flombois
├── factories/
│   └── ServicesFactory             # Abstract factory for creating service instances
├── models/
│   ├── CountryInfo                 # Country metadata (name, code, region)
│   ├── CountryInfoWithBorders      # Country info with border countries
│   ├── HolidayType                 # Enum: PUBLIC, BANK, SCHOOL, AUTHORITIES, OPTIONAL, OBSERVANCE
│   ├── ProblemDetails              # RFC 7807 error response model
│   ├── VersionInfo                 # Application version info
│   └── v3/
│       ├── CountryV3               # Minimal country model (code + name)
│       ├── LongWeekendV3           # Long weekend with bridge day info
│       └── PublicHolidayV3         # Public holiday with types, counties, dates
└── services/
    ├── NagerDateService            # Base marker interface with name() method
    ├── NagerDateServiceException   # Checked exception for service errors
    └── v3/
        ├── CountryV3Service        # Country lookup and listing
        ├── LongWeekendV3Service    # Long weekend retrieval
        └── PublicHolidayV3Service  # Public holiday queries
```

## Key Interfaces

### ServicesFactory

Abstract factory that downstream modules implement to provide service instances:

```java
public interface ServicesFactory {
    CountryV3Service createCountryV3Service();
    LongWeekendV3Service createLongWeekendV3Service();
    PublicHolidayV3Service createPublicHolidayV3Service();
}
```

### Service Interfaces

Each V3 service interface defines methods for querying the Nager.Date API:

- **CountryV3Service** - `getAllCountries()`, `getCountryInfoWithBorders(countryCode)`
- **LongWeekendV3Service** - `getLongWeekend(countryCode, year, availableBridgeDays, subdivision)`
- **PublicHolidayV3Service** - `getPublicHolidays(countryCode, year)`, `isTodayAPublicHoliday(...)`

## Testing

No tests in this module. Service contracts are validated through implementation tests in downstream modules.
