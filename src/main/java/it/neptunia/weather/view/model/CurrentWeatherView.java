package it.neptunia.weather.view.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrentWeatherView {
    String city;
    double temperature;
    double windspeed;
    int humidity;
    double precipitation;
    String weatherDescription;
    String icon;
}
