package wangdaye.com.geometricweather.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import wangdaye.com.geometricweather.R;

/**
 * Display utils.
 * */

public class DisplayUtils {

    public static float dpToPx(Context context, int dp) {
        int dpi = context.getResources().getDisplayMetrics().densityDpi;
        return (float) (dp * (dpi / 160.0));
    }

    public static int getStatusBarHeight(Resources r) {
        int resourceId = r.getIdentifier("status_bar_height", "dimen","android");
        return r.getDimensionPixelSize(resourceId);
    }

    public static void setStatusBarTranslate(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setNavigationBarColor(Context context, Window window, boolean isDay) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean navigationBarColorSwitch = sharedPreferences.getBoolean(
                context.getString(R.string.key_navigation_bar_color_switch),
                false);
        if(navigationBarColorSwitch && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isDay) {
                window.setNavigationBarColor(ContextCompat.getColor(context, R.color.lightPrimary_3));
            } else {
                window.setNavigationBarColor(ContextCompat.getColor(context, R.color.darkPrimary_5));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setNavigationBarColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
    }

    public static void setWindowTopColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isDay = TimeUtils.getInstance(activity).isDay;

            ActivityManager.TaskDescription taskDescription;
            Bitmap topIcon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
            taskDescription = new ActivityManager.TaskDescription(activity.getString(R.string.app_name),
                    topIcon,
                    ContextCompat.getColor(activity, isDay ? R.color.lightPrimary_5 : R.color.darkPrimary_5));
            activity.setTaskDescription(taskDescription);
            topIcon.recycle();
        }
    }
}
