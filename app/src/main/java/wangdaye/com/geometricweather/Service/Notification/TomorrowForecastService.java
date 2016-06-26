package wangdaye.com.geometricweather.service.notification;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.baidu.location.LocationClient;

import wangdaye.com.geometricweather.data.database.LocationTable;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Send tomorrow weather forecast.
 * */

public class TomorrowForecastService extends Service
        implements LocationUtils.OnRequestLocationListener, WeatherUtils.OnRequestWeatherListener {
    // widget
    private LocationClient client;

    // data
    private int startId;
    private Location location;

    /** <br> life cycle. */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public  int onStartCommand(Intent intent, int flags, int startId) {
        this.client = new LocationClient(this);
        this.startId = startId;
        this.location = LocationTable.readLocationList(
                this,
                MyDatabaseHelper.getDatabaseHelper(this)).get(0);

        if (location.location.equals(getString(R.string.local))) {
            LocationUtils.requestLocation(client, this);
        } else {
            WeatherUtils.requestWeather(this, location, location.location, true, this);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.stop();
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
        this.stopSelf(startId);
    }

    // request weather.

    @Override
    public void requestWeatherSuccess(Location result, boolean isDaily) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastType = sharedPreferences.getString(
                getString(R.string.key_forecast_type_today),
                getString(R.string.forecast_type_default));
        if (forecastType.equals(getString(R.string.forecast_type_default))) {
            WidgetAndNotificationUtils.sendForecast(this, result.weather, false);
        } else {
            WidgetAndNotificationUtils.sendNotification(this, result.weather, true);
        }
        this.stopSelf(startId);
    }

    @Override
    public void requestWeatherFailed(Location result, boolean isDaily) {
        WidgetAndNotificationUtils.sendNotificationFailed(this);
        this.stopSelf(startId);
    }
}
