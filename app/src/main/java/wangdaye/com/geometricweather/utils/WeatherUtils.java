package wangdaye.com.geometricweather.utils;

import android.content.Context;
import android.os.Message;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.api.HefengWeather;
import wangdaye.com.geometricweather.data.api.JuheWeather;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.data.database.WeatherTable;
import wangdaye.com.geometricweather.data.model.HefengResult;
import wangdaye.com.geometricweather.data.model.HourlyWeather;
import wangdaye.com.geometricweather.data.model.JuheResult;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.model.Weather;

/**
 * Weather kind tools.
 * */

public class WeatherUtils {

    public static void requestWeather(Context context,
                                      Location location, String requestLocation,
                                      boolean isDaily, OnRequestWeatherListener l) {
        new WeatherClient(l).startClient(context, location, requestLocation, isDaily);
    }

    public static int[] getWeatherIcon(String weatherKind, boolean isDay) {
        int[] imageId = new int[4];

        switch (weatherKind) {
            case "晴":
                if(isDay) {
                    imageId[0] = R.drawable.weather_sun_day;
                    imageId[1] = 0;
                    imageId[2] = 0;
                    imageId[3] = R.drawable.weather_sun_day;
                } else {
                    imageId[0] = R.drawable.weather_sun_night;
                    imageId[1] = 0;
                    imageId[2] = 0;
                    imageId[3] = R.drawable.weather_sun_night;
                }
                break;
            case "云":
                if(isDay) {
                    imageId[0] = R.drawable.weather_cloud_day_1;
                    imageId[1] = R.drawable.weather_cloud_day_2;
                    imageId[2] = R.drawable.weather_cloud_day_3;
                    imageId[3] = R.drawable.weather_cloud_day;
                } else {
                    imageId[0] = R.drawable.weather_cloud_night_1;
                    imageId[1] = R.drawable.weather_cloud_night_2;
                    imageId[2] = 0;
                    imageId[3] = R.drawable.weather_cloud_night;
                }
                break;
            case "阴":
                imageId[0] = R.drawable.weather_cloudy_1;
                imageId[1] = R.drawable.weather_cloudy_2;
                imageId[2] = 0;
                imageId[3] = R.drawable.weather_cloudy;
                break;
            case "雨":
                imageId[0] = R.drawable.weather_rain_1;
                imageId[1] = R.drawable.weather_rain_2;
                imageId[2] = R.drawable.weather_rain_3;
                imageId[3] = R.drawable.weather_rain;
                break;
            case "风":
                imageId[0] = R.drawable.weather_wind;
                imageId[1] = 0;
                imageId[2] = 0;
                imageId[3] = R.drawable.weather_wind;
                break;
            case "雪":
                imageId[0] = R.drawable.weather_snow_1;
                imageId[1] = R.drawable.weather_snow_2;
                imageId[2] = R.drawable.weather_snow_3;
                imageId[3] = R.drawable.weather_snow;
                break;
            case "雾":
                imageId[0] = R.drawable.weather_fog;
                imageId[1] = R.drawable.weather_fog;
                imageId[2] = R.drawable.weather_fog;
                imageId[3] = R.drawable.weather_fog;
                break;
            case "霾":
                imageId[0] = R.drawable.weather_haze_1;
                imageId[1] = R.drawable.weather_haze_2;
                imageId[2] = R.drawable.weather_haze_3;
                imageId[3] = R.drawable.weather_haze;
                break;
            case "雨夹雪":
                imageId[0] = R.drawable.weather_sleet_1;
                imageId[1] = R.drawable.weather_sleet_2;
                imageId[2] = R.drawable.weather_sleet_3;
                imageId[3] = R.drawable.weather_sleet;
                break;
            case "雷雨":
                imageId[0] = R.drawable.weather_thunderstorm_1;
                imageId[1] = R.drawable.weather_thunderstorm_2;
                imageId[2] = R.drawable.weather_thunderstorm_3;
                imageId[3] = R.drawable.weather_thunderstorm;
                break;
            case "雷":
                imageId[0] = R.drawable.weather_thunder_1;
                imageId[1] = R.drawable.weather_thunder_2;
                imageId[2] = R.drawable.weather_thunder_2;
                imageId[3] = R.drawable.weather_thunder;
                break;
            case "冰雹":
                imageId[0] = R.drawable.weather_hail_1;
                imageId[1] = R.drawable.weather_hail_2;
                imageId[2] = R.drawable.weather_hail_3;
                imageId[3] = R.drawable.weather_hail;
                break;
            default:
                imageId[0] = R.drawable.weather_cloudy_1;
                imageId[1] = R.drawable.weather_cloudy_2;
                imageId[2] = R.drawable.weather_cloudy_2;
                imageId[3] = R.drawable.weather_cloudy;
                break;
        }
        return imageId;
    }

    public static int[] getAnimatorId(String weatherKind, boolean isDay) {
        int[] animatorId = new int[3];

        switch (weatherKind) {
            case "晴":
                if(isDay) {
                    animatorId[0] = R.animator.weather_sun_day;
                    animatorId[1] = 0;
                    animatorId[2] = 0;
                } else {
                    animatorId[0] = R.animator.weather_sun_night;
                    animatorId[1] = 0;
                    animatorId[2] = 0;
                }
                break;
            case "云":
                if(isDay) {
                    animatorId[0] = R.animator.weather_cloud_day_1;
                    animatorId[1] = R.animator.weather_cloud_day_2;
                    animatorId[2] = R.animator.weather_cloud_day_3;
                } else {
                    animatorId[0] = R.animator.weather_cloud_night_1;
                    animatorId[1] = R.animator.weather_cloud_night_2;
                    animatorId[2] = 0;
                }
                break;
            case "阴":
                animatorId[0] = R.animator.weather_cloudy_1;
                animatorId[1] = R.animator.weather_cloudy_2;
                animatorId[2] = 0;
                break;
            case "雨":
                animatorId[0] = R.animator.weather_rain_1;
                animatorId[1] = R.animator.weather_rain_2;
                animatorId[2] = R.animator.weather_rain_3;
                break;
            case "风":
                animatorId[0] = R.animator.weather_wind;
                animatorId[1] = 0;
                animatorId[2] = 0;
                break;
            case "雪":
                animatorId[0] = R.animator.weather_snow_1;
                animatorId[1] = R.animator.weather_snow_2;
                animatorId[2] = R.animator.weather_snow_3;
                break;
            case "雾":
                animatorId[0] = R.animator.weather_fog_1;
                animatorId[1] = R.animator.weather_fog_2;
                animatorId[2] = R.animator.weather_fog_3;
                break;
            case "霾":
                animatorId[0] = R.animator.weather_haze_1;
                animatorId[1] = R.animator.weather_haze_2;
                animatorId[2] = R.animator.weather_haze_3;
                break;
            case "雨夹雪":
                animatorId[0] = R.animator.weather_sleet_1;
                animatorId[1] = R.animator.weather_sleet_2;
                animatorId[2] = R.animator.weather_sleet_3;
                break;
            case "雷雨":
                animatorId[0] = R.animator.weather_thunderstorm_1;
                animatorId[1] = R.animator.weather_thunderstorm_2;
                animatorId[2] = R.animator.weather_thunderstorm_3;
                break;
            case "雷":
                animatorId[0] = R.animator.weather_thunder_1;
                animatorId[1] = R.animator.weather_thunder_2;
                animatorId[2] = R.animator.weather_thunder_2;
                break;
            case "冰雹":
                animatorId[0] = R.animator.weather_hail_1;
                animatorId[1] = R.animator.weather_hail_2;
                animatorId[2] = R.animator.weather_hail_3;
                break;
            default:
                animatorId[0] = R.animator.weather_cloudy_1;
                animatorId[1] = R.animator.weather_cloudy_2;
                animatorId[2] = 0;
                break;
        }
        return animatorId;
    }

    public static int getMiniWeatherIcon(String weatherInfo, boolean isDay) {
        int imageId;
        switch (weatherInfo) {
            case "晴":
                if(isDay) {
                    imageId = R.drawable.weather_sun_day_mini;
                } else {
                    imageId = R.drawable.weather_sun_night_mini;
                }
                break;
            case "云":
                if(isDay) {
                    imageId = R.drawable.weather_cloud_day_mini;
                } else {
                    imageId = R.drawable.weather_cloud_mini;
                }
                break;
            case "雨":
                imageId = R.drawable.weather_rain_mini;
                break;
            case "风":
                imageId = R.drawable.weather_wind_mini;
                break;
            case "雪":
                imageId = R.drawable.weather_snow_mini;
                break;
            case "雾":
                imageId = R.drawable.weather_fog_mini;
                break;
            case "霾":
                imageId = R.drawable.weather_haze_mini;
                break;
            case "雨夹雪":
                imageId = R.drawable.weather_sleet_mini;
                break;
            case "雷雨":
                imageId = R.drawable.weather_thunder_mini;
                break;
            case "雷":
                imageId = R.drawable.weather_thunder_mini;
                break;
            case "冰雹":
                imageId = R.drawable.weather_hail_mini;
                break;
            case "阴":
            default:
                imageId = R.drawable.weather_cloud_mini;
                break;
        }
        return imageId;
    }

    /** <br> inner class. */

    public static class WeatherClient implements SafeHandler.HandlerContainer {
        // widget
        private SafeHandler<WeatherClient> handler;
        private OnRequestWeatherListener listener;

        // data
        private Location location;
        private boolean isDaily;

        private final int REQUEST_DATA_SUCCESS = 1;
        private final int REQUEST_DATA_FAILED = -1;

        // life cycle.

        public WeatherClient(OnRequestWeatherListener l) {
            this.handler = new SafeHandler<>(this);
            this.listener = l;
        }

        // data.

        public void startClient(final Context context,
                                final Location location, final String requestLocation, final boolean isDaily) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean requestSuccess;
                    Weather weather;
                    HourlyWeather hourlyWeather;

                    if (isDaily) {
                        if (Location.isEngLocation(requestLocation)) {
                            HefengResult result = HefengWeather.getWeather(requestLocation, true);
                            weather = Weather.buildWeather(context,
                                    result,
                                    location.location);
                            location.hourlyWeather = HourlyWeather.buildHourlyWeather(result);
                        } else {
                            JuheResult result = JuheWeather.getWeather(requestLocation);
                            weather = Weather.buildWeather(context,
                                    result,
                                    location.location,
                                    TimeUtils.getInstance(context).getDayTime(context, false).isDay);
                        }
                        if (weather != null) {
                            location.weather = weather;
                            WeatherTable.writeWeather(
                                    MyDatabaseHelper.getDatabaseHelper(context),
                                    weather);
                        }
                        requestSuccess = weather != null;
                    } else {
                        HefengResult result = HefengWeather.getWeather(requestLocation, false);
                        weather = Weather.buildWeather(context, result, location.location);
                        hourlyWeather = HourlyWeather.buildHourlyWeather(result);
                        if (weather != null && Location.isEngLocation(requestLocation)) {
                            location.weather = weather;
                        }
                        if (hourlyWeather != null) {
                            location.hourlyWeather = hourlyWeather;
                        }
                        requestSuccess = hourlyWeather != null;
                    }

                    WeatherClient.this.location = location;
                    WeatherClient.this.isDaily = isDaily;

                    Message msg = new Message();
                    if (requestSuccess) {
                        msg.what = REQUEST_DATA_SUCCESS;
                    } else {
                        msg.what = REQUEST_DATA_FAILED;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        }

        // handler.

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case REQUEST_DATA_SUCCESS:
                    listener.requestWeatherSuccess(location, isDaily);
                    break;

                case REQUEST_DATA_FAILED:
                    listener.requestWeatherFailed(location, isDaily);
                    break;
            }
        }
    }

    /** <br> listener. */

    public interface OnRequestWeatherListener {
        void requestWeatherSuccess(Location result, boolean isDaily);
        void requestWeatherFailed(Location result,  boolean isDaily);
    }
}
