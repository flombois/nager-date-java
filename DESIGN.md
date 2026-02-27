# Design Patterns

This document describes the main design patterns used across the project modules.

## Template Method

**Location:** `Command.ServiceInvocationCommand` (`nager-date-cli`)

The `execute()` method defines a fixed algorithm skeleton:
1. Build a `Context` from CLI parameters
2. Call the service via a `ServiceExecutor`
3. Map the result to a `PrintableRecord`
4. Print in the requested output format

Subclasses (`PublicHolidayCommand`, `LongWeekendCommand`, `CountryInfoCommand`, `ListAllCountriesCommand`) override the abstract `getServiceExecutor()` and `mapToPrintableRecord()` methods to customize specific steps without changing the overall flow.

## Strategy

**Location:** `OutputFormat` enum (`nager-date-cli`), `ResponseHandler` interface (`nager-date-http-client`)

`OutputFormat` defines three strategies for rendering output (`JSON`, `PLAIN`, `TABLE`), each overriding an abstract `print()` method with format-specific behavior.

`ResponseHandler<T>` dispatches HTTP responses to strategy methods (`success()`, `noContent()`, `badRequest()`, `notFound()`) based on status code, letting each service implementation decide how to handle different outcomes.

## Command

**Location:** `ServiceExecutor` interface and implementations (`nager-date-cli`)

Each API operation is encapsulated as a `ServiceExecutor<T>` object with a single `callService()` method. Concrete implementations (`ListAllCountries`, `GetPublicHoliday`, `GetLongWeekend`, `GetCountryInfoWithBorders`) each wrap a specific service call, decoupling invocation from execution.

## Factory

**Location:** `ServicesFactory` / `HttpServicesFactory` (`nager-date-cli`)

`ServicesFactory` defines factory methods for creating service instances. `HttpServicesFactory` is the concrete implementation that creates HTTP-backed services from a `NagerDateHttpClient`, abstracting away the transport layer.

## Singleton

**Location:** `JsonMapper` (`nager-date-http-client`)

`JsonMapper` uses a private constructor and a static `INSTANCE` field exposed via `getInstance()`. This ensures a single, shared `ObjectMapper` configuration across the application.

## Adapter

**Location:** `PrintableRecord<T>` interface and implementations (`nager-date-cli`)

`PrintableRecord<T>` wraps raw data objects (e.g. `Set<PublicHolidayV3>`) and adapts them to multiple output representations (`toJsonString()`, `toPlainString()`, `toTableString()`). Each implementation translates domain data into a format the output layer can consume.

## Default Method Overloading

**Location:** Service interfaces (`CountryV3Service`, `LongWeekendV3Service`, `PublicHolidayV3Service`) in `nager-date-api`

Service interfaces define a core abstract method and use default methods to provide convenience overloads and `Optional`-based alternatives. Implementations only need to implement the core method and automatically gain the helper variants.

## Module Overview

```
nager-date-api            Service interfaces, models (Strategy via default methods)
nager-date-http-client    HTTP transport layer        (Singleton, Strategy)
nager-date-cli            CLI application             (Template Method, Command, Factory,
                                                       Adapter, Strategy)
```
