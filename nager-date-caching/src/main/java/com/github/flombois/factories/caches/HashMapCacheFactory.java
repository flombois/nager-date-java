package com.github.flombois.factories.caches;

import com.github.flombois.caching.caches.Cache;
import com.github.flombois.caching.caches.MapCache;

import java.util.HashMap;

public class HashMapCacheFactory implements CacheFactory {

    @Override
    public <T> Cache<T> create() {
        return new MapCache<>(new HashMap<>());
    }
}
