package it.neptunia.weather.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private Current current;
    private Daily daily;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Current {
        @JsonProperty("temperature_2m")
        private double temperature;

        @JsonProperty("relative_humidity_2m")
        private int humidity;

        private double precipitation;

        @JsonProperty("weather_code")
        private int weathercode;

        @JsonProperty("wind_speed_10m")
        private double windspeed;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Daily {
        private List<String> time;

        @JsonProperty("temperature_2m_max")
        private List<Double> maxTemperature;

        @JsonProperty("temperature_2m_min")
        private List<Double> minTemperature;

        @JsonProperty("precipitation_sum")
        private List<Double> precipitation;

        @JsonProperty("weather_code")
        private List<Integer> weathercode;
    }
}
