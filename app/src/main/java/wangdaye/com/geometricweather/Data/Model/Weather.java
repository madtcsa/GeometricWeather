package wangdaye.com.geometricweather.data.model;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.api.HefengWeather;
import wangdaye.com.geometricweather.data.api.JuheWeather;

/**
 * Weather.
 * */

public class Weather {
    // data
    public String realLocation;

    public String date;
    public String moon;

    public String refreshTime;
    public String location;

    public String weatherNow;
    public String weatherKindNow;
    public String tempNow;
    public String aqiLevel;

    public String[] weeks;

    public String[] weatherDays;
    public String[] weatherNights;
    public String[] weatherKindDays;
    public String[] weatherKindNights;
    public String[] windDirDays;
    public String[] windDirNights;
    public String[] windLevelDays;
    public String[] windLevelNights;

    public String[] maxiTemps;
    public String[] miniTemps;

    public String windTitle;
    public String windContent;
    public String pmTitle;
    public String pmContent;
    public String humTitle;
    public String humContent;
    public String uvTitle;
    public String uvContent;
    public String dressTitle;
    public String dressContent;
    public String coldTitle;
    public String coldContent;
    public String aqiTitle;
    public String aqiContent;
    public String washCarTitle;
    public String washCarContent;
    public String exerciseTitle;
    public String exerciseContent;

    /** <br> life cycle. */

    public Weather(String realLocation,
                   String date, String moon,
                   String refreshTime, String location,
                   String weatherNow, String weatherKindNow, String tempNow, String aqiLevel,
                   String[] weeks,
                   String[] weatherDays, String[] weatherNights, String[] weatherKindDays, String[] weatherKindNights,
                   String[] windDirDays, String[] windDirNights, String[] windLevelDays, String[] windLevelNights,
                   String[] maxiTemps, String[] miniTemps,
                   String windTitle, String windContent,
                   String pmTitle, String pmContent,
                   String humTitle, String humContent,
                   String uvTitle, String uvContent,
                   String dressTitle, String dressContent,
                   String coldTitle, String coldContent,
                   String aqiTitle, String aqiContent,
                   String washCarTitle, String washCarContent,
                   String exerciseTitle, String exerciseContent) {
        this.realLocation = realLocation;
        this.date = date;
        this.moon = moon;
        this.refreshTime = refreshTime;
        this.location = location;
        this.weatherNow = weatherNow;
        this.weatherKindNow = weatherKindNow;
        this.tempNow = tempNow;
        this.aqiLevel = aqiLevel;
        this.weeks = weeks;
        this.weatherDays = weatherDays;
        this.weatherNights = weatherNights;
        this.weatherKindDays = weatherKindDays;
        this.weatherKindNights = weatherKindNights;
        this.windDirDays = windDirDays;
        this.windDirNights = windDirNights;
        this.windLevelDays = windLevelDays;
        this.windLevelNights = windLevelNights;
        this.maxiTemps = maxiTemps;
        this.miniTemps = miniTemps;
        this.windTitle = windTitle;
        this.windContent = windContent;
        this.pmTitle = pmTitle;
        this.pmContent = pmContent;
        this.humTitle = humTitle;
        this.humContent = humContent;
        this.uvTitle = uvTitle;
        this.uvContent = uvContent;
        this.dressTitle = dressTitle;
        this.dressContent = dressContent;
        this.coldTitle = coldTitle;
        this.coldContent = coldContent;
        this.aqiTitle = aqiTitle;
        this.aqiContent = aqiContent;
        this.washCarTitle = washCarTitle;
        this.washCarContent = washCarContent;
        this.exerciseTitle = exerciseTitle;
        this.exerciseContent = exerciseContent;
    }

    public static Weather buildWeather(Context context, JuheResult result, String realLocation, boolean isDay) {
        if (result == null || !result.error_code.equals("0")) {
            return null;
        }

        String[] weeks = new String[7];
        String[] weatherDays = new String[7];
        String[] weatherNights = new String[7];
        String[] weatherKindDays = new String[7];
        String[] weatherKindNights = new String[7];
        String[] windDirDays = new String[7];
        String[] windDirNights = new String[7];
        String[] windLevelDays = new String[7];
        String[] windLevelNights = new String[7];
        String[] maxiTemps = new String[7];
        String[] miniTemps = new String[7];
        for (int i = 0; i < 7; i ++) {
            weeks[i] = context.getString(R.string.week) + result.result.data.weather.get(i).week;
            weatherDays[i] = result.result.data.weather.get(i).info.day.get(1);
            weatherNights[i] = result.result.data.weather.get(i).info.night.get(1);
            weatherKindDays[i] = JuheWeather.getWeatherKind(weatherDays[i]);
            weatherKindNights[i] = JuheWeather.getWeatherKind(weatherNights[i]);
            windDirDays[i] = result.result.data.weather.get(i).info.day.get(3);
            windDirNights[i] = result.result.data.weather.get(i).info.night.get(3);
            windLevelDays[i] = result.result.data.weather.get(i).info.day.get(4);
            windLevelNights[i] = result.result.data.weather.get(i).info.night.get(4);
            maxiTemps[i] = result.result.data.weather.get(i).info.day.get(2);
            miniTemps[i] = result.result.data.weather.get(i).info.night.get(2);
        }

        return new Weather(
                realLocation,
                result.result.data.realtime.date, result.result.data.realtime.moon,
                result.result.data.realtime.time.split(":")[0] + ":" + result.result.data.realtime.time.split(":")[1],
                result.result.data.realtime.city_name,
                result.result.data.realtime.weatherNow.weatherInfo,
                JuheWeather.getWeatherKind(result.result.data.realtime.weatherNow.weatherInfo),
                result.result.data.realtime.weatherNow.temperature,
                context.getString(R.string.air) + " " + result.result.data.air.pm25.quality,
                weeks,
                weatherDays, weatherNights, weatherKindDays, weatherKindNights,
                windDirDays, windDirNights, windLevelDays, windLevelNights, maxiTemps, miniTemps,
                isDay ?
                        windDirDays[0] + "(" + context.getString(R.string.live) + result.result.data.realtime.wind.direct + ")"
                        :
                        windDirNights[0] + "(" + context.getString(R.string.live) + result.result.data.realtime.wind.direct + ")",
                isDay ?
                        windLevelDays[0] + "(" + context.getString(R.string.live) + result.result.data.realtime.wind.power + ")"
                        :
                        windLevelNights[0] + "(" + context.getString(R.string.live) + result.result.data.realtime.wind.power + ")",
                context.getString(R.string.pm_25) + ": " + result.result.data.air.pm25.pm25 + " / "
                        + context.getString(R.string.pm_10) + ": " + result.result.data.air.pm25.pm10,
                result.result.data.air.pm25.des,
                context.getString(R.string.humidity), result.result.data.realtime.weatherNow.humidity,
                context.getString(R.string.uv) + "-" + result.result.data.life.lifeInfo.ziwaixian.get(0),
                result.result.data.life.lifeInfo.ziwaixian.get(1),
                context.getString(R.string.dressing_index) + "-" + result.result.data.life.lifeInfo.chuanyi.get(0),
                result.result.data.life.lifeInfo.chuanyi.get(1),
                context.getString(R.string.cold_index) + "-" + result.result.data.life.lifeInfo.ganmao.get(0),
                result.result.data.life.lifeInfo.ganmao.get(1),
                context.getString(R.string.aqi) + "-" + result.result.data.life.lifeInfo.wuran.get(0),
                result.result.data.life.lifeInfo.wuran.get(1),
                context.getString(R.string.wash_car_index) + "-" + result.result.data.life.lifeInfo.xiche.get(0),
                result.result.data.life.lifeInfo.xiche.get(1),
                context.getString(R.string.exercise_index) + "-" + result.result.data.life.lifeInfo.yundong.get(0),
                result.result.data.life.lifeInfo.yundong.get(1));

    }

    @SuppressLint("SimpleDateFormat")
    public static Weather buildWeather(Context context, HefengResult result, String realLocation) {
        int position = HefengWeather.getLatestDataPosition(result);
        if (result == null || !result.heWeather.get(position).status.equals("ok")) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = result.heWeather.get(position).basic.update.loc.split(" ")[0];
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(todayDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        String[] weeks = new String[7];
        int day = Integer.parseInt(week);
        for (int i = 0; i < weeks.length; i ++) {
            if (day == 1){
                weeks[i] = context.getString(R.string.week_7);
            } else if (day == 2) {
                weeks[i] = context.getString(R.string.week_1);
            } else if (day == 3) {
                weeks[i] = context.getString(R.string.week_2);
            } else if (day == 4) {
                weeks[i] = context.getString(R.string.week_3);
            } else if (day == 5) {
                weeks[i] = context.getString(R.string.week_4);
            } else if (day == 6) {
                weeks[i] = context.getString(R.string.week_5);
            } else if (day == 7) {
                weeks[i] = context.getString(R.string.week_6);
                day = 0;
            }
            day ++;
        }

        String[] weatherDays = new String[7];
        String[] weatherNights = new String[7];
        String[] weatherKindDays = new String[7];
        String[] weatherKindNights = new String[7];
        String[] windDirDays = new String[7];
        String[] windDirNights = new String[7];
        String[] windLevelDays = new String[7];
        String[] windLevelNights = new String[7];
        String[] maxiTemps = new String[7];
        String[] miniTemps = new String[7];
        for (int i = 0; i < 7; i ++) {
            weatherDays[i] = result.heWeather.get(position).daily_forecast.get(i).cond.txt_d;
            weatherDays[i] = result.heWeather.get(position).daily_forecast.get(i).cond.txt_n;
            weatherKindDays[i] = JuheWeather.getWeatherKind(result.heWeather.get(position).daily_forecast.get(i).cond.code_d);
            weatherKindNights[i] = HefengWeather.getWeatherKind(result.heWeather.get(position).daily_forecast.get(i).cond.code_n);
            windDirDays[i] = windDirNights[i] = result.heWeather.get(position).daily_forecast.get(i).wind.dir;
            windLevelDays[i] = windLevelNights[i] = result.heWeather.get(position).daily_forecast.get(0).wind.sc + context.getString(R.string.level);
            maxiTemps[i] = result.heWeather.get(position).daily_forecast.get(0).tmp.max;
            miniTemps[i] = result.heWeather.get(position).daily_forecast.get(0).tmp.min;
        }

        return new Weather(
                realLocation,
                result.heWeather.get(position).basic.update.loc.split(" ")[0], "",
                result.heWeather.get(position).basic.update.loc.split(" ")[1], result.heWeather.get(position).basic.city,
                result.heWeather.get(position).now.condNow.txt,
                HefengWeather.getWeatherKind(result.heWeather.get(position).now.condNow.code),
                result.heWeather.get(position).now.tmp, "",
                weeks,
                weatherDays, weatherNights, weatherKindDays, weatherKindNights,
                windDirDays, windDirNights, windLevelDays, windLevelNights, maxiTemps, miniTemps,
                result.heWeather.get(position).daily_forecast.get(0).wind.dir
                        + "(" + context.getString(R.string.live) + result.heWeather.get(position).now.wind.dir + ")",
                result.heWeather.get(position).daily_forecast.get(0).wind.sc + context.getString(R.string.level)
                        + "(" + result.heWeather.get(position).now.wind.sc + context.getString(R.string.level) + ")",
                context.getString(R.string.visibility), result.heWeather.get(position).now.vis + "km",
                context.getString(R.string.humidity), result.heWeather.get(position).now.hum,
                context.getString(R.string.sun_rise) + "-" + result.heWeather.get(position).daily_forecast.get(0).astro.sr,
                context.getString(R.string.sun_fall) + "-" + result.heWeather.get(position).daily_forecast.get(0).astro.ss,
                context.getString(R.string.apparent_temp), result.heWeather.get(position).now.fl + "℃",
                context.getString(R.string.cold_index), context.getString(R.string.no_data),
                context.getString(R.string.aqi), context.getString(R.string.no_data),
                context.getString(R.string.wash_car_index), context.getString(R.string.no_data),
                context.getString(R.string.exercise_index), context.getString(R.string.no_data));
    }
}
