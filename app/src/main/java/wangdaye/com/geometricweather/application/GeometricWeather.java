package wangdaye.com.geometricweather.application;

import android.app.Application;

/**
 * Geometric weather.
 * */

public class GeometricWeather extends Application {

    /** <br> life cycle. */

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /** <br> singleton. */

    private static GeometricWeather instance;

    public static GeometricWeather getInstance() {
        return instance;
    }
}
