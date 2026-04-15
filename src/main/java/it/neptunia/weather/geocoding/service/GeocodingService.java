package it.neptunia.weather.geocoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import it.neptunia.weather.exception.CityNotFoundException;
import it.neptunia.weather.model.response.geocoding.GeocodingResponse;
import it.neptunia.weather.proxy.GeocodingProxy;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final GeocodingProxy geocodingProxy;


    /**
     * Converte il nome di una città in coordinate geografiche (latitudine e longitudine).
     *
     * <p>Chiama l'API Geocoding di Open-Meteo con {@code count=1} per ottenere
     * il risultato più pertinente al nome indicato.
     *
     * @param city nome della città da cercare (es. {@code "Paris"})
     * @return il primo {@link GeocodingResponse.CityResult} restituito dall'API,
     *         contenente {@code name}, {@code latitude} e {@code longitude}
     * @throws CityNotFoundException se l'API non restituisce risultati
     *         per il nome indicato (inclusi errori di ortografia)
     *
     * @example
     * <pre>
     *   GeocodingResponse.CityResult result = geocodingService.getCoordinates("Rome");
     *   // result.getName()      → "Rome"
     *   // result.getLatitude()  → 41.89
     *   // result.getLongitude() → 12.48
     * </pre>
     */
    @Cacheable(value = "geocoding", key = "#city.toLowerCase()", unless = "#result == null")
    public GeocodingResponse.CityResult getCoordinates(String city) {
        GeocodingResponse response = geocodingProxy.searchCity(city, 1);

        if (response.getResults() == null || response.getResults().isEmpty()) {
            throw new CityNotFoundException(city);
        }

        return response.getResults().get(0);
    }
}