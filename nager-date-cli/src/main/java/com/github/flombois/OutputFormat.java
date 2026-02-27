package com.github.flombois;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.flombois.printers.PrintableRecord;

/**
 * Enumeration of supported output formats for CLI results.
 * <p>
 * Each format knows how to convert a {@link PrintableRecord} to a string
 * representation and print it to standard output.
 * </p>
 *
 * @since 1.0
 */
public enum OutputFormat {

    /** JSON output format using Jackson serialization. */
    JSON {

        /** Jackson ObjectMapper instance for JSON serialization. */
        public static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        @Override
        String toString(PrintableRecord<?> printableRecord) {
            if(printableRecord.isJsonPrintable()) return printableRecord.toJsonString(objectMapper);
            else throw new IllegalArgumentException("Record is not json printable");
        }
    },

    /** Plain text output format using custom string formatting. */
    PLAIN {
        @Override
        String toString(PrintableRecord<?> printableRecord) {
            if(printableRecord.isPlainPrintable()) return printableRecord.toPlainString();
            else throw new IllegalArgumentException("Record is not plain printable");
        }

    },

    /** Table output format for structured tabular display. */
    TABLE {

        @Override
        String toString(PrintableRecord<?> printableRecord) {
            if(printableRecord.isTablePrintable()) return printableRecord.toTableString();
            else throw new IllegalArgumentException("Record is not table printable");
        }
    };

    /**
     * Converts the given record to a string and prints it to standard output.
     *
     * @param printableRecord the record to print
     */
    public void print(PrintableRecord<?> printableRecord) {
        System.out.println(toString(printableRecord));
    }

    /**
     * Converts the given record to a string representation in this format.
     *
     * @param printableRecord the record to convert
     * @return the formatted string representation
     * @throws IllegalArgumentException if the record does not support this format
     */
    abstract String toString(PrintableRecord<?> printableRecord);
}
