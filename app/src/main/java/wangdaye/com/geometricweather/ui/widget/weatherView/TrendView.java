package wangdaye.com.geometricweather.ui.widget.weatherView;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.model.History;
import wangdaye.com.geometricweather.data.model.HourlyWeather;
import wangdaye.com.geometricweather.data.model.Weather;

/**
 * Trend view.
 * */

public class TrendView extends FrameLayout implements View.OnClickListener {
    // widget
    private RelativeLayout dailyContainer;
    private DailyView dailyView;

    private RelativeLayout hourlyContainer;
    private HourlyView hourlyView;

    private RelativeLayout loadingContainer;
    private CircularProgressView progress;

    private OnStateChangedListener listener;

    // data
    private int state;
    public static final int DAILY_STATE = 0;
    public static final int LOADING_STATE = 1;
    public static final int HOURLY_STATE = 2;

    // animator
    private AnimatorSet viewIn;
    private AnimatorSet viewOut;

    /** <br> life cycle. */

    public TrendView(Context context) {
        super(context);
        this.initialize();
    }

    public TrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public TrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TrendView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.container_trend_view, null);
        addView(view);
        this.setOnClickListener(this);

        dailyContainer = (RelativeLayout) findViewById(R.id.container_temperature_daily);
        dailyView = (DailyView) findViewById(R.id.container_temperature_daily_trendView);

        hourlyContainer = (RelativeLayout) findViewById(R.id.container_temperature_hourly);
        hourlyView = (HourlyView) findViewById(R.id.container_temperature_hourly_trendView);

        loadingContainer = (RelativeLayout) findViewById(R.id.container_loading_view);
        progress = (CircularProgressView) findViewById(R.id.container_loading_view_progress);

        this.setColorResource(R.color.lightPrimary_3);

        viewIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.view_in);
        viewIn.addListener(viewInListener);
        viewOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.view_out);
        viewOut.addListener(viewOutListener);

        dailyContainer.setVisibility(VISIBLE);
        hourlyContainer.setVisibility(GONE);
        loadingContainer.setVisibility(GONE);
    }

    /** <br> state. */

    public void setState(int stateTo) {
        if (stateTo == state) {
            return;
        }
        this.state = stateTo;
        switch (state) {
            case DAILY_STATE:
                if (hourlyContainer.getVisibility() == VISIBLE) {
                    viewOut.setTarget(hourlyContainer);
                } else {
                    viewOut.setTarget(loadingContainer);
                }
                viewIn.setTarget(dailyContainer);
                break;

            case LOADING_STATE:
                viewOut.setTarget(dailyContainer);
                viewIn.setTarget(loadingContainer);
                break;

            case HOURLY_STATE:
                if (dailyContainer.getVisibility() == VISIBLE) {
                    viewOut.setTarget(dailyContainer);
                } else {
                    viewOut.setTarget(loadingContainer);
                }
                viewIn.setTarget(hourlyContainer);
                break;
        }
        setEnabled(false);
        viewOut.start();
    }

    public int getState() {
        return state;
    }

    /** data. */

    public void setColorResource(int colorResource) {
        int color = ContextCompat.getColor(getContext(), colorResource);
        progress.setColor(color);
    }

    public void setDailyTemps(Weather weather, History history) {
        if (weather == null) {
            return;
        }
        int[] maxiTemps = new int[weather.maxiTemps.length];
        int[] miniTemps = new int[maxiTemps.length];
        for (int i = 0; i < maxiTemps.length; i ++) {
            maxiTemps[i] = Integer.parseInt(weather.maxiTemps[i]);
            miniTemps[i] = Integer.parseInt(weather.miniTemps[i]);
        }

        int[] yesterdayTemps = null;
        if (history != null) {
            yesterdayTemps = new int[] {
                    Integer.parseInt(history.maxiTemp),
                    Integer.parseInt(history.miniTemp)};
        }
        dailyView.setData(maxiTemps, miniTemps, yesterdayTemps);
    }

    public void setHourlyTemps(HourlyWeather hourlyWeather) {
        if (hourlyWeather == null) {
            return;
        }
        hourlyView.setData(hourlyWeather.temps, hourlyWeather.pops);
    }

    /** <br> listener. */

    private AnimatorListenerAdapter viewOutListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            switch (state) {
                case DAILY_STATE:
                    dailyContainer.setVisibility(VISIBLE);
                    hourlyContainer.setVisibility(GONE);
                    loadingContainer.setVisibility(GONE);
                    break;

                case LOADING_STATE:
                    dailyContainer.setVisibility(GONE);
                    loadingContainer.setVisibility(VISIBLE);
                    hourlyContainer.setVisibility(GONE);
                    break;

                case HOURLY_STATE:
                    dailyContainer.setVisibility(GONE);
                    loadingContainer.setVisibility(GONE);
                    hourlyContainer.setVisibility(VISIBLE);
                    break;
            }
            viewIn.start();
        }
    };

    private AnimatorListenerAdapter viewInListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setEnabled(true);
            if (listener != null) {
                listener.onStateChange(state);
            }
        }
    };

    /** <br> interface. */

    public interface OnStateChangedListener {
        void onClickTrendView(int state);
        void onStateChange(int state);
    }

    public void setOnStateChangedListener(OnStateChangedListener l) {
        this.listener = l;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClickTrendView(state);
        }
    }
}
