package it.neptunia.weather.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherCodeTest {

    @Test
    void shouldDescribeClearSky() {
        assertEquals("Clear sky", WeatherCode.describe(0));
    }

    @Test
    void shouldDescribeRain() {
        assertEquals("Rain", WeatherCode.describe(61));
        assertEquals("Rain", WeatherCode.describe(63));
        assertEquals("Rain", WeatherCode.describe(65));
    }

    @Test
    void shouldDescribeThunderstorm() {
        assertEquals("Thunderstorm", WeatherCode.describe(95));
    }

    @Test
    void shouldDescribeThunderstormWithHail() {
        assertEquals("Thunderstorm with hail", WeatherCode.describe(96));
        assertEquals("Thunderstorm with hail", WeatherCode.describe(99));
    }

    @Test
    void shouldReturnUnknownForUnrecognizedCode() {
        assertEquals("Unknown", WeatherCode.describe(-1));
        assertEquals("Unknown", WeatherCode.describe(999));
    }
}
