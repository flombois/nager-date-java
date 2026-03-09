package com.github.flombois.mappers;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JsonMapperTest {

    private final JsonMapper mapper = JsonMapper.getInstance();

    @Test
    void singletonInstanceIsNotNull() {
        assertNotNull(JsonMapper.getInstance());
    }

    @Test
    void singletonReturnsSameInstance() {
        assertSame(JsonMapper.getInstance(), JsonMapper.getInstance());
    }

    @Test
    void mapsJsonToObject() {
        byte[] json = """
                {"name": "test", "value": 42}
                """.getBytes(StandardCharsets.UTF_8);
        var result = mapper.map(json, TestDto.class);
        assertEquals("test", result.name);
        assertEquals(42, result.value);
    }

    @Test
    void mapsJsonToSet() {
        byte[] json = """
                [{"name": "a", "value": 1}, {"name": "b", "value": 2}]
                """.getBytes(StandardCharsets.UTF_8);
        Set<TestDto> result = mapper.mapToSet(json, TestDto.class);
        assertEquals(2, result.size());
    }

    @Test
    void writeAsStringProducesJson() {
        var dto = new TestDto();
        dto.name = "hello";
        dto.value = 7;
        String json = mapper.writeAsString(dto);
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"hello\""));
        assertTrue(json.contains("7"));
    }

    @Test
    void mapThrowsMappingExceptionOnInvalidJson() {
        byte[] invalid = "not json".getBytes(StandardCharsets.UTF_8);
        assertThrows(MappingException.class, () -> mapper.map(invalid, TestDto.class));
    }

    @Test
    void mapToSetThrowsMappingExceptionOnInvalidJson() {
        byte[] invalid = "not json".getBytes(StandardCharsets.UTF_8);
        assertThrows(MappingException.class, () -> mapper.mapToSet(invalid, TestDto.class));
    }

    public static class TestDto {
        public String name;
        public int value;
    }
}
