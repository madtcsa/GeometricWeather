package wangdaye.com.geometricweather.service.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.baidu.location.LocationClient;

import wangdaye.com.geometricweather.data.database.LocationTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Send notification.
 * */

public class NotificationService extends Service
        implements LocationUtils.OnRequestLocationListener, WeatherUtils.OnRequestWeatherListener {
    // widget
    private LocationClient client;

    // data
    private int startId;
    private Location location;
    private static final int REQUEST_CODE = 7;

    /** <br> life cycle. */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public  int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificationSwitchOn = sharedPreferences.getBoolean(getString(R.string.key_notification_switch), false);
        boolean autoRefresh = sharedPreferences.getBoolean(getString(R.string.key_notification_auto_refresh_switch), false);

        if(notificationSwitchOn && autoRefresh) {
            this.requestData();
            this.setAlarmIntent();
        } else {
            this.stopSelf(startId);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.stop();
    }

    /** <br> notification. */

    private void requestData() {
        client = new LocationClient(this);

        location = LocationTable.readLocationList(
                this,
                MyDatabaseHelper.getDatabaseHelper(this)).get(0);
        if(location.location.equals(getString(R.string.local))) {
            LocationUtils.requestLocation(client, this);
        } else {
            WeatherUtils.requestWeather(this, location, location.location, true, this);
        }
    }

    /** <br> alarm mission. */

    private void setAlarmIntent() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent target = new Intent(getBaseContext(), NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                getBaseContext(),
                REQUEST_CODE,
                target,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int duration = 1000 * 60 * 60 * WidgetAndNotificationUtils.getNotificationRefreshHours(this);
        alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + duration,
                pendingIntent);
    }

    /** <br> listener. */

    // request location.

    @Override
    public void requestLocationSuccess(String locationName) {
        WeatherUtils.requestWeather(this, location, locationName, true, this);
    }

    @Override
    public void requestLocationFailed() {
        LocationUtils.simpleLocationFailedFeedback(this);
        this.stopSelf(startId);
    }

    // request weather.

    @Override
    public void requestWeatherSuccess(Location result, boolean isDaily) {
        WidgetAndNotificationUtils.sendNotification(this, result.weather, true);
        this.stopSelf(startId);
    }

    @Override
    public void requestWeatherFailed(Location result, boolean isDaily) {
        WidgetAndNotificationUtils.sendNotificationFailed(this);
        this.stopSelf(startId);
    }
}
