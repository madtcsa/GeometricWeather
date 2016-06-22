package wangdaye.com.geometricweather.ui.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.application.GeometricWeather;
import wangdaye.com.geometricweather.data.database.HistoryTable;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.data.database.WeatherTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.ui.activity.MainActivity;
import wangdaye.com.geometricweather.ui.dialog.ManageDialog;
import wangdaye.com.geometricweather.ui.dialog.WeatherDialog;
import wangdaye.com.geometricweather.ui.widget.swipeRefreshLayout.SwipeRefreshLayout;
import wangdaye.com.geometricweather.ui.widget.weatherView.LifeInfoView;
import wangdaye.com.geometricweather.ui.widget.weatherView.SkyView;
import wangdaye.com.geometricweather.ui.widget.SwipeSwitchLayout;
import wangdaye.com.geometricweather.ui.widget.weatherView.TrendView;
import wangdaye.com.geometricweather.ui.widget.weatherView.WeekWeatherView;
import wangdaye.com.geometricweather.utils.DisplayUtils;
import wangdaye.com.geometricweather.utils.LocationUtils;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;

/**
 * Weather fragment.
 * */

public class WeatherFragment extends Fragment
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, NestedScrollView.OnScrollChangeListener,
        SwipeSwitchLayout.OnSwipeListener, WeekWeatherView.OnClickWeekContainerListener, TrendView.OnStateChangedListener,
        WeatherUtils.OnRequestWeatherListener, LocationUtils.OnRequestLocationListener {
    // widget
    private Toolbar toolbar;
    private SkyView skyView;

    private SwipeSwitchLayout swipeSwitchLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private LinearLayout weatherContainer;

    private TextView[] titleTexts;

    private TextView refreshTime;
    private TextView locationText;
    private ImageView collectionIcon;

    private TextView overviewTitle;
    private WeekWeatherView weekWeatherView;
    private TrendView trendView;
    private TextView lifeInfoTitle;
    private LifeInfoView lifeInfoView;

    // data
    public Location location;
    public boolean isCollected;
    private boolean requestingHourlyWeather;

    private int scrollDistance;
    private int totalDistance;

    // animation
    private AnimatorSet viewShowAnimator;

    /** <br> life cycle. */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        this.initData();
        this.initWidget(view);
        this.reset();

        return view;
    }

    /** <br> UI. */

    // initialize.

    private void initWidget(View view) {
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.container_main_toolbar);
        this.skyView = (SkyView) view.findViewById(R.id.fragment_weather_skyView);
        this.initScrollViewPart(view);
    }

    private void initScrollViewPart(View view) {
        // get swipe switch layout.
        this.swipeSwitchLayout = (SwipeSwitchLayout) view.findViewById(R.id.fragment_weather_swipeSwitchLayout);
        swipeSwitchLayout.setOnSwipeListener(this);

        // get swipe refresh layout & set color.
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_weather_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.lightPrimary_3),
                ContextCompat.getColor(getActivity(), R.color.darkPrimary_1));
        swipeRefreshLayout.setOnRefreshListener(this);

        // get nested scroll view & set listener.
        this.nestedScrollView = (NestedScrollView) view.findViewById(R.id.fragment_weather_scrollView);
        nestedScrollView.setOnScrollChangeListener(this);

        // get weather container.
        this.weatherContainer = (LinearLayout) view.findViewById(R.id.container_weather);
        viewShowAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.card_in);
        viewShowAnimator.setTarget(weatherContainer);

        // get touch layout, set height & get live texts.
        RelativeLayout touchLayout = (RelativeLayout) view.findViewById(R.id.container_weather_touchLayout);
        LinearLayout.LayoutParams touchParams = (LinearLayout.LayoutParams) touchLayout.getLayoutParams();
        touchParams.height = (int) (getResources().getDisplayMetrics().widthPixels / 6.8 * 4
                + DisplayUtils.dpToPx(getActivity(), 60)
                - DisplayUtils.dpToPx(getActivity(), 300 - 256));
        touchLayout.setLayoutParams(touchParams);
        touchLayout.setOnClickListener(this);

        this.titleTexts = new TextView[] {
                (TextView) view.findViewById(R.id.container_weather_aqi_text_live),
                (TextView) view.findViewById(R.id.container_weather_weather_text_live)};

        // weather card.
        this.initWeatherCard(view);

        //get life info view.
        this.lifeInfoView = (LifeInfoView) view.findViewById(R.id.container_weather_lifeInfoView);
    }

    private void initWeatherCard(View view) {
        this.refreshTime = (TextView) view.findViewById(R.id.container_weather_time_text_live);

        this.locationText = (TextView) view.findViewById(R.id.container_weather_location_text_live);
        locationText.setOnClickListener(this);
        this.collectionIcon = (ImageView) view.findViewById(R.id.container_weather_location_collect_icon);
        collectionIcon.setOnClickListener(this);
        collectionIcon.setImageResource(
                isCollected ?
                        R.drawable.ic_collect_yes : R.drawable.ic_collect_no);

        this.overviewTitle = (TextView) view.findViewById(R.id.container_weather_overviewTitle);

        this.weekWeatherView = (WeekWeatherView) view.findViewById(R.id.container_weather_weekWeatherView);
        weekWeatherView.setOnClickWeekContainerListener(this);

        this.trendView = (TrendView) view.findViewById(R.id.container_weather_trendView);
        trendView.setOnStateChangedListener(this);

        this.lifeInfoTitle = (TextView) view.findViewById(R.id.container_weather_lifeInfoTitle);
    }

    // reset.

    public void reset() {
        this.requestingHourlyWeather = false;
        toolbar.setAlpha(1);
        skyView.reset();
        this.resetScrollViewPart();
    }

    private void resetScrollViewPart() {
        // set weather container gone.
        weatherContainer.setVisibility(View.GONE);
        // set swipe switch layout reset.
        swipeSwitchLayout.reset();
        swipeSwitchLayout.setEnabled(true);
        // set nested scroll view scroll to top.
        nestedScrollView.scrollTo(0, 0);
        scrollDistance = 0;
        // set swipe refresh layout refreshing.
        if (location.weather == null) {
            setRefreshing(true);
            onRefresh();
        } else {
            buildUI();
        }
    }

    // build UI.

    private void setRefreshing(final boolean b) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

    public void showSkyCircles() {
        skyView.showCircles();
        skyView.iconRise();
    }

    @SuppressLint("SetTextI18n")
    private void buildUI() {
        Weather weather = location.weather;
        if (weather == null) {
            return;
        }

        DisplayUtils.setWindowTopColor(getActivity());
        ((MainActivity) getActivity()).initStatusBarColor();
        ((MainActivity) getActivity()).initNavHeaderBackground();

        skyView.setWeather(weather);

        titleTexts[0].setText(weather.aqiLevel);
        titleTexts[1].setText(weather.weatherNow + " " + weather.tempNow + "℃");
        refreshTime.setText(weather.refreshTime);
        locationText.setText(weather.location);
        collectionIcon.setImageResource(isCollected ?
                R.drawable.ic_collect_yes : R.drawable.ic_collect_no);

        if (TimeUtils.getInstance(getActivity()).isDay) {
            overviewTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightPrimary_3));
            lifeInfoTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightPrimary_3));
        } else {
            overviewTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkPrimary_1));
            lifeInfoTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkPrimary_1));
        }

        weekWeatherView.setData(weather);
        trendView.setDailyTemps(location.weather, location.history);
        trendView.setHourlyTemps(location.hourlyWeather);
        trendView.setColorResource(
                TimeUtils.getInstance(getActivity()).isDay ?
                R.color.lightPrimary_3 : R.color.darkPrimary_1);
        trendView.setState(TrendView.DAILY_STATE);
        lifeInfoView.setData(location.weather);

        weatherContainer.setVisibility(View.VISIBLE);
        viewShowAnimator.start();
    }

    /** <br> data. */

    private void initData() {
        this.totalDistance = (int) DisplayUtils.dpToPx(getActivity(), 256);
    }

    public void setLocation(Location l, boolean isCollected) {
        this.location = l;
        this.isCollected = isCollected;
    }

    /** <br> interface. */

    // on click listener.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_weather_touchLayout:
                skyView.onClickSky();
                break;

            case R.id.container_weather_location_text_live:
                ManageDialog manageDialog = new ManageDialog();
                manageDialog.setOnLocationChangedListener(((MainActivity) getActivity()));
                manageDialog.show(getFragmentManager(), null);
                break;

            case R.id.container_weather_location_collect_icon:
                if (isCollected) {
                    isCollected = false;
                    ((MainActivity) getActivity()).deleteLocation(location);
                    collectionIcon.setImageResource(R.drawable.ic_collect_no);
                } else {
                    isCollected = true;
                    ((MainActivity) getActivity()).addLocation(location);
                    collectionIcon.setImageResource(R.drawable.ic_collect_yes);
                }
                break;
        }
    }

    // on refresh listener.

    @Override
    public void onRefresh() {
        TimeUtils.getInstance(getActivity()).getDayTime(getActivity(), true);
        if (location.location.equals(getString(R.string.local))) {
            LocationUtils.requestLocation(GeometricWeather.getInstance(), this);
        } else {
            WeatherUtils.requestWeather(getActivity(), location, location.location, true, this);
        }
    }

    // on scroll changed listener.

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        scrollDistance += scrollY - oldScrollY;
        float alpha = (float) (1.0 - 1.0 * scrollDistance / totalDistance);
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 1) {
            alpha = 1;
        }
        toolbar.setAlpha(alpha);
    }

    // on swipe listener(swipe switch layout).

    @Override
    public void swipeTakeEffect(int direction) {
        ((MainActivity) getActivity()).switchCity(location.location, direction);
    }

    // on click week listener.

    @Override
    public void onClickWeekContainer(int position) {
        WeatherDialog weatherDialog = new WeatherDialog();
        weatherDialog.setData(location.weather, position);
        weatherDialog.show(getFragmentManager(), null);
    }

    // on state changed listener(trend view).

    @Override
    public void onClickTrendView(int state) {
        switch (state) {
            case TrendView.DAILY_STATE:
                if (location.hourlyWeather != null) {
                    trendView.setState(TrendView.HOURLY_STATE);
                } else {
                    trendView.setState(TrendView.LOADING_STATE);
                }
                break;

            case TrendView.LOADING_STATE:
                trendView.setState(TrendView.DAILY_STATE);
                break;

            case TrendView.HOURLY_STATE:
                trendView.setState(TrendView.DAILY_STATE);
                break;
        }
    }

    @Override
    public void onStateChange(int state) {
        switch (state) {
            case TrendView.LOADING_STATE:
                if (!requestingHourlyWeather) {
                    requestingHourlyWeather = true;
                    WeatherUtils.requestWeather(getActivity(),
                            location, location.weather.location,
                            false, this);
                }
                break;
        }
    }

    // on request weather listener.

    @Override
    public void requestWeatherSuccess(Location result, boolean isDaily) {
        if (result.location.equals(location.location)) {
            location = result;
            if (isDaily) {
                location.history = HistoryTable.readYesterdayWeather(
                        MyDatabaseHelper.getDatabaseHelper(getActivity()),
                        location.weather);

                ((MainActivity) getActivity()).refreshWidgetView();
                ((MainActivity) getActivity()).sendNotification();
                WeatherTable.writeWeather(
                        MyDatabaseHelper.getDatabaseHelper(getActivity()),
                        location.weather);
                HistoryTable.writeTodayWeather(
                        MyDatabaseHelper.getDatabaseHelper(getActivity()),
                        location.weather);

                buildUI();
            } else if (requestingHourlyWeather && location.hourlyWeather != null) {
                requestingHourlyWeather = false;
                trendView.setHourlyTemps(location.hourlyWeather);
                if (trendView.getState() == TrendView.LOADING_STATE) {
                    trendView.setState(TrendView.HOURLY_STATE);
                }
            }
            ((MainActivity) getActivity()).refreshLocation(location);
            setRefreshing(false);
        }
    }

    @Override
    public void requestWeatherFailed(Location result, boolean isDaily) {
        if (isDaily && location.weather == null) {
            location.weather = WeatherTable.readWeather(
                    MyDatabaseHelper.getDatabaseHelper(getActivity()),
                    location.location);
            location.history = HistoryTable.readYesterdayWeather(
                    MyDatabaseHelper.getDatabaseHelper(getActivity()),
                    location.weather);
            buildUI();
        } else if (requestingHourlyWeather && result.location.equals(location.location)) {
            requestingHourlyWeather = false;
            trendView.setState(TrendView.DAILY_STATE);
        }
        ((MainActivity) getActivity()).refreshLocation(location);
        Toast.makeText(getActivity(),
                getString(R.string.get_weather_data_failed),
                Toast.LENGTH_SHORT).show();
        setRefreshing(false);
    }

    // on request location listener.

    @Override
    public void requestLocationSuccess(String locationName) {
        WeatherUtils.requestWeather(getActivity(), location, locationName, true, this);
    }

    @Override
    public void requestLocationFailed() {
        LocationUtils.simpleLocationFailedFeedback(getActivity());
        if (location.weather == null) {
            location.weather = WeatherTable.readWeather(
                    MyDatabaseHelper.getDatabaseHelper(getActivity()),
                    location.location);
            location.history = HistoryTable.readYesterdayWeather(
                    MyDatabaseHelper.getDatabaseHelper(getActivity()),
                    location.weather);
            buildUI();
        }
        setRefreshing(false);
    }
}