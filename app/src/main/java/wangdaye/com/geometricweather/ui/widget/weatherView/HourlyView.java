package wangdaye.com.geometricweather.ui.widget.weatherView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.DisplayUtils;

/**
 * Show the trend of the hourly data.
 * */

public class HourlyView extends View {
    // widget
    private Paint paint;

    // data
    private int[] temps;
    private float[] pops;

    private final String[] hours = new String[] {"01", "04", "07", "10", "13", "16", "19", "22"};

    private float MARGIN_TOP = 60;
    private float MARGIN_BOTTOM = 80;
    private float WEATHER_TEXT_SIZE = 14;
    private float WEATHER_TEXT_WIDTH = 0.7F;
    private float TREND_LINE_SIZE = 2;
    private float TIME_TEXT_SIZE = 10;
    private float TIME_TEXT_WIDTH = 0.25F;
    private float CHART_LINE_SIZE = 1;
    private float MARGIN_TEXT = 2;

    /** <br> life cycle. */

    public HourlyView(Context context) {
        super(context);
        this.initialize();
    }

    public HourlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public HourlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HourlyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    private void initialize() {
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        MARGIN_TOP = DisplayUtils.dpToPx(getContext(), (int) MARGIN_TOP);
        MARGIN_BOTTOM = DisplayUtils.dpToPx(getContext(), (int) MARGIN_BOTTOM);
        WEATHER_TEXT_SIZE = DisplayUtils.dpToPx(getContext(), (int) WEATHER_TEXT_SIZE);
        WEATHER_TEXT_WIDTH = DisplayUtils.dpToPx(getContext(), (int) WEATHER_TEXT_WIDTH);
        TREND_LINE_SIZE = DisplayUtils.dpToPx(getContext(), (int) TREND_LINE_SIZE);
        TIME_TEXT_SIZE = DisplayUtils.dpToPx(getContext(), (int) TIME_TEXT_SIZE);
        TIME_TEXT_WIDTH = DisplayUtils.dpToPx(getContext(), (int) TIME_TEXT_WIDTH);
        CHART_LINE_SIZE = DisplayUtils.dpToPx(getContext(), (int) CHART_LINE_SIZE);
        MARGIN_TEXT = DisplayUtils.dpToPx(getContext(), (int) MARGIN_TEXT);
    }

    /** <br> data. */

    public void setData(int[] temp, float[] pop) {
        if (temp == null || pop == null || temp.length != pop.length || temp.length == 0) {
            return;
        }
        this.temps = temp;
        this.pops = pop;
        invalidate();
    }

    /** <br> draw. */

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (temps == null || pops == null) {
            return;
        }

        float drawSpaceWidth = getMeasuredWidth();
        float drawSpaceHeight = getMeasuredHeight() - MARGIN_BOTTOM - MARGIN_TOP;

        int highestTemp = this.temps[0];
        int lowestTemp = this.temps[0];
        for (int t : temps) {
            if (t > highestTemp) {
                highestTemp = t;
            }
            if (t < lowestTemp) {
                lowestTemp = t;
            }
        }

        if (highestTemp == lowestTemp) {
            highestTemp += 7;
            lowestTemp -= 7;
        }

        int[] timeLineCoordinates = new int[temps.length];
        for (int i = 0; i < timeLineCoordinates.length; i ++) {
            timeLineCoordinates[i] = (int) (drawSpaceWidth / (timeLineCoordinates.length * 2.0) * (2 * i + 1));
        }

        Point[] tempPoints = new Point[temps.length];
        for (int i = 0; i < tempPoints.length; i ++) {
            tempPoints[i] = new Point(
                    timeLineCoordinates[i],
                    (int) (drawSpaceHeight / (highestTemp - lowestTemp) * (highestTemp - temps[i]) + MARGIN_TOP));
        }

        Point[] popPoints = new Point[pops.length];
        for (int i = 0; i < popPoints.length; i ++) {
            popPoints[i] = new Point(
                    timeLineCoordinates[i],
                    (int) (drawSpaceHeight / 100.0 * (100 - pops[i]) + MARGIN_TOP));
        }

        this.drawTimeLine(canvas, timeLineCoordinates);
        this.drawPopLine(canvas, popPoints);
        this.drawTemp(canvas, tempPoints);
    }

    private void drawTimeLine(Canvas canvas, int[] coordinates) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(TIME_TEXT_SIZE);

        for (int i = 0; i < coordinates.length; i ++) {
            paint.setStrokeWidth(CHART_LINE_SIZE);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.chart_line_time));
            canvas.drawLine(
                    coordinates[i], MARGIN_TOP,
                    coordinates[i], getMeasuredHeight() - MARGIN_BOTTOM,
                    paint);

            paint.setStrokeWidth(TIME_TEXT_WIDTH);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.chart_number));
            paint.setAlpha(100);
            canvas.drawText(
                    hours[hours.length - (coordinates.length - i)],
                    coordinates[i] - MARGIN_TEXT,
                    getMeasuredHeight() - MARGIN_BOTTOM - paint.getFontMetrics().bottom,
                    paint);
            paint.setAlpha(255);
        }
    }

    private void drawPopLine(Canvas canvas, Point[] points) {
        Path path = new Path();

        if (points.length == 1) {
            Shader linearGradientLeft = new LinearGradient(
                    points[0].x, points[0].y,
                    (float) (getMeasuredWidth() / 4.0), points[0].y,
                    Color.argb(225, 75, 80, 115), Color.argb(0, 75, 80, 115), Shader.TileMode.CLAMP);
            paint.setShader(linearGradientLeft);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(TREND_LINE_SIZE);
            paint.setAlpha(100);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_1));
            paint.setShadowLayer(2, 0, 2, Color.argb(200, 176, 176, 176));

            path.moveTo(points[0].x, points[0].y);
            path.lineTo((float) (getMeasuredWidth() / 4.0), points[0].y);
            canvas.drawPath(path, paint);
            path.reset();
            paint.setShader(null);
            paint.setAlpha(255);
        } else {
            float corner = (float) (getMeasuredWidth() / points.length / 2.0);
            paint.setPathEffect(new CornerPathEffect(corner));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(TREND_LINE_SIZE);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_1));
            paint.setShadowLayer(2, 0, 2, Color.argb(200, 176, 176, 176));
            path.moveTo(points[0].x, points[0].y);
            for (int i = 1; i < points.length; i ++) {
                path.lineTo(points[i].x, points[i].y);
            }
            canvas.drawPath(path, paint);
            path.reset();
            paint.setPathEffect(null);
        }

        paint.setColor(ContextCompat.getColor(getContext(), R.color.chart_number));
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(WEATHER_TEXT_SIZE);
        paint.setStrokeWidth(WEATHER_TEXT_WIDTH);
        for (int i = 0; i < points.length; i ++) {
            canvas.drawText(
                    Float.toString(pops[i]) + "%",
                    points[i].x - MARGIN_TEXT,
                    points[i].y - paint.getFontMetrics().top + MARGIN_TEXT,
                    paint);
        }
        paint.setShadowLayer(0, 0, 0, ContextCompat.getColor(getContext(), android.R.color.transparent));
    }

    private void drawTemp(Canvas canvas, Point[] points) {
        Path path = new Path();

        if (points.length == 1) {
            Shader linearGradientLeft = new LinearGradient(
                    points[0].x, points[0].y,
                    (float) (getMeasuredWidth() / 4.0), points[0].y,
                    ContextCompat.getColor(getContext(), R.color.lightPrimary_3), Color.argb(0, 150, 214, 219), Shader.TileMode.CLAMP);
            paint.setShader(linearGradientLeft);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(TREND_LINE_SIZE);
            paint.setAlpha(100);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.darkPrimary_1));
            paint.setShadowLayer(2, 0, 2, Color.argb(200, 176, 176, 176));

            path.moveTo(points[0].x, points[0].y);
            path.lineTo((float) (getMeasuredWidth() / 4.0), points[0].y);
            canvas.drawPath(path, paint);
            path.reset();
            paint.setShader(null);
            paint.setAlpha(255);
        } else {
            float corner = (float) ((getMeasuredHeight() - MARGIN_BOTTOM - MARGIN_TOP) / 3.0 * 2);

            Shader linearGradient = new LinearGradient(
                    0, MARGIN_TOP,
                    0, (float) (getMeasuredHeight() - MARGIN_BOTTOM - corner / 3.0 * 2.0),
                    Color.argb(50, 176, 176, 176), Color.argb(0, 176, 176, 176), Shader.TileMode.CLAMP);
            paint.setShader(linearGradient);
            paint.setStyle(Paint.Style.FILL);

            path.moveTo(points[0].x, getMeasuredHeight() - MARGIN_BOTTOM);
            for (Point p : points) {
                path.lineTo(p.x, p.y);
            }
            path.lineTo(points[points.length - 1].x, getMeasuredHeight() - MARGIN_BOTTOM);
            path.close();
            canvas.drawPath(path, paint);
            paint.setShader(null);
            path.reset();

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(TREND_LINE_SIZE);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.lightPrimary_3));
            paint.setShadowLayer(2, 0, 2, Color.argb(200, 176, 176, 176));
            path.moveTo(points[0].x, points[0].y);
            for (int i = 1; i < points.length; i ++) {
                path.lineTo(points[i].x, points[i].y);
            }
            canvas.drawPath(path, paint);
            path.reset();
        }

        paint.setColor(ContextCompat.getColor(getContext(), R.color.chart_number));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(WEATHER_TEXT_SIZE);
        paint.setStrokeWidth(WEATHER_TEXT_WIDTH);
        for (int i = 0; i < points.length; i ++) {
            canvas.drawText(
                    Float.toString(temps[i]) + "°",
                    points[i].x + MARGIN_TEXT,
                    points[i].y - paint.getFontMetrics().bottom - MARGIN_TEXT,
                    paint);
        }
        paint.setShadowLayer(0, 0, 0, ContextCompat.getColor(getContext(), android.R.color.transparent));
    }
}
