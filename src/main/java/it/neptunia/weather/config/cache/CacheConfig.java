package it.neptunia.weather.config.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    private final CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(
            buildCache("weather", cacheProperties.getWeather()),
            buildCache("geocoding", cacheProperties.getGeocoding()),
            buildCache("forecast", cacheProperties.getForecast())
        ));
        return manager;
    }

    private CaffeineCache buildCache(String name, CacheProperties.Entry entry) {
        return new CaffeineCache(name,
            Caffeine.newBuilder()
                .maximumSize(entry.getMaxSize())
                .expireAfterWrite(entry.getTtlHours(), TimeUnit.HOURS)
                .build());
    }
}
