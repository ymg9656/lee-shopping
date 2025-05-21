package com.lee.shopping.infrastracture.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.lee.shopping.infrastracture.configuration.properties.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.stream.Collectors;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    @ConfigurationProperties(prefix = "application.caches")
    CacheProperties cacheConf() {
        return new CacheProperties();
    }


    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        CacheProperties cacheProperties = cacheConf();

        cacheManager.setCacheNames(cacheProperties.getCaffeine()
                .stream()
                .map(CacheProperties.CacheSpec::getName)
                .collect(Collectors.toList()));

        cacheProperties.getCaffeine().forEach(cacheSpec -> {
            cacheManager.registerCustomCache(cacheSpec.getName(), Caffeine.from(cacheSpec.getSpec()).build());
        });

        return cacheManager;
    }


}
