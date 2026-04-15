package it.neptunia.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import it.neptunia.weather.geocoding.service.GeocodingService;
import it.neptunia.weather.model.DailyForecast;
import it.neptunia.weather.model.Weather;
import it.neptunia.weather.model.request.WeatherRequest;
import it.neptunia.weather.model.response.WeatherResponse;
import it.neptunia.weather.model.response.geocoding.GeocodingResponse;
import it.neptunia.weather.proxy.OpenMeteoProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String CURRENT_FIELDS =
            "temperature_2m,relative_humidity_2m,precipitation,weather_code,wind_speed_10m";

    private static final String DAILY_FIELDS =
            "temperature_2m_max,temperature_2m_min,precipitation_sum,weather_code";

    private final OpenMeteoProxy openMeteoProxy;
    private final GeocodingService geocodingService;

    /**
     * Recupera i dati meteo correnti per più città in parallelo.
     * Le città già presenti in cache vengono restituite immediatamente senza chiamate API.
     *
     * @param cities lista di nomi di città (es. {@code ["Roma", "Tokyo", "Londra"]})
     * @return lista di {@link Weather} nello stesso ordine dell'input
     * @throws CityNotFoundException se una delle città non viene trovata
     */
    public List<Weather> getWeatherForCities(List<String> cities) {
        return cities.parallelStream()
                .map(this::getWeather)
                .collect(Collectors.toList());
    }

    /**
     * Recupera i dati meteo correnti per il nome di città indicato.
     *
     * <p>Esegue internamente due chiamate API:
     * <ol>
     *   <li>API Geocoding di Open-Meteo per convertire il nome in coordinate geografiche.</li>
     *   <li>API Forecast di Open-Meteo per recuperare il meteo corrente dalle coordinate.</li>
     * </ol>
     *
     * @param city nome della città (es. {@code "Roma"}, {@code "Tokyo"})
     * @return oggetto {@link Weather} con città, temperatura (°C), umidità (%),
     *         precipitazioni (mm), velocità del vento (km/h) e descrizione meteo
     * @throws CityNotFoundException se l'API geocoding non trova risultati per il nome indicato
     * @throws feign.FeignException se una delle chiamate API esterne fallisce
     *
     * @example
     * <pre>
     *   Weather w = weatherService.getWeather("Londra");
     *   // w.getCity()               → "London"
     *   // w.getTemperature()        → 12.3
     *   // w.getHumidity()           → 72
     *   // w.getWeatherDescription() → "Partly cloudy"
     * </pre>
     */
    @Cacheable(value = "weather", key = "#city.toLowerCase()", unless = "#result == null")
    public Weather getWeather(String city) {
        GeocodingResponse.CityResult coords = geocodingService.getCoordinates(city);

        WeatherRequest request = WeatherRequest.builder()
                .latitude(coords.getLatitude())
                .longitude(coords.getLongitude())
                .current(CURRENT_FIELDS)
                .build();

        WeatherResponse.Current current = openMeteoProxy.getWeather(request).getCurrent();

        return Weather.builder()
                .city(coords.getName())
                .temperature(current.getTemperature())
                .windspeed(current.getWindspeed())
                .humidity(current.getHumidity())
                .precipitation(current.getPrecipitation())
                .weatherDescription(WeatherCode.describe(current.getWeathercode()))
                .build();
    }

    /**
     * Recupera la previsione meteo giornaliera per il numero di giorni indicato.
     *
     * @param city nome della città (es. {@code "Roma"})
     * @param days numero di giorni di previsione (1–16)
     * @return lista di {@link DailyForecast}, uno per giorno
     * @throws CityNotFoundException se la città non viene trovata
     */
    @Cacheable(value = "forecast", key = "#city.toLowerCase() + '-' + #days", unless = "#result == null")
    public List<DailyForecast> getWeatherForecast(String city, int days) {
        GeocodingResponse.CityResult coords = geocodingService.getCoordinates(city);

        WeatherRequest request = WeatherRequest.builder()
                .latitude(coords.getLatitude())
                .longitude(coords.getLongitude())
                .daily(DAILY_FIELDS)
                .forecastDays(days)
                .build();

        WeatherResponse.Daily daily = openMeteoProxy.getWeather(request).getDaily();

        List<DailyForecast> result = new ArrayList<>();
        for (int i = 0; i < daily.getTime().size(); i++) {
            result.add(DailyForecast.builder()
                    .date(daily.getTime().get(i))
                    .maxTemperature(daily.getMaxTemperature().get(i))
                    .minTemperature(daily.getMinTemperature().get(i))
                    .precipitation(daily.getPrecipitation().get(i))
                    .weatherDescription(WeatherCode.describe(daily.getWeathercode().get(i)))
                    .build());
        }
        return result;
    }
}
