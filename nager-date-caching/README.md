# nager-date-caching

Decorator layer that wraps service interfaces with caching behavior. Supports in-memory caching (HashMap-backed) and persistent filesystem caching (JSON files).

## Dependencies

| Dependency | Scope | Purpose |
|---|---|---|
| `nager-date-api` | compile | Service interfaces and models |
| `jackson-databind` | compile | JSON serialization for filesystem cache |
| `jackson-datatype-jsr310` | compile | Java date/time type support |
| `mockito` | test | Unit test mocking |

## Package Structure

```
com.github.flombois
├── caching/
│   ├── caches/
│   │   ├── Cache<T>                  # Generic cache interface (get/put/evict/clear)
│   │   ├── CacheEntry<T>            # Value wrapper for cached items
│   │   ├── TypedCacheEntry<T>       # CacheEntry subclass carrying type info for serialization
│   │   ├── MapCache<T>              # Map-backed cache implementation
│   │   ├── FileSystemCache<T>       # Persistent JSON file-based cache
│   │   └── NullCache<T>             # No-op cache (Null Object pattern)
│   └── strategies/
│       ├── CachingStrategy<T>        # Interface for cache key building and call interception
│       ├── DefaultCachingStrategy<T> # Cache-aside pattern (check cache -> miss -> store)
│       ├── FileSystemCachingStrategy<T> # SHA-256 key hashing for filesystem-safe filenames
│       └── ForwardWithoutCaching<T>  # Bypass strategy that skips caching entirely
├── factories/
│   ├── CachedServicesFactory         # Decorator factory wrapping services with caching
│   ├── ForwardCacheServiceFactory    # Convenience factory with caching disabled
│   └── caches/
│       ├── CacheFactory              # Abstract factory for creating Cache instances
│       ├── HashMapCacheFactory       # Creates HashMap-backed caches
│       ├── FileSystemCacheFactory    # Creates JSON file-backed caches
│       └── NullCacheFactory          # Creates NullCache instances
└── services/
    ├── CachedService<S,T>            # Abstract base for cached service decorators
    ├── CachedServiceException        # Exception wrapper for cache/service errors
    └── v3/
        ├── CachedCountryV3Service    # Cached country service (dual strategy for different return types)
        ├── CachedLongWeekendV3Service # Cached long weekend service
        └── CachedPublicHolidayV3Service # Cached public holiday service (selective per-method caching)
```

## Design

### Cache Abstraction

The `Cache<T>` interface is storage-agnostic. Three implementations are provided:

- **MapCache** - In-memory, backed by any `Map` implementation
- **FileSystemCache** - Persists entries as `.json` files in a configurable directory. Uses `TypedCacheEntry` to carry generic type information through Jackson serialization/deserialization
- **NullCache** - No-op implementation eliminating conditional logic when caching is disabled

### Caching Strategies

`CachingStrategy<T>` controls how cache keys are built and how service calls are intercepted:

- **DefaultCachingStrategy** - Cache-aside pattern: check cache, on miss call service, store result
- **FileSystemCachingStrategy** - Extends default strategy with SHA-256 key hashing (cache keys like `"MyService:[getHolidays, FR, 2026]"` contain characters invalid for filenames)
- **ForwardWithoutCaching** - Bypasses cache entirely, delegates directly to service

### Factory Chain

```
CacheFactory ──creates──> Cache<T>
CachedServicesFactory ──wraps──> ServicesFactory (from api module)
                      ──uses──> CacheFactory to create caches per service
```

`CachedServicesFactory` selects the appropriate `CachingStrategy` based on the `CacheFactory` type: `FileSystemCachingStrategy` for `FileSystemCacheFactory`, `DefaultCachingStrategy` otherwise.

## Testing

```bash
mvn test -pl nager-date-caching
```
