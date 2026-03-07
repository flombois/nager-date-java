package com.github.flombois.factories.caches;

import com.github.flombois.caching.caches.FileSystemCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemCacheFactoryTest {

    @TempDir
    Path tempDir;

    private FileSystemCacheFactory factory;

    @BeforeEach
    void setUp() {
        factory = new FileSystemCacheFactory(tempDir.resolve("cache"));
    }

    @Test
    void createReturnsFileSystemCache() {
        var cache = factory.create();
        assertInstanceOf(FileSystemCache.class, cache);
    }

    @Test
    void createReturnsNewInstanceEachTime() {
        var cache1 = factory.create();
        var cache2 = factory.create();
        assertNotSame(cache1, cache2);
    }
}
