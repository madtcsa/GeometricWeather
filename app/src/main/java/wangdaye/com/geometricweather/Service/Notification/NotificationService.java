package wangdaye.com.geometricweather.service.notification;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import wangdaye.com.geometricweather.data.database.LocationTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.service.RecursionService;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Notification service.
 * */

public class NotificationService extends RecursionService
        implements LocationUtils.OnRequestLocationListener, WeatherUtils.OnRequestWeatherListener {
    // data
    private boolean serviceSwitch;
    private boolean autoRefresh;
    private static final int REQUEST_CODE = 7;

    /** <br> life cycle. */

    @Override
    public void readSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        serviceSwitch = sharedPreferences.getBoolean(getString(R.string.key_notification_switch), false);
        autoRefresh = sharedPreferences.getBoolean(getString(R.string.key_notification_auto_refresh_switch), false);

        setLocation(
                LocationTable.readLocationList(
                        this,
                        MyDatabaseHelper.getDatabaseHelper(this)).get(0));
    }

    @Override
    public void doRefresh() {
        if (serviceSwitch && autoRefresh) {
            requestData(this, this);
            setAlarmIntent(getClass(), REQUEST_CODE, false);
        } else {
            stopSelf(getStartId());
        }
    }

    /** <br> listener. */

    // request location.

    @Override
    public void requestLocationSuccess(String locationName) {
        WeatherUtils.requestWeather(this, getLocation(), locationName, true, this);
    }

    @Override
    public void requestLocationFailed() {
        LocationUtils.simpleLocationFailedFeedback(this);
        this.stopSelf(getStartId());
    }

    // request weather.

    @Override
    public void requestWeatherSuccess(Location result, boolean isDaily) {
        WidgetAndNotificationUtils.sendNotification(this, result.weather, true);
        this.stopSelf(getStartId());
    }

    @Override
    public void requestWeatherFailed(Location result, boolean isDaily) {
        WidgetAndNotificationUtils.sendNotificationFailed(this);
        this.stopSelf(getStartId());
    }
}
