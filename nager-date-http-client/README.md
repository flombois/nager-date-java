# nager-date-http-client

HTTP implementation of the API service interfaces using `java.net.http.HttpClient`. Handles request construction, JSON deserialization, and error responses.

## Dependencies

| Dependency | Scope | Purpose |
|---|---|---|
| `nager-date-api` | compile | Service interfaces and models |
| `jackson-databind` | compile | JSON deserialization |
| `jackson-datatype-jsr310` | compile | Java date/time type support |
| `wiremock` | test | HTTP server mocking |
| `mockito` | test | Unit test mocking |

## Package Structure

```
com.github.flombois
├── ApiCallException                  # Checked exception with HTTP response and ProblemDetails parsing
├── factories/
│   └── HttpServicesFactory           # ServicesFactory implementation creating HTTP-backed services
├── http/
│   ├── NagerDateHttpClient           # HTTP client wrapper (30s timeout, configurable base URL)
│   └── ResponseHandler               # Strategy interface for HTTP status code dispatching
├── mappers/
│   ├── JsonMapper                    # Singleton Jackson ObjectMapper (JavaTime, case-insensitive enums)
│   └── MappingException             # Runtime exception for deserialization failures
└── services/v3/
    ├── HttpCountryV3Service          # /AvailableCountries and /CountryInfo endpoints
    ├── HttpLongWeekendV3Service      # /LongWeekend endpoint with query parameters
    └── HttpPublicHolidayV3Service    # /PublicHolidays and /IsTodayPublicHoliday endpoints
```

## Key Classes

### NagerDateHttpClient

Central HTTP client wrapping `java.net.http.HttpClient`. Provides:
- Configurable base URL (defaults to `https://date.nager.at/api/v3`)
- 30-second connection and request timeouts
- Request execution with `ResponseHandler` callback

### JsonMapper

Singleton `ObjectMapper` configured with:
- `JavaTimeModule` for `LocalDate` support
- `ACCEPT_CASE_INSENSITIVE_ENUMS` for flexible enum parsing
- Deserialization to single objects and `Set` collections

### ResponseHandler

Strategy interface dispatching HTTP responses by status code (200, 204, 400, 404), enabling each service to handle responses appropriately.

## Testing

```bash
mvn test -pl nager-date-http-client
```
