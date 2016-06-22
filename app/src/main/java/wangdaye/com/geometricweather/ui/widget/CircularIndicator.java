package wangdaye.com.geometricweather.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.DisplayUtils;

/**
 * Circle Indicator.
 * */

public class CircularIndicator extends View {
    // widget
    private Paint paint;

    // data
    private int totalPage;
    private int pageFrom;
    private int pageTo;
    private float offset;

    private float targetRadius = (float) 5.0;
    private float normalRadius = (float) 3.5;
    private float space = (float) (1.2 * normalRadius);

    public CircularIndicator(Context context) {
        super(context);
        this.initialize();
    }

    public CircularIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public CircularIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    private void initialize() {
        int color = ContextCompat.getColor(getContext(), R.color.lightPrimary_3);

        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(color);

        this.totalPage = 0;
        this.pageFrom = 0;
        this.pageTo = 0;
        this.offset = 0;

        this.targetRadius = DisplayUtils.dpToPx(getContext(), (int) targetRadius);
        this.normalRadius = DisplayUtils.dpToPx(getContext(), (int) normalRadius);
        this.space = DisplayUtils.dpToPx(getContext(), (int) space);
    }

    public void setData(int totalPage, int pageFrom, int pageTo, float offset) {
        this.totalPage = totalPage;
        this.pageFrom = pageFrom;
        this.pageTo = pageTo;
        this.offset = offset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (totalPage == 0) {
            return;
        }

        float totalWidth = totalPage * (2 * normalRadius) + (totalPage - 1) * space;
        for (int i = 0; i < totalPage; i ++) {
            float radius = normalRadius;
            if (i + 1 == pageFrom) {
                radius = (targetRadius - normalRadius) * (1 - offset) + normalRadius;
            } else if (i + 1 == pageTo) {
                radius = (targetRadius - normalRadius) * offset + normalRadius;
            }

            canvas.drawCircle(
                    (float) (getMeasuredWidth() / 2.0 - totalWidth / 2.0 + i * (2 * normalRadius + space) + normalRadius),
                    (float) (getMeasuredHeight() / 2.0),
                    radius,
                    paint);
        }
    }
}
