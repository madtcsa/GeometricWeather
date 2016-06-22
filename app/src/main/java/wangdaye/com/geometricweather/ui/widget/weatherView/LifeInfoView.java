package wangdaye.com.geometricweather.ui.widget.weatherView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.model.Weather;

/**
 * Life info view.
 * */

public class LifeInfoView extends FrameLayout {
    // widget
    private TextView windTitle;
    private TextView windContent;
    private TextView pmTitle;
    private TextView pmContent;
    private TextView humidityTitle;
    private TextView humidityContent;
    private TextView uvTitle;
    private TextView uvContent;
    private TextView dressTitle;
    private TextView dressContent;
    private TextView coldTitle;
    private TextView coldContent;
    private TextView aqiTitle;
    private TextView aqiContent;
    private TextView washCarTitle;
    private TextView washCarContent;
    private TextView exerciseTitle;
    private TextView exerciseContent;

    /** <br> life cycle. */

    public LifeInfoView(Context context) {
        super(context);
        this.initialize();
    }

    public LifeInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public LifeInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LifeInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.container_life_info, null);
        addView(view);

        windTitle = (TextView) findViewById(R.id.container_details_wind_title);
        windContent = (TextView) findViewById(R.id.container_details_wind_content);
        pmTitle = (TextView) findViewById(R.id.container_details_pm_title);
        pmContent = (TextView) findViewById(R.id.container_details_pm_content);
        humidityTitle = (TextView) findViewById(R.id.container_details_humidity_title);
        humidityContent = (TextView) findViewById(R.id.container_details_humidity_content);
        uvTitle = (TextView) findViewById(R.id.container_details_uv_title);
        uvContent = (TextView) findViewById(R.id.container_details_uv_content);
        dressTitle = (TextView) findViewById(R.id.container_details_dress_title);
        dressContent = (TextView) findViewById(R.id.container_details_dress_content);
        coldTitle = (TextView) findViewById(R.id.container_details_cold_title);
        coldContent = (TextView) findViewById(R.id.container_details_cold_content);
        aqiTitle = (TextView) findViewById(R.id.container_details_aqi_title);
        aqiContent = (TextView) findViewById(R.id.container_details_aqi_content);
        washCarTitle = (TextView) findViewById(R.id.container_details_wash_car_title);
        washCarContent = (TextView) findViewById(R.id.container_details_wash_car_content);
        exerciseTitle = (TextView) findViewById(R.id.container_details_exercise_title);
        exerciseContent = (TextView) findViewById(R.id.container_details_exercise_content);
    }

    /** <br> data. */

    public void setData(Weather weather) {
        windTitle.setText(weather.windTitle);
        windContent.setText(weather.windContent);
        pmTitle.setText(weather.pmTitle);
        pmContent.setText(weather.pmContent);
        humidityTitle.setText(weather.humTitle);
        humidityContent.setText(weather.humContent);
        uvTitle.setText(weather.uvTitle);
        uvContent.setText(weather.uvContent);
        dressTitle.setText(weather.dressTitle);
        dressContent.setText(weather.dressContent);
        coldTitle.setText(weather.coldTitle);
        coldContent.setText(weather.coldContent);
        aqiTitle.setText(weather.aqiTitle);
        aqiContent.setText(weather.aqiContent);
        washCarTitle.setText(weather.washCarTitle);
        washCarContent.setText(weather.washCarContent);
        exerciseTitle.setText(weather.exerciseTitle);
        exerciseContent.setText(weather.exerciseContent);
    }
}
