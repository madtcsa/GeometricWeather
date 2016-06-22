package wangdaye.com.geometricweather.receiver.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import wangdaye.com.geometricweather.service.widget.WidgetClockDayWeekService;
import wangdaye.com.geometricweather.utils.WidgetAndNotificationUtils;

/**
 * Widget provider of the widget [clock + day + week].
 * */

public class WidgetClockDayWeekProvider extends AppWidgetProvider {
    // widget
    private PendingIntent pendingIntent = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        if (pendingIntent == null) {
            Intent alarmIntent = new Intent(context, WidgetClockDayWeekService.class);
            pendingIntent = PendingIntent.getService(context,
                    0,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        long hours = 1000 * 60 * 60 * WidgetAndNotificationUtils.getWidgetRefreshHours(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                hours,
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();
        switch (action) {
            case "com.geometricweather.receiver.REFRESH_WIDGET":
                // update
                Intent intentRefresh = new Intent(context, WidgetClockDayWeekService.class);
                context.startService(intentRefresh);
                break;
        }
    }

    @Override
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
