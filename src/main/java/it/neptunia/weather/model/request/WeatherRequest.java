package it.neptunia.weather.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherRequest {
    private double latitude;
    private double longitude;

    /** Comma-separated current variables (e.g. "temperature_2m,wind_speed_10m"). */
    private String current;

    /** Comma-separated daily variables (e.g. "temperature_2m_max,precipitation_sum"). */
    private String daily;

    @JsonProperty("forecast_days")
    private Integer forecastDays;
}
