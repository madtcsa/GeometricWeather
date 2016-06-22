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
import java.util.Calendar;
import java.util.List;

import wangdaye.com.geometricweather.data.database.LocationTable;
import wangdaye.com.geometricweather.data.database.WeatherTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.service.widget.WidgetWeekService;
import wangdaye.com.geometricweather.utils.DisplayUtils;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;

/**
 * Create the widget [week] on the launcher.
 * */

public class CreateWidgetWeekActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        WeatherUtils.OnRequestWeatherListener, LocationUtils.OnRequestLocationListener {
    // widget
    private ImageView widgetCard;
    private TextView[] widgetWeeks;
    private ImageView[] widgetIcons;
    private TextView[] widgetTemps;

    // data
    private Location location;
    private List<String> nameList;

    private boolean showCard;
    private boolean blackText;

    /** <br> life cycle. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.setStatusBarTranslate(getWindow());
        DisplayUtils.setNavigationBarColor(this, getWindow(), true);
        setContentView(R.layout.activity_create_widget_week);
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
        this.widgetCard = (ImageView) findViewById(R.id.widget_week_card);
        widgetCard.setVisibility(View.GONE);

        this.widgetWeeks = new TextView[] {
                (TextView) findViewById(R.id.widget_week_week_1),
                (TextView) findViewById(R.id.widget_week_week_2),
                (TextView) findViewById(R.id.widget_week_week_3),
                (TextView) findViewById(R.id.widget_week_week_4),
                (TextView) findViewById(R.id.widget_week_week_5)};
        this.widgetIcons = new ImageView[] {
                (ImageView) findViewById(R.id.widget_week_icon_1),
                (ImageView) findViewById(R.id.widget_week_icon_2),
                (ImageView) findViewById(R.id.widget_week_icon_3),
                (ImageView) findViewById(R.id.widget_week_icon_4),
                (ImageView) findViewById(R.id.widget_week_icon_5)};
        this.widgetTemps = new TextView[] {
                (TextView) findViewById(R.id.widget_week_temp_1),
                (TextView) findViewById(R.id.widget_week_temp_2),
                (TextView) findViewById(R.id.widget_week_temp_3),
                (TextView) findViewById(R.id.widget_week_temp_4),
                (TextView) findViewById(R.id.widget_week_temp_5)};

        ImageView wallpaper = (ImageView) findViewById(R.id.activity_create_widget_week_wall);
        wallpaper.setImageDrawable(WallpaperManager.getInstance(this).getDrawable());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text, nameList);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        Spinner locationSpinner = (Spinner) findViewById(R.id.activity_create_widget_week_spinner);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);

        Switch showCardSwitch = (Switch) findViewById(R.id.activity_create_widget_week_showCardSwitch);
        showCardSwitch.setOnCheckedChangeListener(new ShowCardSwitchCheckListener());

        Switch blackTextSwitch = (Switch) findViewById(R.id.activity_create_widget_week_blackTextSwitch);
        blackTextSwitch.setOnCheckedChangeListener(new BlackTextSwitchCheckListener());

        Button doneButton = (Button) findViewById(R.id.activity_create_widget_week_doneButton);
        doneButton.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void refreshWidgetView() {
        if (location.weather == null) {
            return;
        }

        Weather weather = location.weather;
        boolean isDay = TimeUtils.getInstance(this).getDayTime(this, false).isDay;

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
            firstWeekDay = getString(R.string.today);
            secondWeekDay = weather.weeks[1];
        } else if (Integer.parseInt(weatherDates[0]) == year
                && Integer.parseInt(weatherDates[1]) == month
                && Integer.parseInt(weatherDates[2]) == day - 1) {
            firstWeekDay = getString(R.string.yesterday);
            secondWeekDay = getString(R.string.today);
        } else {
            firstWeekDay = weather.weeks[0];
            secondWeekDay = weather.weeks[1];
        }

        for (int i = 0; i < 5; i ++) {
            if (i == 0) {
                widgetWeeks[i].setText(firstWeekDay);
            } else if (i == 1) {
                widgetWeeks[i].setText(secondWeekDay);
            } else {
                widgetWeeks[i].setText(weather.weeks[i]);
            }
            int[] imageIds = WeatherUtils.getWeatherIcon(
                    isDay ? weather.weatherKindDays[i] : weather.weatherKindNights[i],
                    isDay);
            widgetIcons[i].setImageResource(imageIds[3]);
            widgetTemps[i].setText(weather.miniTemps[i] + "/" + weather.maxiTemps[i] + "°");
        }
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
            case R.id.activity_create_widget_week_doneButton:
                SharedPreferences.Editor editor = getSharedPreferences(
                        getString(R.string.sp_widget_week_setting),
                        MODE_PRIVATE)
                        .edit();
                editor.putString(getString(R.string.key_location), location.location);
                editor.putBoolean(getString(R.string.key_show_card), showCard);
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

                Intent service = new Intent(this, WidgetWeekService.class);
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
                for (int i = 0; i < 5; i ++) {
                    widgetWeeks[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextDark));
                    widgetTemps[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextDark));
                }
            } else {
                showCard = false;
                widgetCard.setVisibility(View.GONE);
                if (!blackText) {
                    for (int i = 0; i < 5; i ++) {
                        widgetWeeks[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextLight));
                        widgetTemps[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextLight));
                    }
                }
            }
        }
    }

    private class BlackTextSwitchCheckListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                blackText = true;
                for (int i = 0; i < 5; i ++) {
                    widgetWeeks[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextDark));
                    widgetTemps[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextDark));
                }
            } else {
                blackText = false;
                if (!showCard) {
                    for (int i = 0; i < 5; i ++) {
                        widgetWeeks[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextLight));
                        widgetTemps[i].setTextColor(ContextCompat.getColor(CreateWidgetWeekActivity.this, R.color.colorTextLight));
                    }
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