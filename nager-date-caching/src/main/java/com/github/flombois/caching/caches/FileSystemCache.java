package com.github.flombois.caching.caches;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link Cache} implementation that persists entries as JSON files on the filesystem.
 * <p>
 * Each cache entry is stored as a {@code {key}.json} file in the configured cache directory.
 * The key is expected to be a filesystem-safe string (e.g., a hex hash).
 * Type information is extracted from each {@link CacheEntry} on {@code put()} and
 * persisted alongside the data for correct deserialization on {@code get()}.
 * </p>
 *
 * @param <T> the type of values stored in the cache
 * @since 1.0
 */
public class FileSystemCache<T> implements Cache<T> {

    private static final Logger LOGGER = Logger.getLogger(FileSystemCache.class.getName());

    private final Path cacheDirectory;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new filesystem cache.
     *
     * @param cacheDirectory the directory where cache files are stored
     * @param objectMapper   the Jackson object mapper for JSON serialization/deserialization
     */
    public FileSystemCache(Path cacheDirectory, ObjectMapper objectMapper) {
        this.cacheDirectory = cacheDirectory;
        this.objectMapper = objectMapper;
        createDirectoryIfAbsent();
    }

    private void createDirectoryIfAbsent() {
        try {
            Files.createDirectories(cacheDirectory);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path resolveFile(String key) {
        return cacheDirectory.resolve(key + ".json");
    }

    @Override
    public Optional<CacheEntry<T>> get(String key) {
        Path file = resolveFile(key);
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try {
            final JsonNode root = objectMapper.readTree(file.toFile());
            final String typeCanonical = root.get("type").asText();
            final JavaType javaType = objectMapper.getTypeFactory().constructFromCanonical(typeCanonical);
            T data = objectMapper.convertValue(root.get("data"), javaType);
            LOGGER.info("Cache file read: " + file);
            return Optional.of(new TypedCacheEntry<>(data, javaType));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Corrupt or unreadable cache file: " + file, e);
            return Optional.empty();
        }
    }

    @Override
    public CacheEntry<T> put(String key, CacheEntry<T> entry) {
        Path file = resolveFile(key);
        try {
            TypedCacheEntry<T> typedEntry = (TypedCacheEntry<T>) entry;
            JavaType javaType = objectMapper.getTypeFactory().constructType(typedEntry.getType());
            ObjectNode root = objectMapper.createObjectNode();
            root.put("type", javaType.toCanonical());
            root.set("data", objectMapper.valueToTree(entry.getValue()));
            objectMapper.writeValue(file.toFile(), root);
            LOGGER.info("Cache file written: " + file);
            return entry;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void evict(String key) {
        try {
            Files.deleteIfExists(resolveFile(key));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void clear() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(cacheDirectory, "*.json")) {
            for (Path file : stream) {
                Files.deleteIfExists(file);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
