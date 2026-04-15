package it.neptunia.weather.model.response;

import it.neptunia.weather.model.DailyForecast;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastSummary {
    private String city;
    private List<DailyForecast> forecast;
}
