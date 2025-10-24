package com.example.cross_project.config;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    CacheManager cacheManager(){
        SimpleCacheManager scm = new SimpleCacheManager();
        scm.setCaches(Arrays.asList(
            new ConcurrentMapCache("users"),
            new ConcurrentMapCache("user"),
            
            new ConcurrentMapCache("sensors"),
            new ConcurrentMapCache("sensor"),

            new ConcurrentMapCache("alerts"),
            new ConcurrentMapCache("alert")
            ));
        return scm;
    }
}