package wangdaye.com.geometricweather.ui.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.service.notification.NotificationService;
import wangdaye.com.geometricweather.service.notification.TimeService;
import wangdaye.com.geometricweather.ui.dialog.TimeSetterDialog;
import wangdaye.com.geometricweather.utils.DisplayUtils;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Settings fragment.
 * */

public class SettingsFragment extends PreferenceFragment
        implements TimeSetterDialog.OnTimeChangedListener {

    /** <br> life cycle. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference);

        this.initForecastPart();
        this.initNotificationPart();
    }

    /** <br> UI. */

    private void initForecastPart() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // set today forecast time & todayForecastType.
        Preference setTodayForecastTime = findPreference(getString(R.string.set_forecast_time_today));
        String summaryToday = sharedPreferences.getString(getString(R.string.key_forecast_time_today), "07:00");
        setTodayForecastTime.setSummary(summaryToday);
        ListPreference todayForecastType = (ListPreference) findPreference(getString(R.string.key_forecast_type_today));

        // set tomorrow forecast time & tomorrowForecastType.
        Preference setTomorrowForecastTime = findPreference(getString(R.string.set_forecast_time_tomorrow));
        String summaryTomorrow = sharedPreferences.getString(getString(R.string.key_forecast_time_tomorrow), "21:00");
        setTomorrowForecastTime.setSummary(summaryTomorrow);
        ListPreference tomorrowForecastType = (ListPreference) findPreference(getString(R.string.key_forecast_type_tomorrow));

        if (sharedPreferences.getBoolean(getString(R.string.key_timing_forecast_switch_today), false)) {
            // open today forecast.
            // start service.
            Intent intent = new Intent(getActivity(), TimeService.class);
            getActivity().startService(intent);
            // set item enable.
            setTodayForecastTime.setEnabled(true);
            todayForecastType.setEnabled(true);
        } else {
            // set item enable.
            setTodayForecastTime.setEnabled(false);
            todayForecastType.setEnabled(false);
        }

        if (sharedPreferences.getBoolean(getString(R.string.key_timing_forecast_switch_tomorrow), false)) {
            // open tomorrow forecast.
            Intent intent = new Intent(getActivity(), TimeService.class);
            getActivity().startService(intent);
            setTomorrowForecastTime.setEnabled(true);
            tomorrowForecastType.setEnabled(true);
        } else {
            setTomorrowForecastTime.setEnabled(false);
            tomorrowForecastType.setEnabled(false);
        }

        if (!sharedPreferences.getBoolean(getString(R.string.key_timing_forecast_switch_today), false)
                && !sharedPreferences.getBoolean(getString(R.string.key_timing_forecast_switch_tomorrow), false)) {
            // close forecast.
            Intent intent = new Intent(getActivity(), TimeService.class);
            getActivity().stopService(intent);
        }
    }

    private void initNotificationPart() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // get items.
        ListPreference notificationTextColor = (ListPreference) findPreference(getString(R.string.key_notification_text_color));
        CheckBoxPreference notificationBackground = (CheckBoxPreference) findPreference(getString(R.string.key_notification_background_color_switch));
        CheckBoxPreference notificationClearFlag = (CheckBoxPreference) findPreference(getString(R.string.key_notification_can_clear_switch));
        CheckBoxPreference notificationIconBehavior = (CheckBoxPreference) findPreference(getString(R.string.key_hide_notification_icon));
        CheckBoxPreference notificationHideBehavior = (CheckBoxPreference) findPreference(getString(R.string.key_hide_notification_in_lockScreen));
        CheckBoxPreference notificationAutoRefresh = (CheckBoxPreference) findPreference(getString(R.string.key_notification_auto_refresh_switch));
        ListPreference notificationRefreshTime = (ListPreference) findPreference(getString(R.string.key_notification_time));
        CheckBoxPreference notificationSoundSwitch = (CheckBoxPreference) findPreference(getString(R.string.key_notification_sound_switch));
        CheckBoxPreference notificationShockSwitch = (CheckBoxPreference) findPreference(getString(R.string.key_notification_shock_switch));

        if(sharedPreferences.getBoolean(getString(R.string.key_notification_switch), false)) {
            // open notification.
            notificationTextColor.setEnabled(true);
            notificationBackground.setEnabled(true);
            notificationClearFlag.setEnabled(true);
            notificationIconBehavior.setEnabled(true);
            notificationHideBehavior.setEnabled(true);
            notificationAutoRefresh.setEnabled(true);
            if(sharedPreferences.getBoolean(getString(R.string.key_notification_auto_refresh_switch), false)) {
                // open auto refresh.
                notificationRefreshTime.setEnabled(true);
                notificationSoundSwitch.setEnabled(true);
                notificationShockSwitch.setEnabled(true);
            } else {
                notificationRefreshTime.setEnabled(false);
                notificationSoundSwitch.setEnabled(false);
                notificationShockSwitch.setEnabled(false);
            }
        } else {
            // close notification.
            notificationTextColor.setEnabled(false);
            notificationBackground.setEnabled(false);
            notificationClearFlag.setEnabled(false);
            notificationIconBehavior.setEnabled(false);
            notificationHideBehavior.setEnabled(false);
            notificationAutoRefresh.setEnabled(false);
            notificationRefreshTime.setEnabled(false);
            notificationSoundSwitch.setEnabled(false);
            notificationShockSwitch.setEnabled(false);
        }
    }

    /** <br> data. */

    private void startNotificationService() {
        Intent intent = new Intent(getActivity(), NotificationService.class);
        getActivity().startService(intent);
    }

    private void stopNotificationService() {
        Intent intent = new Intent(getActivity(), NotificationService.class);
        getActivity().stopService(intent);
    }

    /** interface. */

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals(getString(R.string.key_hide_star))) {
            // hide stars.
            Toast.makeText(getActivity(),
                    getString(R.string.please_refresh),
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_navigation_bar_color_switch))) {
            // navigation bar color.
            DisplayUtils.setNavigationBarColor(getActivity(), getActivity().getWindow(), true);
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_timing_forecast_switch_today))) {
            // timing forecast today.
            initForecastPart();
            return true;
        } else if (preference.getKey().equals(getString(R.string.set_forecast_time_today))) {
            // set today forecast time.
            TimeSetterDialog dialog = new TimeSetterDialog();
            dialog.setModel(true);
            dialog.setOnTimeChangedListener(this);
            dialog.show(getFragmentManager(), "TimeSetterDialog");
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_timing_forecast_switch_tomorrow))) {
            // timing forecast tomorrow.
            initForecastPart();
            return true;
        } else if (preference.getKey().equals(getString(R.string.set_forecast_time_tomorrow))) {
            // set tomorrow forecast time.
            TimeSetterDialog dialog = new TimeSetterDialog();
            dialog.setModel(false);
            dialog.setOnTimeChangedListener(this);
            dialog.show(getFragmentManager(), "TimeSetterDialog");
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_notification_switch))) {
            // notification switch.
            initNotificationPart();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (sharedPreferences.getBoolean(getString(R.string.key_notification_switch), false)) {
                // open notification.
                if (sharedPreferences.getBoolean(getString(R.string.key_notification_auto_refresh_switch), false)) {
                    // open auto refresh.
                    startNotificationService();
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.refresh_notification_now),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // close auto refresh.
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.refresh_notification_after_back),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                // close notification.
                stopNotificationService();
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(WidgetAndNotificationUtils.NOTIFICATION_ID);
                notificationManager.cancel(WidgetAndNotificationUtils.FORECAST_ID);
            }
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_notification_text_color))) {
            // notification text color.
            Toast.makeText(
                    getActivity(),
                    getString(R.string.refresh_notification_after_back),
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_notification_background_color_switch))) {
            // notification background.
            Toast.makeText(
                    getActivity(),
                    getString(R.string.refresh_notification_after_back),
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_notification_can_clear_switch))) {
            // notification clear flag.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if(sharedPreferences.getBoolean(getString(R.string.key_notification_switch), false)) {
                Toast.makeText(
                        getActivity(),
                        getString(R.string.refresh_notification_after_back),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (preference.getKey().equals(getString(R.string.key_notification_auto_refresh_switch))) {
            // notification auto refresh.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if(sharedPreferences.getBoolean(getString(R.string.key_notification_auto_refresh_switch), false)) {
                startNotificationService();
                Toast.makeText(
                        getActivity(),
                        getString(R.string.refresh_notification_now),
                        Toast.LENGTH_SHORT).show();
            } else {
                stopNotificationService();
            }
            this.initNotificationPart();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void timeChanged() {
        this.initForecastPart();
    }
}