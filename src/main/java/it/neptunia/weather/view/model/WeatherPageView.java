package it.neptunia.weather.view.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class WeatherPageView {
    CurrentWeatherView current;
    List<DailyForecastView> forecast;
}
