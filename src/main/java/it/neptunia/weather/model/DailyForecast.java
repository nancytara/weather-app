package it.neptunia.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecast {
    private String date;
    private double maxTemperature;
    private double minTemperature;
    private double precipitation;
    private String weatherDescription;
}
