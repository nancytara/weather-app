package it.neptunia.weather.view;

public class WeatherIconMapper {

    private WeatherIconMapper() {}

    public static String iconFor(String description) {
        if (description == null) return "🌡️";
        return switch (description) {
            case "Clear sky"               -> "☀️";
            case "Mainly clear"            -> "🌤️";
            case "Partly cloudy"           -> "⛅";
            case "Overcast"                -> "☁️";
            case "Fog"                     -> "🌫️";
            case "Drizzle"                 -> "🌦️";
            case "Freezing drizzle"        -> "🌨️";
            case "Rain"                    -> "🌧️";
            case "Freezing rain"           -> "🌨️";
            case "Snow", "Snow grains"     -> "❄️";
            case "Rain showers"            -> "🌧️";
            case "Snow showers"            -> "🌨️";
            case "Thunderstorm",
                 "Thunderstorm with hail"  -> "⛈️";
            default                        -> "🌡️";
        };
    }
}
