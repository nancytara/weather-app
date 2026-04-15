package it.neptunia.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private String city;
    private double temperature;
    private double windspeed;
    private int humidity;
    private double precipitation;
    private String weatherDescription;
}
