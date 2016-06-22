package wangdaye.com.geometricweather.data.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wangdaye.com.geometricweather.data.model.History;
import wangdaye.com.geometricweather.data.model.Weather;

/**
 * History table.
 * */

public class HistoryTable {
    // data
    public static final String TABLE_HISTORY = "WeatherHistory";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_WEATHER = "weather";
    public static final String COLUMN_MAXI_TEMP = "maxiTemp";
    public static final String COLUMN_MINI_TEMP = "miniTemp";

    public static final String CREATE_TABLE_HISTORY = "CREATE TABLE WeatherHistory ("
            + "location     text,"
            + "time         text,"
            + "weather      text,"
            + "maxiTemp     text,"
            + "miniTemp     text)";

    @SuppressLint("SimpleDateFormat")
    public static void writeTodayWeather(MyDatabaseHelper databaseHelper, Weather weather) {
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDate = simpleDateFormat.format(date);

        History yesterdayHistory = null;

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_HISTORY,
                null,
                COLUMN_LOCATION + " = '" + weather.location
                        + "' AND "
                        + COLUMN_TIME + " = '" + yesterdayDate + "'",
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()) {
            do {
                String locationText = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                String weatherText = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER));
                String maxiTempText = cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP));
                String miniTempText = cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP));
                String timeText = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                yesterdayHistory = new History(locationText, weatherText, maxiTempText, miniTempText, timeText);
            } while (cursor.moveToNext());
        }
        cursor.close();

        database.delete(TABLE_HISTORY,
                COLUMN_LOCATION + " = ?",
                new String[]{weather.location});

        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, weather.location);
        values.put(COLUMN_WEATHER, weather.weatherNow);
        values.put(COLUMN_MAXI_TEMP, weather.maxiTemps[0]);
        values.put(COLUMN_MINI_TEMP, weather.miniTemps[0]);
        values.put(COLUMN_TIME, weather.date);
        database.insert(TABLE_HISTORY, null, values);
        values.clear();
        if (yesterdayHistory != null) {
            values.put(COLUMN_LOCATION, yesterdayHistory.location);
            values.put(COLUMN_WEATHER, yesterdayHistory.weather);
            values.put(COLUMN_MAXI_TEMP, yesterdayHistory.maxiTemp);
            values.put(COLUMN_MINI_TEMP, yesterdayHistory.miniTemp);
            values.put(COLUMN_TIME, yesterdayHistory.time);
            database.insert(TABLE_HISTORY, null, values);
            values.clear();
        }
        database.close();
    }

    @SuppressLint("SimpleDateFormat")
    public static History readYesterdayWeather(MyDatabaseHelper databaseHelper, Weather weather) {
        if (weather == null) {
            return null;
        }
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDate = simpleDateFormat.format(date);

        History yesterdayHistory = null;
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_HISTORY,
                null,
                COLUMN_LOCATION + " = '" + weather.location
                        + "' AND "
                        + COLUMN_TIME + " = '" + yesterdayDate + "'",
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()) {
            do {
                String locationText = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                String weatherText = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER));
                String maxiTempText = cursor.getString(cursor.getColumnIndex(COLUMN_MAXI_TEMP));
                String miniTempText = cursor.getString(cursor.getColumnIndex(COLUMN_MINI_TEMP));
                String timeText = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                yesterdayHistory = new History(locationText, weatherText, maxiTempText, miniTempText, timeText);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return yesterdayHistory;
    }
}
