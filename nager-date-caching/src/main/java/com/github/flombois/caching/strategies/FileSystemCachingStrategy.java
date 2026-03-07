package com.github.flombois.caching.strategies;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.CacheEntry;
import com.github.flombois.caching.caches.TypedCacheEntry;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * A {@link CachingStrategy} that hashes cache keys using SHA-256 to produce filesystem-safe filenames.
 * <p>
 * Extends {@link DefaultCachingStrategy} and overrides {@link #buildKey(String, Object...)}
 * to return the hex-encoded SHA-256 digest of the original key.
 * Creates {@link TypedCacheEntry} instances so that the filesystem cache can extract
 * type information for serialization.
 * </p>
 *
 * @param <T> the type of values returned by the cached service calls
 * @since 1.0
 */
public class FileSystemCachingStrategy<T> extends DefaultCachingStrategy<T> {

    private final Type type;

    /**
     * Creates a new filesystem caching strategy backed by the given cache.
     *
     * @param cache the cache to use for storing and retrieving results
     * @param type  the type descriptor for cached values, must not be null
     */
    public FileSystemCachingStrategy(Cache<T> cache, Type type) {
        super(cache);
        this.type = type;
    }

    @Override
    protected CacheEntry<T> createEntry(T result) {
        return new TypedCacheEntry<>(result, type);
    }

    @Override
    public String buildKey(String serviceName, Object... params) {
        final String originalKey = super.buildKey(serviceName, params);
        return sha256Hex(originalKey);
    }

    private static String sha256Hex(String input) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
