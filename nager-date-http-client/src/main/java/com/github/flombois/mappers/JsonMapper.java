package com.github.flombois.mappers;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.Set;

/**
 * Singleton JSON deserializer using Jackson's {@link ObjectMapper}.
 * <p>
 * Provides methods to deserialize JSON byte arrays into single objects or
 * sets of objects. Wraps Jackson's {@link IOException} as {@link MappingException}.
 * A shared {@link ObjectMapper} instance is reused across all calls for performance.
 * </p>
 *
 * @since 1.0
 */
public class JsonMapper {

    private static final JsonMapper INSTANCE = new JsonMapper();

    private final ObjectMapper objectMapper;

    private JsonMapper() {
        this(com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build());
    }

    /**
     * Constructs a JsonMapper with a custom {@link ObjectMapper}.
     *
     * @param objectMapper the Jackson object mapper to use for deserialization
     */
    JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Returns the singleton instance.
     *
     * @return the shared {@link JsonMapper} instance
     */
    public static JsonMapper getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the underlying {@link ObjectMapper}.
     *
     * @return the object mapper
     */
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Deserializes a JSON byte array into an object of the specified class.
     *
     * @param <T> the target type
     * @param input the JSON byte array
     * @param clazz the target class
     * @return the deserialized object
     * @throws MappingException if deserialization fails
     */
    public <T> T map(byte[] input, Class<T> clazz) throws MappingException {
        try {
            return objectMapper.readValue(input, clazz);
        } catch (IOException e) {
            throw new MappingException(e);
        }
    }

    /**
     * Deserializes a JSON byte array into a {@link Set} of objects of the specified element type.
     *
     * @param <T> the element type
     * @param input the JSON byte array
     * @param clazz the class of the set elements
     * @return the deserialized set
     * @throws MappingException if deserialization fails
     */
    public <T> Set<T> mapToSet(byte[] input, Class<T> clazz) throws MappingException {
        try {
            var type = objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz);
            return objectMapper.readValue(input, type);
        } catch (IOException e) {
            throw new MappingException(e);
        }
    }

    /**
     * Serializes an object to a pretty-printed JSON string.
     *
     * @param value the object to serialize
     * @return the JSON string representation
     * @throws MappingException if serialization fails
     */
    public String writeAsString(Object value) throws MappingException {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (IOException e) {
            throw new MappingException(e);
        }
    }
}
