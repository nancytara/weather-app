# Neptunia Weather Service

A Spring Boot REST API that retrieves current weather data for any city by name, using the free [Open-Meteo](https://open-meteo.com/) APIs — no API key required.

---

## Project Overview

The service accepts a city name, converts it to geographic coordinates via the Open-Meteo Geocoding API, then fetches current weather from the Open-Meteo Forecast API. It returns a structured JSON response with temperature, wind speed and a human-readable weather description.

---

## Features

- City name → coordinates via Open-Meteo Geocoding API
- Current temperature (°C) and wind speed (km/h)
- Human-readable weather description mapped from WMO weather codes
- Structured error responses for invalid cities, missing parameters and API failures
- OpenAPI / Swagger UI available at `/swagger-ui.html`
- CORS enabled for `GET` and `POST` requests from any origin

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.4 |
| HTTP clients | Spring Cloud OpenFeign + feign-jackson |
| API docs | SpringDoc OpenAPI 2.8.5 |
| Build | Maven 3 |

---

## Installation

**Prerequisites:** Java 21+, Maven 3.9+

```bash
git clone <repository-url>
cd neptunia-weather-service
mvn spring-boot:run
```

The service starts on `http://localhost:8080`.

---

## Usage

### Get current weather

```
GET /weather/temperature?city={cityName}
```

**Example request:**
```
GET http://localhost:8080/weather/temperature?city=Tokyo
```

**Example response:**
```json
{
  "city": "Tokyo",
  "temperature": 22.5,
  "windspeed": 10.0,
  "weatherDescription": "Clear sky"
}
```

---

## Error Handling

| Scenario | HTTP status | Response body |
|---|---|---|
| City not found / misspelled | `404 Not Found` | `{ "status": 404, "message": "Città non trovata: XYZ" }` |
| Missing `city` parameter | `400 Bad Request` | `{ "status": 400, "message": "Parametro mancante: city" }` |
| External API unreachable / timeout | `502 Bad Gateway` | `{ "status": 502, "message": "Errore nella chiamata all'API meteo" }` |
| Unexpected server error | `500 Internal Server Error` | `{ "status": 500, "message": "Errore interno del server" }` |

---

## External APIs

| API | Base URL | Purpose |
|---|---|---|
| Open-Meteo Geocoding | `https://geocoding-api.open-meteo.com` | Convert city name → lat/lng |
| Open-Meteo Forecast | `https://api.open-meteo.com` | Fetch current weather by coordinates |

Both APIs are free and require no authentication.

---

## Running Tests

```bash
mvn test
```

The test suite covers:

- Unit tests for WMO weather code mapping (`WeatherCodeTest`)
- Unit tests for geocoding with mock responses (`GeocodingServiceTest`)
- Unit tests for the weather service orchestration (`WeatherServiceTest`)
- Controller integration tests including error scenarios (`WeatherControllerTest`)

---

## Future Improvements

- Add hourly and daily forecast endpoints
- Cache geocoding responses to reduce redundant API calls
- Add `humidity` and `precipitation` to the response
- Support multiple cities in a single request
- Internationalise weather descriptions
