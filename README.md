# 🌦️ Weather Service (Open-Meteo API)

## Project Overview

This Weather App allows users to check the current weather and 7-day forecast for any city in the world. It uses the Open-Meteo Geocoding API to find location coordinates and then fetches weather data using the Open-Meteo Weather Forecast API. No API key is required.

The app includes a web interface built with Thymeleaf and a full REST API documented with Swagger.

---

## App Features

- Search bar to enter a city name
- Display of temperature, wind speed, humidity, precipitation and weather description
- Weather icons mapped from WMO weather codes
- Live weather widgets for 5 major Italian cities on the home page
- 7-day forecast slider — click a day to expand its details
- Error notification if a city is not found
- Supports multiple city searches via REST API
- Response caching with Caffeine to reduce redundant API calls

---

## How to Navigate & Run the Code

Clone this repository:

```bash
git clone https://github.com/yourusername/neptunia-weather-service.git
```

Navigate to the project folder:

```bash
cd neptunia-weather-service
```

Run the application:

```bash
mvn spring-boot:run
```

Open your browser and go to:

```
http://localhost:8080
```

The REST API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## Project Files

```
src/main/java/it/neptunia/weather/
├── controller/         WeatherController.java          REST API endpoints
├── view/
│   ├── controller/     WeatherViewController.java      Thymeleaf page controller
│   └── model/          CurrentWeatherView, DailyForecastView, WeatherPageView
├── service/            WeatherService.java             Business logic and caching
│                       WeatherCode.java                WMO code → description mapping
├── geocoding/          GeocodingService.java           City name → coordinates
├── proxy/              OpenMeteoProxy.java             Feign client for weather API
│                       GeocodingProxy.java             Feign client for geocoding API
├── model/              Weather.java, DailyForecast.java
├── exception/          CityNotFoundException, GlobalExceptionHandler
└── config/             Cache, CORS, Feign, OpenAPI configuration

src/main/resources/
├── templates/          index.html, weather.html        Thymeleaf templates
├── static/css/         style.css
├── application.yml
└── cache.yml
```

---

## What I Learned

- How to chain multiple external API calls using Spring Cloud OpenFeign
- Mapping raw API codes (WMO weather codes) to human-readable descriptions
- Implementing response caching with Caffeine in Spring Boot
- Separating REST and MVC view layers with dedicated ViewModels
- Building an interactive UI with Thymeleaf, vanilla JS and CSS animations

---

## Challenges

Chaining two asynchronous API calls (geocoding → forecast) and handling failures gracefully at each step required careful exception handling. Keeping the REST layer and the Thymeleaf view layer cleanly separated while reusing the same domain service was also an interesting architectural decision.

---

## Future Improvements

- Add hourly forecast
- Store recent search history
- Improve mobile responsiveness
- Add unit/integration tests for the view layer
- Internationalise weather descriptions
