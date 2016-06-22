package wangdaye.com.geometricweather.ui.activity.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wangdaye.com.geometricweather.data.database.LocationTable;
import wangdaye.com.geometricweather.data.database.WeatherTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.service.widget.WidgetDayService;
import wangdaye.com.geometricweather.utils.DisplayUtils;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Create widget day activity.
 * */

public class CreateWidgetDayActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        WeatherUtils.OnRequestWeatherListener, LocationUtils.OnRequestLocationListener {
    // widget
    private ImageView widgetCard;
    private ImageView widgetIcon;
    private TextView widgetWeather;
    private TextView widgetTemp;
    private TextView widgetRefreshTime;

    // data
    private Location location;
    private List<String> nameList;

    private boolean showCard;
    private boolean hideRefreshTime;
    private boolean blackText;

    /** <br> life cycle. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.setStatusBarTranslate(getWindow());
        DisplayUtils.setNavigationBarColor(this, getWindow(), true);
        setContentView(R.layout.activity_create_widget_day);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initData();
        this.initWidget();

        if (location.location.equals(getString(R.string.local))) {
            LocationUtils.requestLocation(this, this);
        } else {
            WeatherUtils.requestWeather(this, location, location.location, true, this);
        }
    }

    /** <br> UI. */

    private void initWidget() {
        this.widgetCard = (ImageView) findViewById(R.id.widget_day_card);
        widgetCard.setVisibility(View.GONE);

        this.widgetIcon = (ImageView) findViewById(R.id.widget_day_icon);
        this.widgetWeather = (TextView) findViewById(R.id.widget_day_weather);
        this.widgetTemp = (TextView) findViewById(R.id.widget_day_temp);
        this.widgetRefreshTime = (TextView) findViewById(R.id.widget_day_refreshTime);

        ImageView wallpaper = (ImageView) findViewById(R.id.activity_create_widget_day_wall);
        wallpaper.setImageDrawable(WallpaperManager.getInstance(this).getDrawable());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text, nameList);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        Spinner locationSpinner = (Spinner) findViewById(R.id.activity_create_widget_day_spinner);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);

        Switch showCardSwitch = (Switch) findViewById(R.id.activity_create_widget_day_showCardSwitch);
        showCardSwitch.setOnCheckedChangeListener(new ShowCardSwitchCheckListener());

        Switch hideRefreshTimeSwitch = (Switch) findViewById(R.id.activity_create_widget_day_hideRefreshTimeSwitch);
        hideRefreshTimeSwitch.setOnCheckedChangeListener(new HideRefreshTimeSwitchCheckListener());

        Switch blackTextSwitch = (Switch) findViewById(R.id.activity_create_widget_day_blackTextSwitch);
        blackTextSwitch.setOnCheckedChangeListener(new BlackTextSwitchCheckListener());

        Button doneButton = (Button) findViewById(R.id.activity_create_widget_day_doneButton);
        doneButton.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void refreshWidgetView() {
        if (location.weather == null) {
            return;
        }

        Weather weather = location.weather;

        int[] imageId = WeatherUtils.getWeatherIcon(
                weather.weatherKindNow,
                TimeUtils.getInstance(this).getDayTime(this, false).isDay);
        widgetIcon.setImageResource(imageId[3]);

        String[] texts = WidgetAndNotificationUtils.buildWidgetDayStyleText(weather);
        widgetWeather.setText(texts[0]);
        widgetTemp.setText(texts[1]);
        widgetRefreshTime.setText(weather.location + "." + weather.refreshTime);
    }

    /** <br> data. */

    private void initData() {
        this.nameList = new ArrayList<>();
        List<Location> locationList = LocationTable.readLocationList(this, MyDatabaseHelper.getDatabaseHelper(this));
        for (Location l : locationList) {
            nameList.add(l.location);
        }
        this.location = new Location(nameList.get(0));

        this.showCard = false;
        this.blackText = false;
    }

    /** <br> interface. */

    // on click listener.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_create_widget_day_doneButton:
                SharedPreferences.Editor editor = getSharedPreferences(
                        getString(R.string.sp_widget_day_setting),
                        MODE_PRIVATE)
                        .edit();
                editor.putString(getString(R.string.key_location), location.location);
                editor.putBoolean(getString(R.string.key_show_card), showCard);
                editor.putBoolean(getString(R.string.key_hide_refresh_time), hideRefreshTime);
                editor.putBoolean(getString(R.string.key_black_text), blackText);
                editor.apply();

                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                int appWidgetId = 0;
                if (extras != null) {
                    appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                }

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);

                Intent service = new Intent(this, WidgetDayService.class);
                startService(service);
                finish();
                break;
        }
    }

    // on select changed listener(spinner).

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        location = new Location(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        location = new Location(parent.getItemAtPosition(0).toString());
    }

    // on check changed listener(switch).

    private class ShowCardSwitchCheckListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                showCard = true;
                widgetCard.setVisibility(View.VISIBLE);
                widgetWeather.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextDark));
                widgetTemp.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextDark));
            } else {
                showCard = false;
                widgetCard.setVisibility(View.GONE);
                if (!blackText) {
                    widgetWeather.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextLight));
                    widgetTemp.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextLight));
                }
            }
        }
    }

    private class HideRefreshTimeSwitchCheckListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            hideRefreshTime = isChecked;
            widgetRefreshTime.setVisibility(hideRefreshTime ? View.GONE : View.VISIBLE);
        }
    }

    private class BlackTextSwitchCheckListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                blackText = true;
                widgetWeather.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextDark));
                widgetTemp.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextDark));
            } else {
                blackText = false;
                if (!showCard) {
                    widgetWeather.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextLight));
                    widgetTemp.setTextColor(ContextCompat.getColor(CreateWidgetDayActivity.this, R.color.colorTextLight));
                }
            }
        }
    }

    // on request weather listener.

    @Override
    public void requestWeatherSuccess(Location result, boolean isDaily) {
        location = result;
        this.refreshWidgetView();
    }

    @Override
    public void requestWeatherFailed(Location result, boolean isDaily) {
        location.weather = WeatherTable.readWeather(
                MyDatabaseHelper.getDatabaseHelper(this),
                location.location);
        if (location.weather != null) {
            this.refreshWidgetView();
        }
        Toast.makeText(this,
                getString(R.string.get_weather_data_failed),
                Toast.LENGTH_SHORT).show();
    }

    // on request location listener.

    @Override
    public void requestLocationSuccess(String locationName) {
        WeatherUtils.requestWeather(this, location, locationName, true, this);
    }

    @Override
    public void requestLocationFailed() {
        LocationUtils.simpleLocationFailedFeedback(this);
    }
}