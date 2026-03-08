package com.github.flombois.caching.caches;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.flombois.models.v3.LongWeekendV3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemCacheTest {

    @TempDir
    Path tempDir;

    private FileSystemCache<String> cache;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        cache = new FileSystemCache<>(tempDir.resolve("cache"), objectMapper);
    }

    private TypedCacheEntry<String> entry(String value) {
        return new TypedCacheEntry<>(value, String.class);
    }

    @Test
    void getMissReturnsEmpty() {
        assertTrue(cache.get("unknown").isEmpty());
    }

    @Test
    void putThenGetReturnsValue() {
        cache.put("key1", entry("hello"));
        var result = cache.get("key1");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get().getValue());
    }

    @Test
    void putReturnsEntry() {
        var result = cache.put("key1", entry("value"));
        assertNotNull(result);
        assertEquals("value", result.getValue());
    }

    @Test
    void putOverwritesExistingEntry() {
        cache.put("key1", entry("first"));
        cache.put("key1", entry("second"));
        assertEquals("second", cache.get("key1").get().getValue());
    }

    @Test
    void evictRemovesFile() {
        cache.put("key1", entry("value"));
        cache.evict("key1");
        assertTrue(cache.get("key1").isEmpty());
    }

    @Test
    void evictNonExistentKeyIsNoOp() {
        assertDoesNotThrow(() -> cache.evict("missing"));
    }

    @Test
    void clearRemovesAllJsonFiles() {
        cache.put("a", entry("1"));
        cache.put("b", entry("2"));
        cache.clear();
        assertTrue(cache.get("a").isEmpty());
        assertTrue(cache.get("b").isEmpty());
    }

    @Test
    void putIfAbsentStoresOnMiss() {
        var result = cache.putIfAbsent("key1", entry("value"));
        assertEquals("value", result.getValue());
        assertEquals("value", cache.get("key1").get().getValue());
    }

    @Test
    void putIfAbsentReturnsExistingOnHit() {
        cache.put("key1", entry("first"));
        var result = cache.putIfAbsent("key1", entry("second"));
        assertEquals("first", result.getValue());
    }

    @Test
    void corruptFileTreatedAsCacheMiss() throws IOException {
        cache.put("corrupt", entry("value"));
        Path file = tempDir.resolve("cache").resolve("corrupt.json");
        Files.writeString(file, "not valid json{{{");
        assertTrue(cache.get("corrupt").isEmpty());
    }

    @Test
    void createsDirectoryIfAbsent() {
        Path nested = tempDir.resolve("nested/deep/cache");
        var nestedCache = new FileSystemCache<String>(nested, objectMapper);
        nestedCache.put("key", entry("value"));
        assertEquals("value", nestedCache.get("key").get().getValue());
    }

    @Test
    void getReturnsTypedCacheEntry() {
        cache.put("key1", entry("value"));
        var result = cache.get("key1");
        assertTrue(result.isPresent());
        assertInstanceOf(TypedCacheEntry.class, result.get());
    }

    @Test
    void roundTripWithCollectionType() {
        Type setType = TypeFactory.defaultInstance().constructCollectionType(Set.class, LongWeekendV3.class);
        var setCache = new FileSystemCache<Set<LongWeekendV3>>(tempDir.resolve("set-cache"), objectMapper);

        var lw = new LongWeekendV3();
        lw.setStartDate(LocalDate.of(2026, 5, 1));
        lw.setEndDate(LocalDate.of(2026, 5, 4));
        lw.setDayCount(4);
        lw.setNeedBridgeDay(true);

        Set<LongWeekendV3> original = Set.of(lw);
        setCache.put("weekends", new TypedCacheEntry<>(original, setType));

        var result = setCache.get("weekends");
        assertTrue(result.isPresent());
        Set<LongWeekendV3> deserialized = result.get().getValue();
        assertEquals(1, deserialized.size());
        var first = deserialized.iterator().next();
        assertEquals(LocalDate.of(2026, 5, 1), first.getStartDate());
        assertEquals(LocalDate.of(2026, 5, 4), first.getEndDate());
        assertEquals(4, first.getDayCount());
        assertTrue(first.isNeedBridgeDay());
    }
}
