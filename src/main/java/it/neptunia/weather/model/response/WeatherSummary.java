package it.neptunia.weather.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherSummary {
    private String city;
    private double temperature;
    private double windspeed;
    private int humidity;
    private double precipitation;
    private String weatherDescription;
}
