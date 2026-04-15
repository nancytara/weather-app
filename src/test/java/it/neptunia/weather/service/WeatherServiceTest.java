package it.neptunia.weather.service;

import it.neptunia.weather.exception.CityNotFoundException;
import it.neptunia.weather.geocoding.service.GeocodingService;
import it.neptunia.weather.model.Weather;
import it.neptunia.weather.model.request.WeatherRequest;
import it.neptunia.weather.model.response.WeatherResponse;
import it.neptunia.weather.model.response.geocoding.GeocodingResponse;
import it.neptunia.weather.proxy.OpenMeteoProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private OpenMeteoProxy openMeteoProxy;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void shouldReturnWeatherWithDescriptionForValidCity() {
        GeocodingResponse.CityResult coords = GeocodingResponse.CityResult.builder()
                .name("Tokyo")
                .latitude(35.6895)
                .longitude(139.6917)
                .build();
        WeatherResponse.Current current = WeatherResponse.Current.builder()
                .temperature(22.5)
                .windspeed(10.0)
                .humidity(65)
                .precipitation(0.0)
                .weathercode(0)
                .build();
        WeatherResponse weatherResponse = new WeatherResponse(current, null);

        when(geocodingService.getCoordinates("Tokyo")).thenReturn(coords);
        when(openMeteoProxy.getWeather(any(WeatherRequest.class))).thenReturn(weatherResponse);

        Weather result = weatherService.getWeather("Tokyo");

        assertEquals("Tokyo", result.getCity());
        assertEquals(22.5, result.getTemperature());
        assertEquals(10.0, result.getWindspeed());
        assertEquals(65, result.getHumidity());
        assertEquals("Clear sky", result.getWeatherDescription());
    }

    @Test
    void shouldPropagateExceptionWhenCityNotFound() {
        when(geocodingService.getCoordinates("INVALID")).thenThrow(new CityNotFoundException("INVALID"));

        assertThrows(CityNotFoundException.class, () -> weatherService.getWeather("INVALID"));
    }
}
