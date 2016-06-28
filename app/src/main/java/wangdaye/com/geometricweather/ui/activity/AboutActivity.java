package wangdaye.com.geometricweather.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.ui.dialog.IntroduceDialog;
import wangdaye.com.geometricweather.ui.widget.StatusBarView;
import wangdaye.com.geometricweather.utils.DisplayUtils;

/**
 * Show application's details.
 * */

public class AboutActivity extends AppCompatActivity
        implements NestedScrollView.OnScrollChangeListener, View.OnClickListener {
    // widget
    private StatusBarView statusBar;

    // data
    private boolean started = false;
    private int scrollDistance = 0;
    private int totalDistance;

    /** <br> life cycle. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.setWindowTopColor(this, getString(R.string.about));
        DisplayUtils.setStatusBarTranslate(getWindow());
        DisplayUtils.setNavigationBarColor(this, getWindow(), true);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!started) {
            this.started = true;
            this.totalDistance = (int) DisplayUtils.dpToPx(this, 256);
            this.initWidget();
        }
    }

    /** <br> UI. */

    private void initWidget() {
        this.statusBar = (StatusBarView) findViewById(R.id.activity_about_statusBar);
        assert statusBar != null;
        statusBar.setAlpha(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_about_toolbar);
        assert toolbar != null;
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(this);

        ImageView designBackground = (ImageView) findViewById(R.id.activity_about_image);
        assert designBackground != null;
        Glide.with(this).load(R.drawable.design_background).into(designBackground);

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.activity_about_scrollView);
        assert scrollView != null;
        scrollView.setOnScrollChangeListener(this);

        ImageView icon = (ImageView) findViewById(R.id.container_about_app_icon);
        assert icon != null;
        Glide.with(this).load(R.drawable.ic_launcher).into(icon);

        RelativeLayout introductionContainer = (RelativeLayout) findViewById(R.id.container_about_app_introduce);
        assert introductionContainer != null;
        introductionContainer.setOnClickListener(this);
    }

    /** <br> interface. */

    // on click listener.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1:
                finish();
                break;

            case R.id.container_about_app_introduce:
                IntroduceDialog dialog = new IntroduceDialog();
                dialog.show(getFragmentManager(), null);
                break;
        }
    }

    // on scroll changed listener.

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        scrollDistance += scrollY - oldScrollY;
        float alpha = (float) (1.0 * scrollDistance / totalDistance);
        if (alpha > 1) {
            alpha = 1;
        } else if (alpha < 0) {
            alpha = 0;
        }
        statusBar.setAlpha(alpha);
    }
}
