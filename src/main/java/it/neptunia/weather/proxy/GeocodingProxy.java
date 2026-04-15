package it.neptunia.weather.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.neptunia.weather.model.response.geocoding.GeocodingResponse;

/**
 * Client Feign per il servizio di geocoding Open-Meteo.
 * Converte il nome di una città in coordinate geografiche (latitudine e longitudine).
 */
@FeignClient(name = "geocoding", url = "https://geocoding-api.open-meteo.com")
public interface GeocodingProxy {

	/**
     * Cerca una città e restituisce le sue coordinate geografiche.
     *
     * @param name  il nome della città da cercare (es. "Roma")
     * @param count il numero massimo di risultati da restituire
     * @return {@link GeocodingResponse} contenente la lista delle città trovate
     */
	@GetMapping("/v1/search")
	GeocodingResponse searchCity(@RequestParam String name, @RequestParam int count);
}