package com.github.flombois.caching.caches;

import java.util.Optional;

/**
 * A generic cache abstraction for storing and retrieving entries by key.
 *
 * @param <T> the type of values stored in the cache
 * @since 1.0
 */
public interface Cache<T> {

    /**
     * Retrieves a cache entry associated with the given key.
     *
     * @param key the cache key
     * @return an {@link Optional} containing the {@link CacheEntry} if present, or empty on a cache miss
     */
    Optional<CacheEntry<T>> get(String key);

    /**
     * Stores a value in the cache under the given key, replacing any existing entry.
     *
     * @param key    the cache key
     * @param object the value to cache
     * @return the {@link CacheEntry} wrapping the stored value
     */
    CacheEntry<T> put(String key, T object);

    /**
     * Removes the cache entry associated with the given key, if present.
     *
     * @param key the cache key to evict
     */
    void evict(String key);

    /**
     * Removes all entries from the cache.
     */
    void clear();

    /**
     * Stores a value in the cache only if no entry exists for the given key.
     *
     * @param key   the cache key
     * @param value the value to cache if absent
     * @return the existing {@link CacheEntry} if present, or the newly created entry
     */
    default CacheEntry<T> putIfAbsent(String key, T value) {
        return get(key).orElseGet(() -> put(key, value));
    }
}
