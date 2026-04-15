package it.neptunia.weather.geocoding.service;

import it.neptunia.weather.exception.CityNotFoundException;
import it.neptunia.weather.model.response.geocoding.GeocodingResponse;
import it.neptunia.weather.proxy.GeocodingProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeocodingServiceTest {

    @Mock
    private GeocodingProxy geocodingProxy;

    @InjectMocks
    private GeocodingService geocodingService;

    @Test
    void shouldReturnCityResultWhenCityExists() {
        GeocodingResponse.CityResult cityResult = GeocodingResponse.CityResult.builder()
                .name("London")
                .latitude(51.5085)
                .longitude(-0.1257)
                .build();
        GeocodingResponse response = new GeocodingResponse(List.of(cityResult));

        when(geocodingProxy.searchCity("London", 1)).thenReturn(response);

        GeocodingResponse.CityResult result = geocodingService.getCoordinates("London");

        assertEquals("London", result.getName());
        assertEquals(51.5085, result.getLatitude());
        assertEquals(-0.1257, result.getLongitude());
    }

    @Test
    void shouldThrowCityNotFoundExceptionWhenResultsAreEmpty() {
        GeocodingResponse response = new GeocodingResponse(Collections.emptyList());
        when(geocodingProxy.searchCity("XYZ123", 1)).thenReturn(response);

        assertThrows(CityNotFoundException.class, () -> geocodingService.getCoordinates("XYZ123"));
    }

    @Test
    void shouldThrowCityNotFoundExceptionWhenResultsAreNull() {
        GeocodingResponse response = new GeocodingResponse(null);
        when(geocodingProxy.searchCity("XYZ123", 1)).thenReturn(response);

        assertThrows(CityNotFoundException.class, () -> geocodingService.getCoordinates("XYZ123"));
    }
}
