package wangdaye.com.geometricweather.data.model;

import wangdaye.com.geometricweather.data.api.HefengWeather;

/**
 * Hourly weather.
 * */

public class HourlyWeather {
    public int[] temps;
    public float[] pops;

    public HourlyWeather(int[] temps, float[] pops) {
        this.temps = temps;
        this.pops = pops;
    }

    public static HourlyWeather buildHourlyWeather(HefengResult result) {
        int position = HefengWeather.getLatestDataPosition(result);
        int[] temps = new int[result.heWeather.get(position).hourly_forecast.size()];
        for (int i = 0; i < temps.length; i ++) {
            temps[i] = Integer.parseInt(result.heWeather.get(position).hourly_forecast.get(i).tmp);
        }
        float[] pops = new float[temps.length];
        for (int i = 0; i < temps.length; i ++) {
            pops[i] = Float.parseFloat(result.heWeather.get(position).hourly_forecast.get(i).pop);
        }
        return new HourlyWeather(temps, pops);
    }
}
