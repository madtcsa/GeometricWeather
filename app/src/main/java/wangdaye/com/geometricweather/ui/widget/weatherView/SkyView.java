package wangdaye.com.geometricweather.ui.widget.weatherView;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.model.Weather;
import wangdaye.com.geometricweather.utils.TimeUtils;
import wangdaye.com.geometricweather.utils.WeatherUtils;

/**
 * Sky weather view.
 * */

public class SkyView extends FrameLayout {
    // widget
    private CircularSkyView circularSkyView;
    private ImageView[] flagIcons;
    private ImageView[] starts;

    // data
    private int[] imageIds;

    // animator
    private AnimatorSet[] iconTouchAnimators;
    private AnimatorSet[] starShineAnimators;
    private AnimatorSet iconRiseAnimator;
    private AnimatorSet iconFallAnimator;

    /** <br> life cycle. */

    public SkyView(Context context) {
        super(context);
        this.initialize();
    }

    public SkyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public SkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SkyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.container_sky_view, null);
        addView(view);

        this.circularSkyView = (CircularSkyView) findViewById(R.id.container_sky_view_circularSkyView);

        this.flagIcons = new ImageView[] {
                (ImageView) findViewById(R.id.container_sky_view_icon_1),
                (ImageView) findViewById(R.id.container_sky_view_icon_2),
                (ImageView) findViewById(R.id.container_sky_view_icon_3)};
        imageIds = new int[3];
        iconTouchAnimators = new AnimatorSet[3];

        this.starts = new ImageView[] {
                (ImageView) findViewById(R.id.container_sky_view_star_1),
                (ImageView) findViewById(R.id.container_sky_view_star_2)};
        Glide.with(getContext()).load(R.drawable.star_1).into(starts[0]);
        Glide.with(getContext()).load(R.drawable.star_2).into(starts[1]);

        this.starShineAnimators = new AnimatorSet[] {
                (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.start_shine_1),
                (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.start_shine_2)};
        for (int i = 0; i < starShineAnimators.length; i ++) {
            starShineAnimators[i].addListener(starShineAnimatorListeners[i]);
            starShineAnimators[i].setTarget(starts[i]);
            starts[i].setVisibility(GONE);
        }

        FrameLayout iconContainer = (FrameLayout) findViewById(R.id.container_sky_view_iconContainer);

        this.iconRiseAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.icon_rise);
        iconRiseAnimator.addListener(iconRiseAnimatorListener);
        iconRiseAnimator.setTarget(iconContainer);

        this.iconFallAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.icon_fall);
        iconFallAnimator.addListener(iconFallAnimatorListener);
        iconFallAnimator.setTarget(iconContainer);
    }

    /** <br> UI. */

    public void reset() {
        boolean isDay = TimeUtils.getInstance(getContext()).isDay;

        if (circularSkyView.getState() == CircularSkyView.INITIAL_STATE) {
            circularSkyView.showCircle(TimeUtils.getInstance(getContext()).isDay);
        }

        for (ImageView i : flagIcons) {
            i.setVisibility(View.GONE);
        }

        if (isDay) {
            for (int i = 0; i < starts.length; i ++) {
                if (starts[i].getVisibility() == VISIBLE) {
                    starts[i].setVisibility(View.GONE);
                    starShineAnimators[i].end();
                }
            }
        } else {
            for (int i = 0; i < starts.length; i ++) {
                if (starts[i].getVisibility() == GONE) {
                    starts[i].setVisibility(View.VISIBLE);
                    starShineAnimators[i].start();
                }
            }
        }
    }

    public void showCircles() {
        circularSkyView.showCircle(TimeUtils.getInstance(getContext()).isDay);
        if (TimeUtils.getInstance(getContext()).isDay) {
            for (int i = 0; i < starts.length; i ++) {
                if (starts[i].getVisibility() == VISIBLE) {
                    starts[i].setVisibility(View.GONE);
                    starShineAnimators[i].end();
                }
            }
        } else {
            for (int i = 0; i < starts.length; i ++) {
                if (starts[i].getVisibility() == GONE) {
                    starts[i].setVisibility(View.VISIBLE);
                    starShineAnimators[i].start();
                }
            }
        }
    }

    public void iconRise() {
        iconRiseAnimator.start();
    }

    public void iconFall() {
        iconFallAnimator.start();
    }

    public void onClickSky() {
        circularSkyView.touchCircle();
        for (int i = 0; i < flagIcons.length; i ++) {
            if (imageIds[i] != 0 && iconTouchAnimators[i] != null) {
                iconTouchAnimators[i].start();
            }
        }
    }

    public void setWeather(Weather weather) {
        if (weather == null) {
            return;
        }

        boolean isDay = TimeUtils.getInstance(getContext()).isDay;

        int[] animatorIds = WeatherUtils.getAnimatorId(weather.weatherKindNow, isDay);
        iconTouchAnimators = new AnimatorSet[animatorIds.length];
        for (int i = 0; i < iconTouchAnimators.length; i ++) {
            if (animatorIds[i] != 0) {
                iconTouchAnimators[i] = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), animatorIds[i]);
                iconTouchAnimators[i].setTarget(flagIcons[i]);
            }
        }

        imageIds = WeatherUtils.getWeatherIcon(weather.weatherKindNow, isDay);
        this.iconFall();
        this.showCircles();
    }

    private void setFlagIconsImage() {
        for (int i = 0; i < flagIcons.length; i ++) {
            if (imageIds[i] == 0) {
                flagIcons[i].setVisibility(GONE);
            } else {
                Glide.with(getContext()).load(imageIds[i]).into(flagIcons[i]);
                flagIcons[i].setVisibility(VISIBLE);
            }
        }
    }

    /** <br> listener. */

    private AnimatorListenerAdapter[] starShineAnimatorListeners = new AnimatorListenerAdapter[] {
            // 1.
            new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    starShineAnimators[0].start();
                }
            },
            // 2.
            new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    starShineAnimators[1].start();
                }
            }
    };

    private AnimatorListenerAdapter iconFallAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setFlagIconsImage();
            iconRiseAnimator.start();
        }
    };

    private AnimatorListenerAdapter iconRiseAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            for (int i = 0; i < iconTouchAnimators.length; i ++) {
                if (imageIds[i] != 0 && iconTouchAnimators[i] != null) {
                    iconTouchAnimators[i].start();
                }
            }
        }
    };
}
