package wangdaye.com.geometricweather.ui.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wangdaye.com.geometricweather.data.database.LocationTable;
import wangdaye.com.geometricweather.data.model.Location;
import wangdaye.com.geometricweather.data.database.MyDatabaseHelper;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.service.widget.WidgetClockDayService;
import wangdaye.com.geometricweather.service.widget.WidgetClockDayCenterService;
import wangdaye.com.geometricweather.service.widget.WidgetClockDayWeekService;
import wangdaye.com.geometricweather.service.widget.WidgetDayService;
import wangdaye.com.geometricweather.service.widget.WidgetDayWeekService;
import wangdaye.com.geometricweather.service.widget.WidgetWeekService;
import wangdaye.com.geometricweather.ui.dialog.IntroduceDialog;
import wangdaye.com.geometricweather.ui.dialog.ManageDialog;
import wangdaye.com.geometricweather.ui.fragment.WeatherFragment;
import wangdaye.com.geometricweather.ui.widget.StatusBarView;
import wangdaye.com.geometricweather.ui.widget.SwipeSwitchLayout;
import wangdaye.com.geometricweather.utils.DisplayUtils;
import wangdaye.com.geometricweather.utils.SafeHandler;
import wangdaye.com.geometricweather.utils.ShareUtils;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Main activity.
 * */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ManageDialog.OnLocationChangedListener,
        SafeHandler.HandlerContainer {
    // widget
    private StatusBarView statusBar;
    private ImageView navBackground;
    private WeatherFragment weatherFragment;
    private SafeHandler<MainActivity> handler;

    // data
    private boolean started;
    private List<Location> locationList;

    private int introduceVersionNow;
    private final int INTRODUCE_VERSION_CODE = 2;

    private final int LOCATION_PERMISSIONS_REQUEST_CODE = 1;
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2;

    private final int INTRODUCE_DIALOG = 0;
    private final int SETTINGS_ACTIVITY = 1;
    public final int SHARE_ACTIVITY = 2;

    /** <br> life cycle. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initData();
        this.handler = new SafeHandler<>(this);
        DisplayUtils.setWindowTopColor(this);
        DisplayUtils.setNavigationBarColor(this, getWindow(), TimeUtils.getInstance(this).isDay);
        DisplayUtils.setStatusBarTranslate(getWindow());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (introduceVersionNow < INTRODUCE_VERSION_CODE) {
            this.guideUser();
        } else {
            this.buildUI();
        }
    }

    public void buildUI() {
        if (!started) {
            started = true;
            this.initWidget();
        }
        if (weatherFragment == null) {
            weatherFragment = new WeatherFragment();
            weatherFragment.setLocation(locationList.get(0), true);
            changeFragment(weatherFragment);
        } else {
            weatherFragment.showSkyCircles();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawerLayout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS_ACTIVITY:
                DisplayUtils.setNavigationBarColor(this, getWindow(), TimeUtils.getInstance(this).isDay);
                sendNotification();
                break;
            case SHARE_ACTIVITY:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String oldUri = sharedPreferences.getString(getString(R.string.key_share_uri), "null");
                if (! oldUri.equals("null")) {
                    ShareUtils.deleteSharePicture(this, Uri.parse(oldUri));
                }
                break;
        }
    }

    private void guideUser() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            IntroduceDialog introduceDialog = new IntroduceDialog();
            introduceDialog.show(getFragmentManager(), null);
        } else {
            this.requestPermission(LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    /** <br> permission. */

    private void requestPermission(int permissionCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        switch (permissionCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE:
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.INSTALL_LOCATION_PROVIDER) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSIONS_REQUEST_CODE);
                }
                break;

            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putInt(getString(R.string.key_introduce_version_code), INTRODUCE_VERSION_CODE);
                editor.apply();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = INTRODUCE_DIALOG;
                        handler.sendMessage(msg);
                    }
                }, 100);
                break;

            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (weatherFragment != null) {
                    ShareUtils.shareWeather(this, weatherFragment.location.weather);
                }
                break;
        }
    }

    /** <br> UI. */

    private void initWidget() {
        this.statusBar = (StatusBarView) findViewById(R.id.container_main_statusBar);
        this.initStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.container_main_toolbar);
        assert toolbar != null;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_nav);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);

        ImageView headerIcon = (ImageView) navHeader.findViewById(R.id.container_nav_header_icon);
        Glide.with(this).load(R.drawable.ic_launcher).into(headerIcon);

        this.navBackground = (ImageView) navHeader.findViewById(R.id.container_nav_header_background);
        this.initNavHeaderBackground();
    }

    public void initNavHeaderBackground() {
        boolean isDay = TimeUtils.getInstance(this).isDay;
        int navBackId = isDay ? R.drawable.nav_head_day : R.drawable.nav_head_night;
        Glide.with(this).load(navBackId).into(navBackground);
    }

    public void initStatusBarColor() {
        statusBar.setBackgroundResource(
                TimeUtils.getInstance(this).isDay ?
                        R.color.lightPrimary_5 : R.color.darkPrimary_5);
    }

    public void showCircularSky() {
        if (weatherFragment != null) {
            weatherFragment.showSkyCircles();
        }
    }

    /** <br> data. */

    private void initData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        this.started = false;
        this.locationList = LocationTable.readLocationList(
                this,
                MyDatabaseHelper.getDatabaseHelper(this));
        this.introduceVersionNow = sharedPreferences.getInt(getString(R.string.key_introduce_version_code), 0);
    }

    public void switchCity(String name, int swipeDir) {
        for (int i = 0; i < locationList.size(); i ++) {
            if (locationList.get(i).location.equals(name)) {
                int position = swipeDir == SwipeSwitchLayout.DIRECTION_LEFT ?
                        i + 1 : i - 1;
                if (position < 0) {
                    position = locationList.size() - 1;
                } else if (position > locationList.size() - 1) {
                    position = 0;
                }
                weatherFragment.setLocation(locationList.get(position), true);
                weatherFragment.reset();
                return;
            }
        }
        weatherFragment.setLocation(locationList.get(0), true);
        weatherFragment.reset();
    }

    public void addLocation(Location location) {
        LocationTable.writeLocation(
                MyDatabaseHelper.getDatabaseHelper(this),
                location.location);
        locationList.add(location);
    }

    public void deleteLocation(Location location) {
        LocationTable.deleteLocation(
                MyDatabaseHelper.getDatabaseHelper(this),
                location.location);
        for (int i = 0; i < locationList.size(); i ++) {
            if (locationList.get(i).location.equals(location.location)) {
                locationList.remove(i);
                break;
            }
        }
    }

    public void refreshLocation(Location location) {
        for (int i = 0; i < locationList.size(); i ++) {
            if (locationList.get(i).location.equals(location.location)) {
                locationList.remove(i);
                locationList.add(i, location);
                break;
            }
        }
    }

    /** <br> fragment. */

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_main_fragment, fragment);
        fragmentTransaction.commit();
    }

    /** <br> widget & notification. */

    // widget.

    public void refreshWidgetView() {
        SharedPreferences sharedPreferences;
        String locationName;

        // day
        sharedPreferences = getSharedPreferences(
                getString(R.string.sp_widget_day_setting),
                Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(
                getString(R.string.key_location),
                getString(R.string.local));
        if (weatherFragment.location.location.equals(locationName)) {
            WidgetDayService.refreshWidgetView(this, weatherFragment.location.weather);
        }

        // week
        sharedPreferences = getSharedPreferences(
                getString(R.string.sp_widget_week_setting),
                Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(
                getString(R.string.key_location),
                getString(R.string.local));
        if (weatherFragment.location.location.equals(locationName)) {
            WidgetWeekService.refreshWidgetView(this, weatherFragment.location.weather);
        }

        // day week
        sharedPreferences = getSharedPreferences(
                getString(R.string.sp_widget_day_week_setting),
                Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(
                getString(R.string.key_location),
                getString(R.string.local));
        if (weatherFragment.location.location.equals(locationName)) {
            WidgetDayWeekService.refreshWidgetView(this, weatherFragment.location.weather);
        }

        // clock day
        sharedPreferences = getSharedPreferences(
                getString(R.string.sp_widget_clock_day_setting),
                Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(
                getString(R.string.key_location),
                getString(R.string.local));
        if (weatherFragment.location.location.equals(locationName)) {
            WidgetClockDayService.refreshWidgetView(this, weatherFragment.location.weather);
        }

        // clock day center
        sharedPreferences = getSharedPreferences(
                getString(R.string.sp_widget_clock_day_center_setting),
                Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(
                getString(R.string.key_location),
                getString(R.string.local));
        if (weatherFragment.location.location.equals(locationName)) {
            WidgetClockDayCenterService.refreshWidgetView(this, weatherFragment.location.weather);
        }

        // clock day week
        sharedPreferences = getSharedPreferences(
                getString(R.string.sp_widget_clock_day_week_setting),
                Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(
                getString(R.string.key_location),
                getString(R.string.local));
        if (weatherFragment.location.location.equals(locationName)) {
            WidgetClockDayWeekService.refreshWidgetView(this, weatherFragment.location.weather);
        }
    }

    public void sendNotification() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean(
                getString(R.string.key_notification_switch),
                false)) {
            WidgetAndNotificationUtils.sendNotification(this,
                    weatherFragment.location.weather,
                    false);
        }
    }

    /** <br> interface. */

    // menu.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                boolean shared = sharedPreferences.getBoolean(getString(R.string.key_shared), false);
                if (!shared) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(getString(R.string.key_shared), true);
                    editor.apply();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        ShareUtils.shareWeather(this, weatherFragment.location.weather);
                    } else {
                        requestPermission(WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }
                } else {
                    ShareUtils.shareWeather(this, weatherFragment.location.weather);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // drawer.

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_manage:
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = R.id.nav_manage;
                        handler.sendMessage(msg);
                    }
                }, 400);
                break;

            case R.id.nav_settings:
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = R.id.nav_settings;
                        handler.sendMessage(msg);
                    }
                }, 400);
                break;

            case R.id.nav_about:
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = R.id.nav_about;
                        handler.sendMessage(msg);
                    }
                }, 400);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_drawerLayout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    // on location changed listener.

    @Override
    public void selectLocation(String result) {
        Location location = null;
        boolean isCollected = false;
        for (Location l : locationList) {
            if (l.location.equals(result)) {
                location = l;
                isCollected = true;
                break;
            }
        }
        if (location == null) {
            location = new Location(result);
        }
        if (weatherFragment != null) {
            weatherFragment.setLocation(location, isCollected);
            weatherFragment.reset();
        }
    }

    @Override
    public void changeLocationList(List<String> nameList) {
        List<Location> newList = new ArrayList<>();
        for (int i = 0; i < nameList.size(); i ++) {
            for (Location l : locationList) {
                if (l.location.equals(nameList.get(i))) {
                    newList.add(l);
                    break;
                }
            }
            if (newList.size() - 1 < i) {
                newList.add(new Location(nameList.get(i)));
            }
        }
        MyDatabaseHelper helper = MyDatabaseHelper.getDatabaseHelper(this);
        for (Location l : locationList) {
            LocationTable.deleteLocation(helper, l.location);
        }
        locationList = newList;
        for (Location l : locationList) {
            LocationTable.writeLocation(helper, l.location);
        }
    }

    // handler.

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.nav_manage:
                ManageDialog manageDialog = new ManageDialog();
                manageDialog.setOnLocationChangedListener(this);
                manageDialog.show(getFragmentManager(), null);
                break;

            case R.id.nav_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivityForResult(intentSettings, SETTINGS_ACTIVITY);
                break;

            case R.id.nav_about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;

            case INTRODUCE_DIALOG:
                IntroduceDialog introduceDialog = new IntroduceDialog();
                introduceDialog.show(getFragmentManager(), null);
                break;
        }
    }
}
