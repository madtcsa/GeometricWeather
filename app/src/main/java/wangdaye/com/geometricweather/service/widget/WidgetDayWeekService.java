package wangdaye.com.geometricweather.service.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.baidu.location.LocationClient;

import java.util.Calendar;

import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.data.database.WeatherTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.ui.activity.MainActivity;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.receiver.widget.WidgetDayWeekProvider;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Widget day week service.
 * */

public class WidgetDayWeekService extends Service
        implements LocationUtils.OnRequestLocationListener, WeatherUtils.OnRequestWeatherListener {
    // widget
    private LocationClient client;

    // data
    private int startId;
    private Location location;
    private static final int REQUEST_CODE = 5;

    /** <br> life cycle. */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.client = new LocationClient(this);
        this.startId = startId;

        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.sp_widget_day_week_setting), Context.MODE_PRIVATE);
        this.location = new Location(
                sharedPreferences.getString(
                        getString(R.string.key_location),
                        getString(R.string.local)));

        int[] widgetIds = AppWidgetManager.getInstance(this)
                .getAppWidgetIds(new ComponentName(this, WidgetDayWeekProvider.class));
        if (widgetIds != null && widgetIds.length != 0) {
            if (location.location.equals(getString(R.string.local))) {
                LocationUtils.requestLocation(client, this);
            } else {
                WeatherUtils.requestWeather(this, location, location.location, true, this);
            }
            this.setAlarmIntent();
        } else {
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.stop();
    }

    /** <br> widget. */

    public static void refreshWidgetView(Context context, Weather weather) {
        if (weather == null) {
            return;
        }

        // get settings & time.
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.sp_widget_day_week_setting),
                Context.MODE_PRIVATE);
        boolean showCard = sharedPreferences.getBoolean(context.getString(R.string.key_show_card), false);
        boolean blackText = sharedPreferences.getBoolean(context.getString(R.string.key_black_text), false);
        boolean hideRefreshTime = sharedPreferences.getBoolean(context.getString(R.string.key_hide_refresh_time), false);
        boolean isDay = TimeUtils.getInstance(context).getDayTime(context, false).isDay;

        // get text color.
        int textColor;
        if (blackText || showCard) {
            textColor = ContextCompat.getColor(context, R.color.colorTextDark);
        } else {
            textColor = ContextCompat.getColor(context, R.color.colorTextLight);
        }

        // get remote views.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_day_week);

        // build view.
        int[] imageId = WeatherUtils.getWeatherIcon( // get icon resource id.
                weather.weatherKindNow,
                isDay);
        views.setImageViewResource( // set icon.
                R.id.widget_day_week_icon,
                imageId[3]);
        // build weather & temps text.
        String[] texts = WidgetAndNotificationUtils.buildWidgetDayStyleText(weather);
        views.setTextViewText( // set weather.
                R.id.widget_day_week_weather,
                texts[0]);
        views.setTextViewText( // set temps.
                R.id.widget_day_week_temp,
                texts[1]);
        views.setTextViewText( // set time.
                R.id.widget_day_week_refreshTime,
                weather.location + "." + weather.refreshTime);
        // set week icons.
        views.setImageViewResource(
                R.id.widget_day_week_icon_1,
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[0] : weather.weatherKindNights[0],
                        isDay)[3]);
        views.setImageViewResource(
                R.id.widget_day_week_icon_2,
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[1] : weather.weatherKindNights[1],
                        isDay)[3]);
        views.setImageViewResource(
                R.id.widget_day_week_icon_3,
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[2] : weather.weatherKindNights[2],
                        isDay)[3]);
        views.setImageViewResource(
                R.id.widget_day_week_icon_4,
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[3] : weather.weatherKindNights[3],
                        isDay)[3]);
        views.setImageViewResource(
                R.id.widget_day_week_icon_5,
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[4] : weather.weatherKindNights[4],
                        isDay)[3]);
        // build week texts.
        String firstWeekDay;
        String secondWeekDay;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String[] weatherDates = weather.date.split("-");
        if (Integer.parseInt(weatherDates[0]) == year
                && Integer.parseInt(weatherDates[1]) == month
                && Integer.parseInt(weatherDates[2]) == day) {
            firstWeekDay = context.getString(R.string.today);
            secondWeekDay = weather.weeks[1];
        } else if (Integer.parseInt(weatherDates[0]) == year
                && Integer.parseInt(weatherDates[1]) == month
                && Integer.parseInt(weatherDates[2]) == day - 1) {
            firstWeekDay = context.getString(R.string.yesterday);
            secondWeekDay = context.getString(R.string.today);
        } else {
            firstWeekDay = weather.weeks[0];
            secondWeekDay = weather.weeks[1];
        }
        // set week texts.
        views.setTextViewText(
                R.id.widget_day_week_week_1,
                firstWeekDay);
        views.setTextViewText(
                R.id.widget_day_week_week_2,
                secondWeekDay);
        views.setTextViewText(
                R.id.widget_day_week_week_3,
                weather.weeks[2]);
        views.setTextViewText(
                R.id.widget_day_week_week_4,
                weather.weeks[3]);
        views.setTextViewText(
                R.id.widget_day_week_week_5,
                weather.weeks[4]);
        // set temps texts.
        views.setTextViewText(
                R.id.widget_day_week_temp_1,
                weather.miniTemps[0] + "/" + weather.maxiTemps[0] + "°");
        views.setTextViewText(
                R.id.widget_day_week_temp_2,
                weather.miniTemps[1] + "/" + weather.maxiTemps[1] + "°");
        views.setTextViewText(
                R.id.widget_day_week_temp_3,
                weather.miniTemps[2] + "/" + weather.maxiTemps[2] + "°");
        views.setTextViewText(
                R.id.widget_day_week_temp_4,
                weather.miniTemps[3] + "/" + weather.maxiTemps[3] + "°");
        views.setTextViewText(
                R.id.widget_day_week_temp_5,
                weather.miniTemps[4] + "/" + weather.maxiTemps[4] + "°");
        // set text color.
        views.setTextColor(R.id.widget_day_week_weather, textColor);
        views.setTextColor(R.id.widget_day_week_temp, textColor);
        views.setTextColor(R.id.widget_day_week_refreshTime, textColor);
        views.setTextColor(R.id.widget_day_week_week_1, textColor);
        views.setTextColor(R.id.widget_day_week_week_2, textColor);
        views.setTextColor(R.id.widget_day_week_week_3, textColor);
        views.setTextColor(R.id.widget_day_week_week_4, textColor);
        views.setTextColor(R.id.widget_day_week_week_5, textColor);
        views.setTextColor(R.id.widget_day_week_temp_1, textColor);
        views.setTextColor(R.id.widget_day_week_temp_2, textColor);
        views.setTextColor(R.id.widget_day_week_temp_3, textColor);
        views.setTextColor(R.id.widget_day_week_temp_4, textColor);
        views.setTextColor(R.id.widget_day_week_temp_5, textColor);
        // set card visibility.
        views.setViewVisibility(R.id.widget_day_week_card, showCard ? View.VISIBLE : View.GONE);
        // set refresh time visibility.
        views.setViewVisibility(R.id.widget_day_week_refreshTime, hideRefreshTime ? View.GONE : View.VISIBLE);
        // set clock intent.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_day_week_button, pendingIntent);

        // commit.
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(
                new ComponentName(context, WidgetDayWeekProvider.class),
                views);
    }

    /** <br> data. */

    private void setAlarmIntent() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent target = new Intent(getBaseContext(), WidgetDayWeekService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                getBaseContext(),
                REQUEST_CODE,
                target,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int duration = 1000 * 60 * 60 * WidgetAndNotificationUtils.getWidgetRefreshHours(this);
        alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + duration,
                pendingIntent);
    }

    /** <br> interface. */

    // request location.

    @Override
    public void requestLocationSuccess(String locationName) {
        WeatherUtils.requestWeather(this, location, locationName, true, this);
    }

    @Override
    public void requestLocationFailed() {
        LocationUtils.simpleLocationFailedFeedback(this);
        refreshWidgetView(this,
                WeatherTable.readWeather(
                        MyDatabaseHelper.getDatabaseHelper(this),
                        location.location));
        this.stopSelf(startId);
    }

    // request weather.

    @Override
    public void requestWeatherSuccess(Location result, boolean isDaily) {
        refreshWidgetView(this, result.weather);
        this.stopSelf(startId);
    }

    @Override
    public void requestWeatherFailed(Location result, boolean isDaily) {
        WidgetAndNotificationUtils.refreshWidgetFailed(this);
        refreshWidgetView(this,
                WeatherTable.readWeather(
                        MyDatabaseHelper.getDatabaseHelper(this),
                        location.location));
        this.stopSelf(startId);
    }
}