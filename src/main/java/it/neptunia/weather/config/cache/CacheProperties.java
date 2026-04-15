package it.neptunia.weather.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private Entry weather = new Entry();
    private Entry geocoding = new Entry();
    private Entry forecast = new Entry();

    @Data
    public static class Entry {
        private int ttlHours;
        private int maxSize;
    }
}
