package wangdaye.com.geometricweather.data.model;

/**
 * Location.
 * */

public class Location {
    // data
    public String location;
    public Weather weather;
    public HourlyWeather hourlyWeather;
    public History history;

    public Location(String location) {
        this(location, null, null, null);
    }

    public Location(String location, Weather weather) {
        this(location, weather, null, null);
    }

    public Location(String location, Weather weather, HourlyWeather hourlyWeather) {
        this(location, weather, hourlyWeather, null);
    }

    public Location(String location, Weather weather, HourlyWeather hourlyWeather, History history) {
        this.location = location;
        this.weather = weather;
        this.hourlyWeather = hourlyWeather;
        this.history = history;
    }

    public static boolean isEngLocation(String locationName) {
        return locationName.replaceAll(" ", "").matches("[a-zA-Z]+");
    }
}
