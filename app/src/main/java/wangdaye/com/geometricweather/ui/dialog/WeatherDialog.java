package wangdaye.com.geometricweather.ui.dialog;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;

/**
 * Weather dialog.
 * */

public class WeatherDialog extends DialogFragment implements View.OnClickListener {
    // animator
    private AnimatorSet[][] iconAnimatorSets;

    // data
    private Weather weather;
    private int position;

    /** <br> life cycle. */

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_weather, null, false);
        this.initWidget(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    /** <br> UI. */

    private void initWidget(View view) {
        boolean isDay = TimeUtils.getInstance(getActivity()).getDayTime(getActivity(), false).isDay;

        RelativeLayout[] containers = new RelativeLayout[]{
                (RelativeLayout) view.findViewById(R.id.fragment_weather_details_container_day),
                (RelativeLayout) view.findViewById(R.id.fragment_weather_details_container_night)};
        containers[0].setOnClickListener(this);
        containers[1].setOnClickListener(this);

        ImageView[][] weatherIcons = new ImageView[2][3];
        weatherIcons[0][0] = (ImageView) view.findViewById(R.id.fragment_weather_details_icon_day_1);
        weatherIcons[0][1] = (ImageView) view.findViewById(R.id.fragment_weather_details_icon_day_2);
        weatherIcons[0][2] = (ImageView) view.findViewById(R.id.fragment_weather_details_icon_day_3);
        weatherIcons[1][0] = (ImageView) view.findViewById(R.id.fragment_weather_details_icon_night_1);
        weatherIcons[1][1] = (ImageView) view.findViewById(R.id.fragment_weather_details_icon_night_2);
        weatherIcons[1][2] = (ImageView) view.findViewById(R.id.fragment_weather_details_icon_night_3);
        int[][] imageIds = new int[2][3];
        imageIds[0] = WeatherUtils.getWeatherIcon(weather.weatherKindDays[position], true);
        imageIds[1] = WeatherUtils.getWeatherIcon(weather.weatherKindNights[position], false);
        for (int i = 0; i < weatherIcons.length; i ++) {
            for (int j = 0; j < weatherIcons[i].length; j ++) {
                if (imageIds[i][j] != 0) {
                    weatherIcons[i][j].setImageResource(imageIds[i][j]);
                    weatherIcons[i][j].setVisibility(View.VISIBLE);
                } else {
                    weatherIcons[i][j].setVisibility(View.GONE);
                }
            }
        }

        TextView[] weatherText = new TextView[]{
                (TextView) view.findViewById(R.id.fragment_weather_details_text_day),
                (TextView) view.findViewById(R.id.fragment_weather_details_text_night)};
        String text = weather.weatherDays[position] + " " + weather.maxiTemps[position] + "℃"
                + "\n" + weather.windDirDays[position] + weather.windLevelDays[position];
        weatherText[0].setText(text);
        text = weather.weatherNights[position] + " " + weather.miniTemps[position] + "℃"
                + "\n" + weather.windDirNights[position] + weather.windLevelNights[position];
        weatherText[1].setText(text);

        Button done = (Button) view.findViewById(R.id.fragment_weather_details_button);
        if (isDay) {
            done.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightPrimary_3));
        } else {
            done.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkPrimary_1));
        }
        done.setOnClickListener(this);

        int[][] animatorIds = new int[2][3];
        animatorIds[0] = WeatherUtils.getAnimatorId(weather.weatherKindDays[position], true);
        animatorIds[1] = WeatherUtils.getAnimatorId(weather.weatherKindNights[position], false);
        this.iconAnimatorSets = new AnimatorSet[2][3];
        for (int i = 0; i < iconAnimatorSets.length; i ++) {
            for (int j = 0; j < iconAnimatorSets[i].length; j ++) {
                if (animatorIds[i][j] != 0) {
                    iconAnimatorSets[i][j] = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), animatorIds[i][j]);
                    iconAnimatorSets[i][j].setTarget(weatherIcons[i][j]);
                } else {
                    iconAnimatorSets[i][j] = null;
                }
            }
        }
    }

    /** <br> data. */

    public void setData(Weather weather, int position) {
        this.weather = weather;
        this.position = position;
    }

    /** <br> interface. */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_weather_details_button:
                dismiss();
                break;

            case R.id.fragment_weather_details_container_day:
                for (AnimatorSet a : iconAnimatorSets[0]) {
                    if (a != null) {
                        a.start();
                    }
                }
                break;

            case R.id.fragment_weather_details_container_night:
                for (AnimatorSet a : iconAnimatorSets[1]) {
                    if (a != null) {
                        a.start();
                    }
                }
                break;
        }
    }
}
