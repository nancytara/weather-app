package it.neptunia.weather.controller;

import feign.FeignException;
import feign.Request;
import it.neptunia.weather.exception.CityNotFoundException;
import it.neptunia.weather.model.Weather;
import it.neptunia.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void shouldReturn200WithWeatherSummaryForValidCity() throws Exception {
        Weather weather = Weather.builder()
                .city("London")
                .temperature(15.0)
                .windspeed(20.0)
                .weatherDescription("Partly cloudy")
                .build();

        when(weatherService.getWeather("London")).thenReturn(weather);

        mockMvc.perform(get("/weather/temperature").param("city", "London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.temperature").value(15.0))
                .andExpect(jsonPath("$.windspeed").value(20.0))
                .andExpect(jsonPath("$.weatherDescription").value("Partly cloudy"));
    }

    @Test
    void shouldReturn404WhenCityNotFound() throws Exception {
        when(weatherService.getWeather("INVALID")).thenThrow(new CityNotFoundException("INVALID"));

        mockMvc.perform(get("/weather/temperature").param("city", "INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn200WithWeatherListForMultipleCities() throws Exception {
        Weather rome = Weather.builder().city("Rome").temperature(21.0).windspeed(8.0).weatherDescription("Clear sky").build();
        Weather london = Weather.builder().city("London").temperature(12.0).windspeed(20.0).weatherDescription("Overcast").build();

        when(weatherService.getWeatherForCities(List.of("Rome", "London"))).thenReturn(List.of(rome, london));

        mockMvc.perform(get("/weather/temperatures").param("cities", "Rome", "London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Rome"))
                .andExpect(jsonPath("$[1].city").value("London"));
    }

    @Test
    void shouldReturn400WhenCityParamIsMissing() throws Exception {
        mockMvc.perform(get("/weather/temperature"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn502WhenExternalApiFails() throws Exception {
        Request dummyRequest = Request.create(Request.HttpMethod.GET, "https://api.open-meteo.com", Map.of(), null, null, null);
        when(weatherService.getWeather("Rome"))
                .thenThrow(new FeignException.ServiceUnavailable("API non raggiungibile", dummyRequest, null, null));

        mockMvc.perform(get("/weather/temperature").param("city", "Rome"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502));
    }

    @Test
    void shouldReturn502WhenApiTimesOut() throws Exception {
        Request dummyRequest = Request.create(Request.HttpMethod.GET, "https://api.open-meteo.com", Map.of(), null, null, null);
        when(weatherService.getWeather("Paris"))
                .thenThrow(new FeignException.GatewayTimeout("Timeout", dummyRequest, null, null));

        mockMvc.perform(get("/weather/temperature").param("city", "Paris"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502));
    }

    @Test
    void shouldReturn500OnUnexpectedError() throws Exception {
        when(weatherService.getWeather("Berlin"))
                .thenThrow(new RuntimeException("Errore imprevisto"));

        mockMvc.perform(get("/weather/temperature").param("city", "Berlin"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }
}
