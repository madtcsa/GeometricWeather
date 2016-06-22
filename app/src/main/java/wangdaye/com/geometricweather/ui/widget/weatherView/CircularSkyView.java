package wangdaye.com.geometricweather.ui.widget.weatherView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.DisplayUtils;
import wangdaye.com.geometricweather.utils.TimeUtils;

/**
 * Show the sky.
 * */

public class CircularSkyView extends View {
    // widget
    private Paint paint;

    // data
    private int drawTime;
    private float cX;
    private float cY;
    private final double proportion = 6.8;

    private final int SHOW_FIRST_FLOOR_TIME = 15;
    private final int SHOW_OTHERS_FLOOR_TIME = 22;
    private final int SHOW_BACKGROUND_TIME = SHOW_FIRST_FLOOR_TIME + 3 * SHOW_OTHERS_FLOOR_TIME;

    private final int HIDE_FIRST_FLOOR_TIME = 6;
    private final int HIDE_SECOND_FLOOR_TIME = 15;
    private final int HIDE_THIRD_FLOOR_TIME = 25;
    private final int HIDE_FORTH_FLOOR_TIME = 34;
    private final int HIDE_BACKGROUND_TIME = 34;

    private final int TOUCH_DAY_UNIT_TIME = 6;
    private final int TOUCH_NIGHT_UNIT_TIME = 12;

    private boolean isDay;
    private int stateNow;
    public static final int INITIAL_STATE = 0;
    public static final int SHOWING_STATE = 1;
    public static final int NORMAL_STATE = 2;
    public static final int TOUCHING_STATE = 3;
    public static final int HIDING_STATE = 4;

    /** <br> life cycle. */

    public CircularSkyView(Context context) {
        super(context);
        this.initialize();
    }

    public CircularSkyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public CircularSkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularSkyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    private void initialize() {
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        this.isDay = TimeUtils.getInstance(getContext()).isDay;
        this.drawTime = 0;
    }

    /** <br> state. */

    private void setState(int stateTo) {
        if (stateNow != stateTo) {
            stateNow = stateTo;
            invalidate();
        }
    }

    public int getState() {
        return stateNow;
    }

    public void showCircle(boolean isDay) {
        if (this.isDay != isDay) {
            this.isDay = isDay;
            setState(HIDING_STATE);
        } else {
            setState(SHOWING_STATE);
        }
    }

    public void touchCircle() {
        if (stateNow == NORMAL_STATE) {
            setState(TOUCHING_STATE);
        }
    }

    /** <br> measure */

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (width / 6.8 * 4 + DisplayUtils.dpToPx(getContext(), 60));
        setMeasuredDimension(width, height);

        cX = (float) (getMeasuredWidth() / 2.0);
        cY = getMeasuredHeight();
    }

    /** <br> draw. */

    // on draw.

    @Override
    protected void onDraw(Canvas canvas) {
        switch (stateNow) {
            case INITIAL_STATE:
                drawTime = 0;
                break;

            case SHOWING_STATE:
                drawTime ++;
                showBackground(canvas);
                showForthFloor(canvas);
                showThirdFloor(canvas);
                showSecondFloor(canvas);
                showFirstFloor(canvas);
                if (drawTime < SHOW_FIRST_FLOOR_TIME + 3 * SHOW_OTHERS_FLOOR_TIME) {
                    invalidate();
                } else {
                    setState(NORMAL_STATE);
                }
                break;

            case NORMAL_STATE:
                drawTime ++;
                showBackground(canvas);
                showForthFloor(canvas);
                showThirdFloor(canvas);
                showSecondFloor(canvas);
                showFirstFloor(canvas);
                drawTime = 0;
                break;

            case TOUCHING_STATE:
                drawTime ++;
                touchBackground(canvas);
                touchForthFloor(canvas);
                touchThirdFloor(canvas);
                touchSecondFloor(canvas);
                touchFirstFloor(canvas);
                if (isDay) {
                    if (drawTime < TOUCH_DAY_UNIT_TIME * 6) {
                        invalidate();
                    } else {
                        drawTime = SHOW_FIRST_FLOOR_TIME + 3 * SHOW_OTHERS_FLOOR_TIME;
                        setState(NORMAL_STATE);
                    }
                } else {
                    if (drawTime < TOUCH_NIGHT_UNIT_TIME * 6) {
                        invalidate();
                    } else {
                        drawTime = SHOW_FIRST_FLOOR_TIME + 3 * SHOW_OTHERS_FLOOR_TIME;
                        setState(NORMAL_STATE);
                    }
                }
                break;

            case HIDING_STATE:
                drawTime ++;
                hideBackground(canvas);
                hideForthFloor(canvas);
                hideThirdFloor(canvas);
                hideSecondFloor(canvas);
                hideFirstFloor(canvas);

                if (drawTime < HIDE_BACKGROUND_TIME) {
                    invalidate();
                } else {
                    drawTime = 0;
                    setState(SHOWING_STATE);
                }
                break;
        }
    }

    // show.

    private void showFirstFloor(Canvas canvas) {
        float radius = (float) (getMeasuredWidth() / proportion);
        float radiusNow = drawTime < SHOW_FIRST_FLOOR_TIME ?
                (float) (1.0 * radius / SHOW_FIRST_FLOOR_TIME * drawTime) : radius;

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_1));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_1));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void showSecondFloor(Canvas canvas) {
        if (drawTime <= SHOW_FIRST_FLOOR_TIME) {
            return;
        }

        float radius = (float) (2 * getMeasuredWidth() / proportion);
        float radiusNow = drawTime < SHOW_FIRST_FLOOR_TIME + SHOW_OTHERS_FLOOR_TIME ?
                (float) (0.5 * radius + 0.5 * radius / SHOW_OTHERS_FLOOR_TIME * (drawTime - SHOW_FIRST_FLOOR_TIME))
                :
                radius;

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_2));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_2));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void showThirdFloor(Canvas canvas) {
        if (drawTime <= SHOW_FIRST_FLOOR_TIME + SHOW_OTHERS_FLOOR_TIME) {
            return;
        }

        float radius = (float) (3 * getMeasuredWidth() / proportion);
        float radiusNow = drawTime < SHOW_FIRST_FLOOR_TIME + 2 * SHOW_OTHERS_FLOOR_TIME ?
                (float) (2.0 / 3.0 * radius + 1.0 / 3.0 * radius / SHOW_OTHERS_FLOOR_TIME * (drawTime - SHOW_FIRST_FLOOR_TIME - SHOW_OTHERS_FLOOR_TIME))
                :
                radius;

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_3));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_3));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void showForthFloor(Canvas canvas) {
        if (drawTime <= SHOW_FIRST_FLOOR_TIME + 2 * SHOW_OTHERS_FLOOR_TIME) {
            return;
        }

        float radius = (float) (4 * getMeasuredWidth() / proportion);
        float radiusNow = drawTime < SHOW_FIRST_FLOOR_TIME + 3 * SHOW_OTHERS_FLOOR_TIME ?
                (float) (3.0 / 4.0 * radius + 1.0 / 4.0 * radius / SHOW_OTHERS_FLOOR_TIME * (drawTime - SHOW_FIRST_FLOOR_TIME - 2 * SHOW_OTHERS_FLOOR_TIME))
                :
                radius;

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_4));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_4));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void showBackground(Canvas canvas) {
        int alpha = drawTime < SHOW_BACKGROUND_TIME ?
                (int) (255.0 * drawTime / SHOW_BACKGROUND_TIME) : 255;
        if (isDay) {
            canvas.drawColor(Color.argb(alpha, 117, 190, 203));
        } else {
            canvas.drawColor(Color.argb(alpha, 26, 27, 34));
        }
    }

    // hide.

    private void hideFirstFloor(Canvas canvas) {
        if (drawTime > HIDE_FIRST_FLOOR_TIME) {
            return;
        }

        float radius = (float) (getMeasuredWidth() / proportion);
        float radiusNow = drawTime < HIDE_FIRST_FLOOR_TIME ?
                radius / HIDE_FIRST_FLOOR_TIME * (HIDE_FIRST_FLOOR_TIME - drawTime)
                :
                0;

        if (!isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_1));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_1));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void hideSecondFloor(Canvas canvas) {
        if (drawTime > HIDE_SECOND_FLOOR_TIME) {
            return;
        }

        float radius = (float) (getMeasuredWidth() / proportion);
        float radiusNow = drawTime < HIDE_SECOND_FLOOR_TIME ?
                radius / HIDE_SECOND_FLOOR_TIME * (HIDE_SECOND_FLOOR_TIME - drawTime)
                :
                0;

        if (!isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_2));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_2));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void hideThirdFloor(Canvas canvas) {
        if (drawTime > HIDE_THIRD_FLOOR_TIME) {
            return;
        }

        float radius = (float) (getMeasuredWidth() / proportion);
        float radiusNow = drawTime < HIDE_THIRD_FLOOR_TIME ?
                radius / HIDE_THIRD_FLOOR_TIME * (HIDE_THIRD_FLOOR_TIME - drawTime)
                :
                0;

        if (!isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_3));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_3));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void hideForthFloor(Canvas canvas) {
        if (drawTime > HIDE_FORTH_FLOOR_TIME) {
            return;
        }

        float radius = (float) (getMeasuredWidth() / proportion);
        float radiusNow = drawTime < HIDE_FORTH_FLOOR_TIME ?
                radius / HIDE_FORTH_FLOOR_TIME * (HIDE_FORTH_FLOOR_TIME - drawTime)
                :
                0;

        if (!isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_4));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_4));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void hideBackground(Canvas canvas) {
        int alpha = drawTime < HIDE_BACKGROUND_TIME ?
                (int) (255.0 / HIDE_BACKGROUND_TIME * (HIDE_BACKGROUND_TIME - drawTime)) : 0;

        if (!isDay) {
            canvas.drawColor(Color.argb(alpha, 117, 190, 203));
        } else {
            canvas.drawColor(Color.argb(alpha, 26, 27, 34));
        }
    }

    // touch.

    private void touchFirstFloor(Canvas canvas) {
        final int TOUCH_UNIT_TIME = isDay ?
                TOUCH_DAY_UNIT_TIME : TOUCH_NIGHT_UNIT_TIME;

        float scale[] = isDay ?
                new float[] {1.3F, 0.5F} : new float[] {1.2F, 0.9F};

        float radius = (float) (getMeasuredWidth() / proportion);
        float radiusNow;
        if (drawTime <= TOUCH_UNIT_TIME) {
            radiusNow = radius + (scale[0] - 1) * radius / TOUCH_UNIT_TIME * drawTime;
        } else if (drawTime <= 2 * TOUCH_UNIT_TIME) {
            radiusNow = scale[0] * radius - (scale[0] - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - TOUCH_UNIT_TIME);
        } else if (drawTime <= 3 * TOUCH_UNIT_TIME) {
            radiusNow = scale[1] * radius + (1 - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 2 * TOUCH_UNIT_TIME);
        } else {
            radiusNow = radius;
        }

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_1));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_1));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void touchSecondFloor(Canvas canvas) {
        final int TOUCH_UNIT_TIME = isDay ?
                TOUCH_DAY_UNIT_TIME : TOUCH_NIGHT_UNIT_TIME;

        float scale[] = isDay ?
                new float[] {1.2F, 0.7F} : new float[] {1.05F, 0.95F};

        float radius = (float) (2 * getMeasuredWidth() / proportion);
        float radiusNow;
        if (drawTime <= TOUCH_UNIT_TIME) {
            radiusNow = radius;
        } else if (drawTime <= 2 * TOUCH_UNIT_TIME) {
            radiusNow = radius + (scale[0] - 1) * radius / TOUCH_UNIT_TIME * (drawTime - TOUCH_UNIT_TIME);
        } else if (drawTime <= 3 * TOUCH_UNIT_TIME) {
            radiusNow = scale[0] * radius - (scale[0] - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 2 * TOUCH_UNIT_TIME);
        } else if (drawTime <= 4 * TOUCH_UNIT_TIME) {
            radiusNow = scale[1] * radius + (1 - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 3 * TOUCH_UNIT_TIME);
        } else {
            radiusNow = radius;
        }

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_2));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_2));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void touchThirdFloor(Canvas canvas) {
        final int TOUCH_UNIT_TIME = isDay ?
                TOUCH_DAY_UNIT_TIME : TOUCH_NIGHT_UNIT_TIME;

        float scale[] = isDay ?
                new float[] {1.15F, 0.8F} : new float[] {1.02F, 0.98F};

        float radius = (float) (3 * getMeasuredWidth() / proportion);
        float radiusNow;
        if (drawTime <= 2 * TOUCH_UNIT_TIME) {
            radiusNow = radius;
        } else if (drawTime <= 3 * TOUCH_UNIT_TIME) {
            radiusNow = radius + (scale[0] - 1) * radius / TOUCH_UNIT_TIME * (drawTime - 2 * TOUCH_UNIT_TIME);
        } else if (drawTime <= 4 * TOUCH_UNIT_TIME) {
            radiusNow = scale[0] * radius - (scale[0] - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 3 * TOUCH_UNIT_TIME);
        } else if (drawTime <= 5 * TOUCH_UNIT_TIME) {
            radiusNow = scale[1] * radius + (1 - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 4 * TOUCH_UNIT_TIME);
        } else {
            radiusNow = radius;
        }

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_3));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_3));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void touchForthFloor(Canvas canvas) {
        final int TOUCH_UNIT_TIME = isDay ?
                TOUCH_DAY_UNIT_TIME : TOUCH_NIGHT_UNIT_TIME;

        float scale[] = isDay ?
                new float[] {1.05F, 0.85F} : new float[] {1.005F, 0.995F};

        float radius = (float) (4 * getMeasuredWidth() / proportion);
        float radiusNow;
        if (drawTime <= 3 * TOUCH_UNIT_TIME) {
            radiusNow = radius;
        } else if (drawTime <= 4 * TOUCH_UNIT_TIME) {
            radiusNow = radius + (scale[0] - 1) * radius / TOUCH_UNIT_TIME * (drawTime - 3 * TOUCH_UNIT_TIME);
        } else if (drawTime <= 5 * TOUCH_UNIT_TIME) {
            radiusNow = scale[0] * radius - (scale[0] - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 4 * TOUCH_UNIT_TIME);
        } else if (drawTime <= 6 * TOUCH_UNIT_TIME) {
            radiusNow = scale[1] * radius + (1 - scale[1]) * radius / TOUCH_UNIT_TIME * (drawTime - 5 * TOUCH_UNIT_TIME);
        } else {
            radiusNow = radius;
        }

        if (isDay) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_4));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_4));
        }
        canvas.drawCircle(cX, cY, radiusNow, paint);
    }

    private void touchBackground(Canvas canvas) {
        if (isDay) {
            canvas.drawColor(Color.argb(255, 117, 190, 203));
        } else {
            canvas.drawColor(Color.argb(255, 26, 27, 34));
        }
    }
}