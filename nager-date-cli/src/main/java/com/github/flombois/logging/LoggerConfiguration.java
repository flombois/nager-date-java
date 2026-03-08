package com.github.flombois.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configures the root {@link Logger} level based on CLI flags.
 * <p>
 * When debug mode is enabled, the root logger and all its handlers are set to
 * {@link Level#INFO} (which also captures WARNING and above). Otherwise, only
 * {@link Level#SEVERE} errors are logged.
 * </p>
 *
 * @since 1.0
 */
public final class LoggerConfiguration {

    private LoggerConfiguration() {}

    /**
     * Configures the root logger level.
     *
     * @param debug {@code true} to enable INFO-level logging, {@code false} for SEVERE only
     */
    public static void configure(boolean debug) {
        final Logger root = Logger.getLogger("");
        final Level level = debug ? Level.INFO : Level.SEVERE;
        root.setLevel(level);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(level);
        }
    }
}
