package com.github.flombois;

import com.github.flombois.printers.PrintableRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutputFormatTest {

    @SuppressWarnings("unchecked")
    private PrintableRecord<Object> mockRecord() {
        return mock(PrintableRecord.class);
    }

    @Test
    void jsonFormatCallsToJsonString() {
        var record = mockRecord();
        when(record.isJsonPrintable()).thenReturn(true);
        when(record.toJsonString(any())).thenReturn("{}");

        OutputFormat.JSON.print(record);

        verify(record).toJsonString(any());
    }

    @Test
    void plainFormatCallsToPlainString() {
        var record = mockRecord();
        when(record.isPlainPrintable()).thenReturn(true);
        when(record.toPlainString()).thenReturn("plain");

        OutputFormat.PLAIN.print(record);

        verify(record).toPlainString();
    }

    @Test
    void tableFormatCallsToTableString() {
        var record = mockRecord();
        when(record.isTablePrintable()).thenReturn(true);
        when(record.toTableString()).thenReturn("table");

        OutputFormat.TABLE.print(record);

        verify(record).toTableString();
    }

    @Test
    void jsonFormatThrowsWhenNotSupported() {
        var record = mockRecord();
        when(record.isJsonPrintable()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> OutputFormat.JSON.print(record));
    }

    @Test
    void plainFormatThrowsWhenNotSupported() {
        var record = mockRecord();
        when(record.isPlainPrintable()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> OutputFormat.PLAIN.print(record));
    }

    @Test
    void tableFormatThrowsWhenNotSupported() {
        var record = mockRecord();
        when(record.isTablePrintable()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> OutputFormat.TABLE.print(record));
    }
}
