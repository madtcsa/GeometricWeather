package wangdaye.com.geometricweather.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.ui.activity.MainActivity;

/**
 * Share utils.
 * */

public class ShareUtils {

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public static void shareWeather(MainActivity activity, Weather weather) {
        if (weather == null) {
            Toast.makeText(activity,
                    activity.getString(R.string.share_error),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(activity).inflate(R.layout.container_share_view, null);
        boolean isDay = TimeUtils.getInstance(activity).getDayTime(activity, false).isDay;

        ImageView background = (ImageView) view.findViewById(R.id.container_share_view_background);
        ImageView iconNow = (ImageView) view.findViewById(R.id.container_share_view_flagIcon);
        TextView location = (TextView) view.findViewById(R.id.container_share_view_location);
        TextView weatherNow = (TextView) view.findViewById(R.id.container_share_view_weather);
        TextView tempNow = (TextView) view.findViewById(R.id.container_share_view_temp);
        TextView wind = (TextView) view.findViewById(R.id.container_share_view_wind);
        TextView air = (TextView) view.findViewById(R.id.container_share_view_aqi);
        TextView[] weeks = new TextView[] {
                (TextView) view.findViewById(R.id.container_share_view_week_1),
                (TextView) view.findViewById(R.id.container_share_view_week_2),
                (TextView) view.findViewById(R.id.container_share_view_week_3)
        };
        ImageView[] icons = new ImageView[] {
                (ImageView) view.findViewById(R.id.container_share_view_icon_1),
                (ImageView) view.findViewById(R.id.container_share_view_icon_2),
                (ImageView) view.findViewById(R.id.container_share_view_icon_3)
        };
        TextView[] temps = new TextView[] {
                (TextView) view.findViewById(R.id.container_share_view_temp_1),
                (TextView) view.findViewById(R.id.container_share_view_temp_2),
                (TextView) view.findViewById(R.id.container_share_view_temp_3)
        };

        Glide.with(activity)
                .load(isDay ? R.drawable.nav_head_day : R.drawable.nav_head_night)
                .into(background);

        int[][] imageId = new int[][] {
                WeatherUtils.getWeatherIcon(weather.weatherKindNow, isDay),
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[0] : weather.weatherKindNights[0],
                        isDay),
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[1] : weather.weatherKindNights[1],
                        isDay),
                WeatherUtils.getWeatherIcon(
                        isDay ? weather.weatherKindDays[2] : weather.weatherKindNights[2],
                        isDay),
        };
        for (int i = 0; i < imageId.length; i ++) {
            if (imageId[i][3] != 0) {
                if (i == 0) {
                    Glide.with(activity).load(imageId[i][3]).into(iconNow);
                } else {
                    Glide.with(activity).load(imageId[i][3]).into(icons[i - 1]);
                }
            }
        }

        location.setText(weather.location);
        weatherNow.setText(weather.weatherNow + " " + weather.tempNow + "℃");
        tempNow.setText(activity.getString(R.string.temp) +  weather.miniTemps[0] + "/" + weather.maxiTemps[0] + "°");
        wind.setText(
                (isDay ? weather.windDirDays[0] : weather.windDirNights[0]) +
                (isDay ? weather.windLevelDays[0] : weather.windLevelNights[0]));
        air.setText(weather.aqiLevel);
        for (int i = 0; i < weeks.length; i ++) {
            weeks[i].setText(weather.weeks[i]);
            temps[i].setText(weather.miniTemps[i] + "/" + weather.maxiTemps[i] + "°");
        }

        view.setDrawingCacheEnabled(true);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(
                        activity.getResources().getDisplayMetrics().widthPixels,
                        View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(
                        (int) DisplayUtils.dpToPx(activity, 260),
                        View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String oldUri = sharedPreferences.getString(activity.getString(R.string.key_share_uri), "null");
        if (! oldUri.equals("null")) {
            deleteSharePicture(activity, Uri.parse(oldUri));
        }

        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(activity.getString(R.string.key_share_uri), uri.toString());
        editor.apply();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        activity.startActivityForResult(
                Intent.createChooser(
                        shareIntent,
                        activity.getResources().getText(R.string.action_share)),
                activity.SHARE_ACTIVITY);
    }

    public static void deleteSharePicture(Context context, Uri uri) {
        if (uri != null) {
            context.getContentResolver().delete(uri, null, null);
        }
    }
}
