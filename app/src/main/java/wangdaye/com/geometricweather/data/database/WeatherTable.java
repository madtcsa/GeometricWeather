package wangdaye.com.geometricweather.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import wangdaye.com.geometricweather.data.model.Weather;

/**
 * History table.
 * */

public class WeatherTable {
    // data
    public static final String TABLE_WEATHER = "History";
    public static final String COLUMN_REAL_LOCATION = "realLocation";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MOON = "moon";
    public static final String COLUMN_REFRESH_TIME = "refreshTime";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_WEATHER_NOW = "weatherNow";
    public static final String COLUMN_WEATHER_KIND_NOW = "weatherKindNow";
    public static final String COLUMN_TEMP_NOW = "tempNow";
    public static final String COLUMN_AQI_LEVEL = "aqiLevel";
    public static final String COLUMN_WEEK_1 =  "week1";
    public static final String COLUMN_WEEK_2 =  "week2";
    public static final String COLUMN_WEEK_3 =  "week3";
    public static final String COLUMN_WEEK_4 =  "week4";
    public static final String COLUMN_WEEK_5 =  "week5";
    public static final String COLUMN_WEEK_6 =  "week6";
    public static final String COLUMN_WEEK_7 =  "week7";
    public static final String COLUMN_WEATHER_DAY_1 = "weatherDay1";
    public static final String COLUMN_WEATHER_DAY_2 = "weatherDay2";
    public static final String COLUMN_WEATHER_DAY_3 = "weatherDay3";
    public static final String COLUMN_WEATHER_DAY_4 = "weatherDay4";
    public static final String COLUMN_WEATHER_DAY_5 = "weatherDay5";
    public static final String COLUMN_WEATHER_DAY_6 = "weatherDay6";
    public static final String COLUMN_WEATHER_DAY_7 = "weatherDay7";
    public static final String COLUMN_WEATHER_NIGHT_1 = "weatherNight1";
    public static final String COLUMN_WEATHER_NIGHT_2 = "weatherNight2";
    public static final String COLUMN_WEATHER_NIGHT_3 = "weatherNight3";
    public static final String COLUMN_WEATHER_NIGHT_4 = "weatherNight4";
    public static final String COLUMN_WEATHER_NIGHT_5 = "weatherNight5";
    public static final String COLUMN_WEATHER_NIGHT_6 = "weatherNight6";
    public static final String COLUMN_WEATHER_NIGHT_7 = "weatherNight7";
    public static final String COLUMN_WEATHER_KIND_DAY_1 = "weatherKindDay1";
    public static final String COLUMN_WEATHER_KIND_DAY_2 = "weatherKindDay2";
    public static final String COLUMN_WEATHER_KIND_DAY_3 = "weatherKindDay3";
    public static final String COLUMN_WEATHER_KIND_DAY_4 = "weatherKindDay4";
    public static final String COLUMN_WEATHER_KIND_DAY_5 = "weatherKindDay5";
    public static final String COLUMN_WEATHER_KIND_DAY_6 = "weatherKindDay6";
    public static final String COLUMN_WEATHER_KIND_DAY_7 = "weatherKindDay7";
    public static final String COLUMN_WEATHER_KIND_NIGHT_1 = "weatherKindNight1";
    public static final String COLUMN_WEATHER_KIND_NIGHT_2 = "weatherKindNight2";
    public static final String COLUMN_WEATHER_KIND_NIGHT_3 = "weatherKindNight3";
    public static final String COLUMN_WEATHER_KIND_NIGHT_4 = "weatherKindNight4";
    public static final String COLUMN_WEATHER_KIND_NIGHT_5 = "weatherKindNight5";
    public static final String COLUMN_WEATHER_KIND_NIGHT_6 = "weatherKindNight6";
    public static final String COLUMN_WEATHER_KIND_NIGHT_7 = "weatherKindNight7";
    public static final String COLUMN_WIND_DIR_DAY_1 = "windDirDay1";
    public static final String COLUMN_WIND_DIR_DAY_2 = "windDirDay2";
    public static final String COLUMN_WIND_DIR_DAY_3 = "windDirDay3";
    public static final String COLUMN_WIND_DIR_DAY_4 = "windDirDay4";
    public static final String COLUMN_WIND_DIR_DAY_5 = "windDirDay5";
    public static final String COLUMN_WIND_DIR_DAY_6 = "windDirDay6";
    public static final String COLUMN_WIND_DIR_DAY_7 = "windDirDay7";
    public static final String COLUMN_WIND_DIR_NIGHT_1 = "windDirNight1";
    public static final String COLUMN_WIND_DIR_NIGHT_2 = "windDirNight2";
    public static final String COLUMN_WIND_DIR_NIGHT_3 = "windDirNight3";
    public static final String COLUMN_WIND_DIR_NIGHT_4 = "windDirNight4";
    public static final String COLUMN_WIND_DIR_NIGHT_5 = "windDirNight5";
    public static final String COLUMN_WIND_DIR_NIGHT_6 = "windDirNight6";
    public static final String COLUMN_WIND_DIR_NIGHT_7 = "windDirNight7";
    public static final String COLUMN_WIND_LEVEL_DAY_1 = "windLevelDay1";
    public static final String COLUMN_WIND_LEVEL_DAY_2 = "windLevelDay2";
    public static final String COLUMN_WIND_LEVEL_DAY_3 = "windLevelDay3";
    public static final String COLUMN_WIND_LEVEL_DAY_4 = "windLevelDay4";
    public static final String COLUMN_WIND_LEVEL_DAY_5 = "windLevelDay5";
    public static final String COLUMN_WIND_LEVEL_DAY_6 = "windLevelDay6";
    public static final String COLUMN_WIND_LEVEL_DAY_7 = "windLevelDay7";
    public static final String COLUMN_WIND_LEVEL_NIGHT_1 = "windLevelNight1";
    public static final String COLUMN_WIND_LEVEL_NIGHT_2 = "windLevelNight2";
    public static final String COLUMN_WIND_LEVEL_NIGHT_3 = "windLevelNight3";
    public static final String COLUMN_WIND_LEVEL_NIGHT_4 = "windLevelNight4";
    public static final String COLUMN_WIND_LEVEL_NIGHT_5 = "windLevelNight5";
    public static final String COLUMN_WIND_LEVEL_NIGHT_6 = "windLevelNight6";
    public static final String COLUMN_WIND_LEVEL_NIGHT_7 = "windLevelNight7";
    public static final String COLUMN_MAXI_TEMP_1 = "maxiTemp1";
    public static final String COLUMN_MAXI_TEMP_2 = "maxiTemp2";
    public static final String COLUMN_MAXI_TEMP_3 = "maxiTemp3";
    public static final String COLUMN_MAXI_TEMP_4 = "maxiTemp4";
    public static final String COLUMN_MAXI_TEMP_5 = "maxiTemp5";
    public static final String COLUMN_MAXI_TEMP_6 = "maxiTemp6";
    public static final String COLUMN_MAXI_TEMP_7 = "maxiTemp7";
    public static final String COLUMN_MINI_TEMP_1 = "miniTemp1";
    public static final String COLUMN_MINI_TEMP_2 = "miniTemp2";
    public static final String COLUMN_MINI_TEMP_3 = "miniTemp3";
    public static final String COLUMN_MINI_TEMP_4 = "miniTemp4";
    public static final String COLUMN_MINI_TEMP_5 = "miniTemp5";
    public static final String COLUMN_MINI_TEMP_6 = "miniTemp6";
    public static final String COLUMN_MINI_TEMP_7 = "miniTemp7";
    public static final String COLUMN_WIND_TITLE = "windTitle";
    public static final String COLUMN_WIND_CONTENT = "windContent";
    public static final String COLUMN_PM_TITLE = "pmTitle";
    public static final String COLUMN_PM_CONTENT = "pmContent";
    public static final String COLUMN_HUM_TITLE = "humTitle";
    public static final String COLUMN_HUM_CONTENT = "humContent";
    public static final String COLUMN_UV_TITLE = "uvTitle";
    public static final String COLUMN_UV_CONTENT = "uvContent";
    public static final String COLUMN_DRESS_TITLE = "dressTitle";
    public static final String COLUMN_DRESS_CONTENT = "dressContent";
    public static final String COLUMN_COLD_TITLE = "coldTitle";
    public static final String COLUMN_COLD_CONTENT = "coldContent";
    public static final String COLUMN_AQI_TITLE = "aqiTitle";
    public static final String COLUMN_AQI_CONTENT = "aqiContent";
    public static final String COLUMN_WASH_CAR_TITLE = "washCarTitle";
    public static final String COLUMN_WASH_CAR_CONTENT = "washCarContent";
    public static final String COLUMN_EXERCISE_TITLE = "exerciseTitle";
    public static final String COLUMN_EXERCISE_CONTENT = "exerciseContent";

    public static final String CREATE_TABLE_WEATHER = "CREATE TABLE History ("
            + "realLocation         text    PRIMARY KEY,"
            + "date                 text,"
            + "moon                 text,"
            + "refreshTime          text,"
            + "location             text,"
            + "weatherNow           text,"
            + "weatherKindNow       text,"
            + "tempNow              text,"
            + "aqiLevel             text,"
            + "week1                text,"
            + "week2                text,"
            + "week3                text,"
            + "week4                text,"
            + "week5                text,"
            + "week6                text,"
            + "week7                text,"
            + "weatherDay1          text,"
            + "weatherDay2          text,"
            + "weatherDay3          text,"
            + "weatherDay4          text,"
            + "weatherDay5          text,"
            + "weatherDay6          text,"
            + "weatherDay7          text,"
            + "weatherNight1        text,"
            + "weatherNight2        text,"
            + "weatherNight3        text,"
            + "weatherNight4        text,"
            + "weatherNight5        text,"
            + "weatherNight6        text,"
            + "weatherNight7        text,"
            + "weatherKindDay1      text,"
            + "weatherKindDay2      text,"
            + "weatherKindDay3      text,"
            + "weatherKindDay4      text,"
            + "weatherKindDay5      text,"
            + "weatherKindDay6      text,"
            + "weatherKindDay7      text,"
            + "weatherKindNight1    text,"
            + "weatherKindNight2    text,"
            + "weatherKindNight3    text,"
            + "weatherKindNight4    text,"
            + "weatherKindNight5    text,"
            + "weatherKindNight6    text,"
            + "weatherKindNight7    text,"
            + "windDirDay1          text,"
            + "windDirDay2          text,"
            + "windDirDay3          text,"
            + "windDirDay4          text,"
            + "windDirDay5          text,"
            + "windDirDay6          text,"
            + "windDirDay7          text,"
            + "windDirNight1        text,"
            + "windDirNight2        text,"
            + "windDirNight3        text,"
            + "windDirNight4        text,"
            + "windDirNight5        text,"
            + "windDirNight6        text,"
            + "windDirNight7        text,"
            + "windLevelDay1        text,"
            + "windLevelDay2        text,"
            + "windLevelDay3        text,"
            + "windLevelDay4        text,"
            + "windLevelDay5        text,"
            + "windLevelDay6        text,"
            + "windLevelDay7        text,"
            + "windLevelNight1      text,"
            + "windLevelNight2      text,"
            + "windLevelNight3      text,"
            + "windLevelNight4      text,"
            + "windLevelNight5      text,"
            + "windLevelNight6      text,"
            + "windLevelNight7      text,"
            + "maxiTemp1            text,"
            + "maxiTemp2            text,"
            + "maxiTemp3            text,"
            + "maxiTemp4            text,"
            + "maxiTemp5            text,"
            + "maxiTemp6            text,"
            + "maxiTemp7            text,"
            + "miniTemp1            text,"
            + "miniTemp2            text,"
            + "miniTemp3            text,"
            + "miniTemp4            text,"
            + "miniTemp5            text,"
            + "miniTemp6            text,"
            + "miniTemp7            text,"
            + "windTitle            text,"
            + "windContent          text,"
            + "pmTitle              text,"
            + "pmContent            text,"
            + "humTitle             text,"
            + "humContent           text,"
            + "uvTitle              text,"
            + "uvContent            text,"
            + "dressTitle           text,"
            + "dressContent         text,"
            + "coldTitle            text,"
            + "coldContent          text,"
            + "aqiTitle             text,"
            + "aqiContent           text,"
            + "washCarTitle         text,"
            + "washCarContent       text,"
            + "exerciseTitle        text,"
            + "exerciseContent      text)";

    public static Weather readWeather(MyDatabaseHelper databaseHelper, String realLocation) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_WEATHER,
                null,
                COLUMN_REAL_LOCATION + " = '" + realLocation + "'",
                null,
                null,
                null,
                null);
        Weather weather;
        if(cursor.moveToFirst()) {
            do {
                weather = new Weather(cursor.getString(cursor.getColumnIndex(COLUMN_REAL_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MOON)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_REFRESH_TIME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NOW)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NOW)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TEMP_NOW)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AQI_LEVEL)),
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEEK_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DAY_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_NIGHT_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_DAY_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_KIND_NIGHT_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_DAY_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR_NIGHT_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_DAY_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_LEVEL_NIGHT_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP_7))
                        },
                        new String[] {
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_1)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_2)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_3)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_4)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_5)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_6)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP_7))
                        },
                        cursor.getString(cursor.getColumnIndex(COLUMN_WIND_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_WIND_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PM_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PM_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_HUM_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_HUM_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_UV_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_UV_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DRESS_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DRESS_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COLD_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COLD_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AQI_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AQI_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_WASH_CAR_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_WASH_CAR_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_CONTENT)));
            } while (cursor.moveToNext());
        } else {
            return null;
        }
        cursor.close();
        database.close();
        return weather;
    }

    public static void writeWeather(MyDatabaseHelper databaseHelper, Weather weather) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete(TABLE_WEATHER,
                COLUMN_REAL_LOCATION + " = ?",
                new String[]{weather.realLocation});

        ContentValues values = new ContentValues();
        values.put(COLUMN_REAL_LOCATION, weather.realLocation);
        values.put(COLUMN_DATE, weather.date);
        values.put(COLUMN_MOON, weather.moon);
        values.put(COLUMN_REFRESH_TIME, weather.refreshTime);
        values.put(COLUMN_LOCATION, weather.location);
        values.put(COLUMN_WEATHER_NOW, weather.weatherNow);
        values.put(COLUMN_WEATHER_KIND_NOW, weather.weatherKindNow);
        values.put(COLUMN_TEMP_NOW, weather.tempNow);
        values.put(COLUMN_AQI_LEVEL, weather.aqiLevel);
        values.put(COLUMN_WEEK_1, weather.weeks[0]);
        values.put(COLUMN_WEEK_2, weather.weeks[1]);
        values.put(COLUMN_WEEK_3, weather.weeks[2]);
        values.put(COLUMN_WEEK_4, weather.weeks[3]);
        values.put(COLUMN_WEEK_5, weather.weeks[4]);
        values.put(COLUMN_WEEK_6, weather.weeks[5]);
        values.put(COLUMN_WEEK_7, weather.weeks[6]);
        values.put(COLUMN_WEATHER_DAY_1, weather.weatherDays[0]);
        values.put(COLUMN_WEATHER_DAY_2, weather.weatherDays[1]);
        values.put(COLUMN_WEATHER_DAY_3, weather.weatherDays[2]);
        values.put(COLUMN_WEATHER_DAY_4, weather.weatherDays[3]);
        values.put(COLUMN_WEATHER_DAY_5, weather.weatherDays[4]);
        values.put(COLUMN_WEATHER_DAY_6, weather.weatherDays[5]);
        values.put(COLUMN_WEATHER_DAY_7, weather.weatherDays[6]);
        values.put(COLUMN_WEATHER_NIGHT_1, weather.weatherNights[0]);
        values.put(COLUMN_WEATHER_NIGHT_2, weather.weatherNights[1]);
        values.put(COLUMN_WEATHER_NIGHT_3, weather.weatherNights[2]);
        values.put(COLUMN_WEATHER_NIGHT_4, weather.weatherNights[3]);
        values.put(COLUMN_WEATHER_NIGHT_5, weather.weatherNights[4]);
        values.put(COLUMN_WEATHER_NIGHT_6, weather.weatherNights[5]);
        values.put(COLUMN_WEATHER_NIGHT_7, weather.weatherNights[6]);
        values.put(COLUMN_WEATHER_KIND_DAY_1, weather.weatherKindDays[0]);
        values.put(COLUMN_WEATHER_KIND_DAY_2, weather.weatherKindDays[1]);
        values.put(COLUMN_WEATHER_KIND_DAY_3, weather.weatherKindDays[2]);
        values.put(COLUMN_WEATHER_KIND_DAY_4, weather.weatherKindDays[3]);
        values.put(COLUMN_WEATHER_KIND_DAY_5, weather.weatherKindDays[4]);
        values.put(COLUMN_WEATHER_KIND_DAY_6, weather.weatherKindDays[5]);
        values.put(COLUMN_WEATHER_KIND_DAY_7, weather.weatherKindDays[6]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_1, weather.weatherKindNights[0]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_2, weather.weatherKindNights[1]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_3, weather.weatherKindNights[2]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_4, weather.weatherKindNights[3]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_5, weather.weatherKindNights[4]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_6, weather.weatherKindNights[5]);
        values.put(COLUMN_WEATHER_KIND_NIGHT_7, weather.weatherKindNights[6]);
        values.put(COLUMN_WIND_DIR_DAY_1, weather.windDirDays[0]);
        values.put(COLUMN_WIND_DIR_DAY_2, weather.windDirDays[1]);
        values.put(COLUMN_WIND_DIR_DAY_3, weather.windDirDays[2]);
        values.put(COLUMN_WIND_DIR_DAY_4, weather.windDirDays[3]);
        values.put(COLUMN_WIND_DIR_DAY_5, weather.windDirDays[4]);
        values.put(COLUMN_WIND_DIR_DAY_6, weather.windDirDays[5]);
        values.put(COLUMN_WIND_DIR_DAY_7, weather.windDirDays[6]);
        values.put(COLUMN_WIND_DIR_NIGHT_1, weather.windDirNights[0]);
        values.put(COLUMN_WIND_DIR_NIGHT_2, weather.windDirNights[1]);
        values.put(COLUMN_WIND_DIR_NIGHT_3, weather.windDirNights[2]);
        values.put(COLUMN_WIND_DIR_NIGHT_4, weather.windDirNights[3]);
        values.put(COLUMN_WIND_DIR_NIGHT_5, weather.windDirNights[4]);
        values.put(COLUMN_WIND_DIR_NIGHT_6, weather.windDirNights[5]);
        values.put(COLUMN_WIND_DIR_NIGHT_7, weather.windDirNights[6]);
        values.put(COLUMN_WIND_LEVEL_DAY_1, weather.windLevelDays[0]);
        values.put(COLUMN_WIND_LEVEL_DAY_2, weather.windLevelDays[1]);
        values.put(COLUMN_WIND_LEVEL_DAY_3, weather.windLevelDays[2]);
        values.put(COLUMN_WIND_LEVEL_DAY_4, weather.windLevelDays[3]);
        values.put(COLUMN_WIND_LEVEL_DAY_5, weather.windLevelDays[4]);
        values.put(COLUMN_WIND_LEVEL_DAY_6, weather.windLevelDays[5]);
        values.put(COLUMN_WIND_LEVEL_DAY_7, weather.windLevelDays[6]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_1, weather.windLevelNights[0]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_2, weather.windLevelNights[1]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_3, weather.windLevelNights[2]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_4, weather.windLevelNights[3]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_5, weather.windLevelNights[4]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_6, weather.windLevelNights[5]);
        values.put(COLUMN_WIND_LEVEL_NIGHT_7, weather.windLevelNights[6]);
        values.put(COLUMN_MAXI_TEMP_1, weather.maxiTemps[0]);
        values.put(COLUMN_MAXI_TEMP_2, weather.maxiTemps[1]);
        values.put(COLUMN_MAXI_TEMP_3, weather.maxiTemps[2]);
        values.put(COLUMN_MAXI_TEMP_4, weather.maxiTemps[3]);
        values.put(COLUMN_MAXI_TEMP_5, weather.maxiTemps[4]);
        values.put(COLUMN_MAXI_TEMP_6, weather.maxiTemps[5]);
        values.put(COLUMN_MAXI_TEMP_7, weather.maxiTemps[6]);
        values.put(COLUMN_MINI_TEMP_1, weather.miniTemps[0]);
        values.put(COLUMN_MINI_TEMP_2, weather.miniTemps[1]);
        values.put(COLUMN_MINI_TEMP_3, weather.miniTemps[2]);
        values.put(COLUMN_MINI_TEMP_4, weather.miniTemps[3]);
        values.put(COLUMN_MINI_TEMP_5, weather.miniTemps[4]);
        values.put(COLUMN_MINI_TEMP_6, weather.miniTemps[5]);
        values.put(COLUMN_MINI_TEMP_7, weather.miniTemps[6]);
        values.put(COLUMN_WIND_TITLE, weather.windTitle);
        values.put(COLUMN_WIND_CONTENT, weather.windContent);
        values.put(COLUMN_PM_TITLE, weather.pmTitle);
        values.put(COLUMN_PM_CONTENT, weather.pmContent);
        values.put(COLUMN_HUM_TITLE, weather.humTitle);
        values.put(COLUMN_HUM_CONTENT, weather.humContent);
        values.put(COLUMN_UV_TITLE, weather.uvTitle);
        values.put(COLUMN_UV_CONTENT, weather.uvContent);
        values.put(COLUMN_DRESS_TITLE, weather.dressTitle);
        values.put(COLUMN_DRESS_CONTENT, weather.dressContent);
        values.put(COLUMN_COLD_TITLE, weather.coldTitle);
        values.put(COLUMN_COLD_CONTENT, weather.coldContent);
        values.put(COLUMN_AQI_TITLE, weather.aqiTitle);
        values.put(COLUMN_AQI_CONTENT, weather.aqiContent);
        values.put(COLUMN_WASH_CAR_TITLE, weather.washCarTitle);
        values.put(COLUMN_WASH_CAR_CONTENT, weather.washCarContent);
        values.put(COLUMN_EXERCISE_TITLE, weather.exerciseTitle);
        values.put(COLUMN_EXERCISE_CONTENT, weather.exerciseContent);
        database.insert(TABLE_WEATHER, null, values);
        values.clear();
        database.close();
    }
}
