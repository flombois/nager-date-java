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
     * Stores an entry in the cache under the given key, replacing any existing entry.
     *
     * @param key   the cache key
     * @param entry the cache entry to store
     * @return the stored {@link CacheEntry}
     */
    CacheEntry<T> put(String key, CacheEntry<T> entry);

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
     * Stores an entry in the cache only if no entry exists for the given key.
     *
     * @param key   the cache key
     * @param entry the cache entry to store if absent
     * @return the existing {@link CacheEntry} if present, or the newly stored entry
     */
    default CacheEntry<T> putIfAbsent(String key, CacheEntry<T> entry) {
        return get(key).orElseGet(() -> put(key, entry));
    }
}
