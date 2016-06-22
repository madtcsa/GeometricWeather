package wangdaye.com.geometricweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

import wangdaye.com.geometricweather.R;

/**
 * Time utils.
 * */

public class TimeUtils {
    // data
    public boolean isDay;

    /** <br> data. */

    private TimeUtils(Context context) {
        getLastDayTime(context);
    }

    public TimeUtils getDayTime(Context context, boolean writeToPreference) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        isDay = 5 < hour && hour < 19;

        if (writeToPreference) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean(context.getString(R.string.key_isDay), isDay);
            editor.apply();
        }

        return this;
    }

    public TimeUtils getLastDayTime(Context context) {
        isDay = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(
                        context.getString(R.string.key_isDay),
                        true);
        return this;
    }

    /** <br> singleton. */

    private static TimeUtils instance;

    public static synchronized TimeUtils getInstance(Context context) {
        synchronized (TimeUtils.class) {
            if (instance == null) {
                instance = new TimeUtils(context);
            }
        }
        return instance;
    }
}
