package it.neptunia.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.neptunia.weather.model.Weather;
import it.neptunia.weather.model.response.WeatherForecastSummary;
import it.neptunia.weather.model.response.WeatherSummary;
import it.neptunia.weather.service.WeatherService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Restituisce i dati meteo correnti per più città in parallelo.
     *
     * @param cities lista di nomi di città separati da virgola (es. {@code ?cities=Roma,Londra,Tokyo})
     * @return {@code 200 OK} con lista di {@link WeatherSummary};
     *         {@code 404 Not Found} se una delle città non viene trovata
     */
    @GetMapping("/temperatures")
    public ResponseEntity<List<WeatherSummary>> getWeatherForCities(@RequestParam List<String> cities) {
        List<WeatherSummary> result = weatherService.getWeatherForCities(cities).stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * Restituisce i dati meteo correnti per la città indicata.
     *
     * @param city nome della città (es. {@code "Tokyo"})
     * @return {@code 200 OK} con {@link WeatherSummary};
     *         {@code 400 Bad Request} se il parametro {@code city} è assente;
     *         {@code 404 Not Found} se la città non viene trovata;
     *         {@code 502 Bad Gateway} se una chiamata API esterna fallisce
     */
    @GetMapping("/temperature")
    public ResponseEntity<WeatherSummary> getWeather(@RequestParam String city) {
        return ResponseEntity.ok(toSummary(weatherService.getWeather(city)));
    }

    /**
     * Restituisce la previsione meteo giornaliera per la città indicata.
     *
     * @param city nome della città (es. {@code "Tokyo"})
     * @param days numero di giorni di previsione, 1–16 (default: 7)
     * @return {@code 200 OK} con {@link WeatherForecastSummary};
     *         {@code 404 Not Found} se la città non viene trovata
     */
    @GetMapping("/forecast")
    public ResponseEntity<WeatherForecastSummary> getForecast(
            @RequestParam String city,
            @RequestParam(defaultValue = "7") int days) {
        WeatherForecastSummary summary = WeatherForecastSummary.builder()
                .city(city)
                .forecast(weatherService.getWeatherForecast(city, days))
                .build();
        return ResponseEntity.ok(summary);
    }

    private WeatherSummary toSummary(Weather w) {
        return WeatherSummary.builder()
                .city(w.getCity())
                .temperature(w.getTemperature())
                .windspeed(w.getWindspeed())
                .humidity(w.getHumidity())
                .precipitation(w.getPrecipitation())
                .weatherDescription(w.getWeatherDescription())
                .build();
    }
}
