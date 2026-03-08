package com.github.flombois;

import com.beust.jcommander.JCommander;
import com.github.flombois.commands.Command;

import com.github.flombois.commands.Command.*;
import com.github.flombois.services.NagerDateServiceException;

import java.util.Map;
import java.util.Objects;
import java.util.jar.Manifest;

import static java.lang.System.exit;

/**
 * Main entry point for the Nager Date CLI application.
 * <p>
 * Uses JCommander to parse command-line arguments and dispatch to the appropriate
 * subcommand: {@code countries}, {@code country}, {@code long-weekend}, {@code public-holiday},
 * {@code last-holidays}, {@code weekday-holidays}, or {@code shared-holidays}.
 * </p>
 *
 * @since 1.0
 */
public class App {

    /** The root command used for global options parsing. */
    public static final Command MAIN_COMMAND = new Command();

    /** Map of subcommand names to their corresponding command instances. */
    public static final Map<String, ServiceInvocationCommand<?>> SUB_COMMANDS = Map.of(
            ListAllCountriesCommand.INSTANCE.name(), ListAllCountriesCommand.INSTANCE,
            CountryInfoCommand.INSTANCE.name(), CountryInfoCommand.INSTANCE,
            LongWeekendCommand.INSTANCE.name(), LongWeekendCommand.INSTANCE,
            PublicHolidayCommand.INSTANCE.name(), PublicHolidayCommand.INSTANCE,
            LastHolidaysCommand.INSTANCE.name(), LastHolidaysCommand.INSTANCE,
            WeekdayHolidaysCommand.INSTANCE.name(), WeekdayHolidaysCommand.INSTANCE,
            SharedHolidaysCommand.INSTANCE.name(), SharedHolidaysCommand.INSTANCE
    );

    private static final JCommander cmd = buildJCommander();

    /**
     * Parses command-line arguments and executes the corresponding subcommand.
     * <p>
     * If no subcommand is provided, prints usage information and exits with code 1.
     * </p>
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        cmd.parse(args);
        final var subCommand = cmd.getParsedCommand();

        // Handle the help/version flags
        if (MAIN_COMMAND.version) showVersion();
        if (MAIN_COMMAND.help) showHelp();
        if (Objects.isNull(subCommand)) showHelp();


        // Execute the subcommand
        try {
            SUB_COMMANDS.get(subCommand).execute();
        } catch (NagerDateServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showVersion() {
        try (final var inputStream = App.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF")) {
            final var manifest = new Manifest(inputStream);
            manifest.getMainAttributes().forEach((key, value) -> System.out.println(key + ": " + value));
        } catch (Exception e) {
            System.out.println("Failed to retrieve version information");
            exit(1);
        }
        exit(0);
    }

    private static void showHelp() {
        cmd.usage();
        exit(0);
    }


    /**
     * Builds and configures the JCommander instance with the main command and all subcommands.
     *
     * @return the configured JCommander instance
     */
    private static JCommander buildJCommander() {
        final var builder = JCommander.newBuilder();
        builder.addObject(MAIN_COMMAND);
        SUB_COMMANDS.forEach(builder::addCommand);
        return builder.build();
    }

}
