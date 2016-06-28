package wangdaye.com.geometricweather.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.ui.fragment.SettingsFragment;
import wangdaye.com.geometricweather.utils.DisplayUtils;

/**
 * Settings activity.
 * */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    // data
    private boolean started = false;

    /** <br> life cycle. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.setWindowTopColor(this, getString(R.string.settings));
        DisplayUtils.setStatusBarTranslate(getWindow());
        DisplayUtils.setNavigationBarColor(this, getWindow(), true);
        this.setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!started) {
            started = true;

            this.initToolbar();
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.activity_settings_container, settingsFragment)
                    .commit();
        }
    }

    /** <br> UI. */

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_settings_toolbar);
        assert toolbar != null;
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(this);
    }

    /** <br> listener. */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1:
                finish();
                break;
        }
    }
}