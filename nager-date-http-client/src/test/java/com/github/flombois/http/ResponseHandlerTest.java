package com.github.flombois.http;

import com.github.flombois.ApiCallException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseHandlerTest {

    @Mock
    HttpResponse<byte[]> response;

    private final ResponseHandler<String> handler = new ResponseHandler<>() {
        @Override
        public String success(HttpResponse<byte[]> response) {
            return "ok";
        }
    };

    @Test
    void handleDispatchesTo200Success() throws ApiCallException {
        when(response.statusCode()).thenReturn(200);
        assertEquals("ok", handler.handle(response));
    }

    @Test
    void handleThrowsOnNullResponse() {
        assertThrows(ApiCallException.class, () -> handler.handle(null));
    }

    @Test
    void handleThrowsOn204NoContent() {
        when(response.statusCode()).thenReturn(204);
        assertThrows(ApiCallException.class, () -> handler.handle(response));
    }

    @Test
    void handleThrowsOn400BadRequest() {
        when(response.statusCode()).thenReturn(400);
        assertThrows(ApiCallException.class, () -> handler.handle(response));
    }

    @Test
    void handleThrowsOn404NotFound() {
        when(response.statusCode()).thenReturn(404);
        assertThrows(ApiCallException.class, () -> handler.handle(response));
    }

    @Test
    void handleThrowsOnUnexpectedStatusCode() {
        when(response.statusCode()).thenReturn(500);
        var ex = assertThrows(ApiCallException.class, () -> handler.handle(response));
        assertNotNull(ex.getResponse());
    }
}
