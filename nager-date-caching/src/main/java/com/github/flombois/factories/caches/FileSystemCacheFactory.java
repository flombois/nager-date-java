package com.github.flombois.factories.caches;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.FileSystemCache;

import java.nio.file.Path;

/**
 * A {@link CacheFactory} that creates {@link FileSystemCache} instances backed by JSON files.
 *
 * @since 1.0
 */
public class FileSystemCacheFactory implements CacheFactory {

    private final Path cacheDirectory;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new factory that stores cache files in the given directory.
     *
     * @param cacheDirectory the directory where cache files are stored
     */
    public FileSystemCacheFactory(Path cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
    }

    @Override
    public <T> Cache<T> create() {
        return new FileSystemCache<>(cacheDirectory, objectMapper);
    }
}
