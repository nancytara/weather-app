package it.neptunia.weather.view.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DailyForecastView {
    String date;
    double maxTemperature;
    double minTemperature;
    double precipitation;
    String weatherDescription;
    String icon;
}
