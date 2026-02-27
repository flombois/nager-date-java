# nager-date-cli

A command-line interface for the [Nager.Date API](https://date.nager.at), providing public holiday data, long weekend opportunities, and country information.

## Prerequisites

- Java 25+
- Maven 3.9+

## Build

```bash
mvn clean package
```

This produces a fat JAR at `nager-date-cli/target/nager-date-cli-1.0-SNAPSHOT.jar`.

## Usage

```bash
java -jar nager-date-cli/target/nager-date-cli-1.0-SNAPSHOT.jar <command> [options]
```

### Commands

| Command          | Description                              |
|------------------|------------------------------------------|
| `public-holiday` | List public holidays for a country/year  |
| `long-weekend`   | Find long weekend opportunities          |
| `countries`      | List all available countries             |
| `country`        | Get detailed country info with borders   |

### Global Options

| Option                          | Description                                | Default        |
|---------------------------------|--------------------------------------------|----------------|
| `-c, --country-code`            | ISO 3166-1 alpha-2 country code            | System locale  |
| `-y, --year`                    | Target year                                | Current year   |
| `-f, --format`                  | Output format: `PLAIN`, `JSON`, `TABLE`    | `PLAIN`        |
| `-d, --subdivision`             | Filter by federal state/province           |                |
| `-o, --offset`                  | UTC timezone offset (-12 to +12)           |                |
| `-b, --available-bridge-days`   | Max bridge days for long weekends (1-100)  | `1`            |
| `-u, --url`                     | Custom API base URL                        | date.nager.at  |
| `-k, --key`                     | API key for authentication                 |                |
| `-h, --help`                    | Show help message                          |                |
| `-v, --version`                 | Show version info                          |                |

### Examples

```bash
# Public holidays in France for 2026
java -jar nager-date-cli.jar public-holiday -c FR -y 2026

# Long weekends in Germany, table format
java -jar nager-date-cli.jar long-weekend -c DE -f TABLE

# List all countries as JSON
java -jar nager-date-cli.jar countries -f JSON

# Country info for Belgium
java -jar nager-date-cli.jar country -c BE
```

## Project Structure

```
nager-date-cli/
├── nager-date-api/            # Models and service interfaces
├── nager-date-http-client/    # HTTP client implementation
└── nager-date-cli/            # CLI application (JCommander)
```

## Testing

```bash
mvn test
```

## Further Improvements

- GraalVM native image support for faster startup and reduced memory footprint
- Caching of API responses to reduce network calls
- CSV output format
- Filtering results by holiday type, date range, or other criteria
- Sorting results by date, name, or other fields
- Limiting the number of returned results
- Use [picocli](https://picocli.info/) instead of JCommander

## Design

For an overview of the design patterns used in this project, see [DESIGN.md](DESIGN.md).

## License

See [LICENSE](LICENSE) for details.
