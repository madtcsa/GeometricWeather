package wangdaye.com.geometricweather.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Introduce image view.
 * */

public class IntroduceImageView extends ImageView {

    /** <br> life cycle. */

    public IntroduceImageView(Context context) {
        super(context);
    }

    public IntroduceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntroduceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IntroduceImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                getMeasuredWidth(),
                (int) (getMeasuredWidth() / 8.0 * 5.0));
    }
}
