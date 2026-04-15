package it.neptunia.weather.view.controller;

import it.neptunia.weather.model.DailyForecast;
import it.neptunia.weather.model.Weather;
import it.neptunia.weather.service.WeatherService;
import it.neptunia.weather.view.WeatherIconMapper;
import it.neptunia.weather.view.model.CurrentWeatherView;
import it.neptunia.weather.view.model.DailyForecastView;
import it.neptunia.weather.view.model.WeatherPageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WeatherViewController {

    private static final List<String> FEATURED_CITIES =
            List.of("Roma", "Milano", "Napoli", "Torino", "Firenze");

    private static final DateTimeFormatter DISPLAY_DATE_FMT =
            DateTimeFormatter.ofPattern("EEEE d", Locale.ENGLISH);

    private final WeatherService weatherService;

    @GetMapping("/")
    public String index(Model model) {
        List<CurrentWeatherView> cityWidgets = FEATURED_CITIES.stream()
                .map(city -> {
                    try {
                        return Optional.of(toCurrentView(weatherService.getWeather(city)));
                    } catch (Exception e) {
                        return Optional.<CurrentWeatherView>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        model.addAttribute("cityWidgets", cityWidgets);
        return "index";
    }

    @GetMapping("/view/weather")
    public String weather(@RequestParam String city, Model model, RedirectAttributes redirectAttrs) {
        try {
            Weather current = weatherService.getWeather(city);
            List<DailyForecast> forecast = weatherService.getWeatherForecast(city, 7);

            WeatherPageView page = WeatherPageView.builder()
                    .current(toCurrentView(current))
                    .forecast(forecast.stream().map(this::toDailyView).toList())
                    .build();

            model.addAttribute("weather", page);
            return "weather";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Could not load weather for \"" + city + "\".");
            return "redirect:/";
        }
    }

    private CurrentWeatherView toCurrentView(Weather w) {
        return CurrentWeatherView.builder()
                .city(w.getCity())
                .temperature(w.getTemperature())
                .windspeed(w.getWindspeed())
                .humidity(w.getHumidity())
                .precipitation(w.getPrecipitation())
                .weatherDescription(w.getWeatherDescription())
                .icon(WeatherIconMapper.iconFor(w.getWeatherDescription()))
                .build();
    }

    private DailyForecastView toDailyView(DailyForecast d) {
        String displayDate = LocalDate.parse(d.getDate()).format(DISPLAY_DATE_FMT);
        return DailyForecastView.builder()
                .displayDate(displayDate)
                .maxTemperature(d.getMaxTemperature())
                .minTemperature(d.getMinTemperature())
                .precipitation(d.getPrecipitation())
                .weatherDescription(d.getWeatherDescription())
                .icon(WeatherIconMapper.iconFor(d.getWeatherDescription()))
                .build();
    }
}
