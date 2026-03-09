package com.github.flombois;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApiCallExceptionTest {

    @Mock
    HttpResponse<byte[]> response;

    @Test
    void messageConstructorSetsMessage() {
        var ex = new ApiCallException("test error");
        assertEquals("test error", ex.getMessage());
        assertNull(ex.getResponse());
    }

    @Test
    void causeConstructorSetsCause() {
        var cause = new IOException("io error");
        var ex = new ApiCallException(cause);
        assertEquals(cause, ex.getCause());
        assertNull(ex.getResponse());
    }

    @Test
    void responseConstructorSetsResponse() {
        var ex = new ApiCallException(response);
        assertSame(response, ex.getResponse());
    }

    @Test
    void messageAndResponseConstructorSetsBoth() {
        var ex = new ApiCallException("error", response);
        assertEquals("error", ex.getMessage());
        assertSame(response, ex.getResponse());
    }

    @Test
    void getProblemDetailsReturnsNullWhenNoResponse() {
        var ex = new ApiCallException("test");
        assertNull(ex.getProblemDetails());
    }
}
