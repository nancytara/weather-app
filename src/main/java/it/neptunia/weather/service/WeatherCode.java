package it.neptunia.weather.service;

public enum WeatherCode {

    CLEAR_SKY(0, "Clear sky"),
    MAINLY_CLEAR(1, "Mainly clear"),
    PARTLY_CLOUDY(2, "Partly cloudy"),
    OVERCAST(3, "Overcast"),
    FOG(45, "Fog"),
    DEPOSITING_RIME_FOG(48, "Fog"),
    LIGHT_DRIZZLE(51, "Drizzle"),
    MODERATE_DRIZZLE(53, "Drizzle"),
    DENSE_DRIZZLE(55, "Drizzle"),
    LIGHT_FREEZING_DRIZZLE(56, "Freezing drizzle"),
    DENSE_FREEZING_DRIZZLE(57, "Freezing drizzle"),
    SLIGHT_RAIN(61, "Rain"),
    MODERATE_RAIN(63, "Rain"),
    HEAVY_RAIN(65, "Rain"),
    LIGHT_FREEZING_RAIN(66, "Freezing rain"),
    HEAVY_FREEZING_RAIN(67, "Freezing rain"),
    SLIGHT_SNOW(71, "Snow"),
    MODERATE_SNOW(73, "Snow"),
    HEAVY_SNOW(75, "Snow"),
    SNOW_GRAINS(77, "Snow grains"),
    SLIGHT_RAIN_SHOWERS(80, "Rain showers"),
    MODERATE_RAIN_SHOWERS(81, "Rain showers"),
    VIOLENT_RAIN_SHOWERS(82, "Rain showers"),
    SLIGHT_SNOW_SHOWERS(85, "Snow showers"),
    HEAVY_SNOW_SHOWERS(86, "Snow showers"),
    THUNDERSTORM(95, "Thunderstorm"),
    THUNDERSTORM_WITH_SLIGHT_HAIL(96, "Thunderstorm with hail"),
    THUNDERSTORM_WITH_HEAVY_HAIL(99, "Thunderstorm with hail");

    private final int code;
    private final String description;

    WeatherCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Restituisce una descrizione leggibile per un codice meteo WMO.
     *
     * <p>I codici WMO vengono restituiti dall'API Forecast di Open-Meteo nel campo
     * {@code weather_code}. La lista completa è definita dalla World Meteorological Organization.
     *
     * @param code codice meteo WMO (es. {@code 0} per cielo sereno, {@code 61} per pioggia)
     * @return descrizione breve in inglese (es. {@code "Clear sky"}, {@code "Rain"}),
     *         oppure {@code "Unknown"} se il codice non è riconosciuto
     *
     * @example
     * <pre>
     *   WeatherCode.describe(0);   // → "Clear sky"
     *   WeatherCode.describe(61);  // → "Rain"
     *   WeatherCode.describe(95);  // → "Thunderstorm"
     *   WeatherCode.describe(999); // → "Unknown"
     * </pre>
     */
    public static String describe(int code) {
        for (WeatherCode wc : values()) {
            if (wc.code == code) {
                return wc.description;
            }
        }
        return "Unknown";
    }
}
