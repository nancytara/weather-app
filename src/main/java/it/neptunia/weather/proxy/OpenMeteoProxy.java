package it.neptunia.weather.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import it.neptunia.weather.model.request.WeatherRequest;
import it.neptunia.weather.model.response.WeatherResponse;

/**
 * Client Feign per il servizio meteorologico Open-Meteo.
 * Recupera i dati meteo correnti a partire dalle coordinate geografiche.
 */
@FeignClient(name = "openmeteo", url = "https://api.open-meteo.com")
public interface OpenMeteoProxy {

	/**
     * Recupera i dati meteorologici correnti per una posizione geografica.
     *
     * @param request oggetto contenente latitudine, longitudine e flag {@code current_weather}
     * @return {@link WeatherResponse} contenente temperatura e velocità del vento
     */
	@GetMapping("/v1/forecast")
	WeatherResponse getWeather(@SpringQueryMap WeatherRequest request);
}