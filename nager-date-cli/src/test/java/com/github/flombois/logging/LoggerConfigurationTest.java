package com.github.flombois.logging;

import org.junit.jupiter.api.Test;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LoggerConfigurationTest {

    @Test
    void configureWithDebugEnabledSetsInfoLevel() {
        LoggerConfiguration.configure(true);

        Logger root = Logger.getLogger("");
        assertEquals(Level.INFO, root.getLevel());
        for (Handler handler : root.getHandlers()) {
            assertEquals(Level.INFO, handler.getLevel());
        }
    }

    @Test
    void configureWithDebugDisabledSetsSevereLevel() {
        LoggerConfiguration.configure(false);

        Logger root = Logger.getLogger("");
        assertEquals(Level.SEVERE, root.getLevel());
        for (Handler handler : root.getHandlers()) {
            assertEquals(Level.SEVERE, handler.getLevel());
        }
    }
}
