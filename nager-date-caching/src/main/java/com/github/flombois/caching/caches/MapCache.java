package com.github.flombois.caching.caches;

import java.util.Map;
import java.util.Optional;

/**
 * A {@link Cache} implementation backed by a {@link Map}.
 * <p>
 * Thread safety depends on the {@link Map} implementation provided at construction time.
 * For concurrent access, use a thread-safe map such as {@link java.util.concurrent.ConcurrentHashMap}.
 * </p>
 *
 * @param <T> the type of values stored in the cache
 * @since 1.0
 */
public class MapCache<T> implements Cache<T> {

    private final Map<String, CacheEntry<T>> cacheMap;

    /**
     * Creates a new {@code MapCache} backed by the given map.
     *
     * @param cacheMap the map used as the underlying cache store
     */
    public MapCache(Map<String, CacheEntry<T>> cacheMap) {
        this.cacheMap = cacheMap;
    }

    @Override
    public Optional<CacheEntry<T>> get(String key) {
        return Optional.ofNullable(cacheMap.get(key));
    }

    @Override
    public CacheEntry<T> put(String key, T object) {
        final var entry = new CacheEntry<>(object);
        cacheMap.put(key, entry);
        return entry;
    }

    @Override
    public void evict(String key) {
        cacheMap.remove(key);
    }

    @Override
    public void clear() {
        cacheMap.clear();
    }
}
