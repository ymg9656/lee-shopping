package com.lee.shopping.infrastracture.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalCacheManager {
    private final CacheManager cacheManager;
    public void cacheEvict(String cacheName,String cacheKey){

        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
        Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();

        List<Object> keys = nativeCache.asMap().keySet().stream()
                .filter(key -> key.toString().startsWith(cacheKey))
                .toList();

        for(Object key:keys){
            log.info("cacheEvict name={}, key={}",cacheName,key);
            nativeCache.invalidate(key);
        }

    }
}
