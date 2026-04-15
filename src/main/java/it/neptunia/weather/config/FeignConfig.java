package it.neptunia.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.QueryMapEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class FeignConfig {

    /**
     * QueryMapEncoder that uses Jackson to serialize @SpringQueryMap objects,
     * respecting @JsonProperty field name mappings (e.g. camelCase → snake_case).
     */
    @Bean
    public QueryMapEncoder jacksonQueryMapEncoder() {
        ObjectMapper mapper = new ObjectMapper();
        return object -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.convertValue(object, Map.class);
            map.values().removeIf(v -> v == null);
            return map;
        };
    }
}
