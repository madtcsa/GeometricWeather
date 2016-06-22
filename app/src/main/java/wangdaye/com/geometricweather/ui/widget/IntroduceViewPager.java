package wangdaye.com.geometricweather.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Introduce view pager.
 * */

public class IntroduceViewPager extends ViewPager {

    /** <br> life cycle. */

    public IntroduceViewPager(Context context) {
        super(context);
    }

    public IntroduceViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** <br> measure. */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                getMeasuredWidth(),
                (int) (getMeasuredWidth() / 8.0 * 5.0));
    }
}
