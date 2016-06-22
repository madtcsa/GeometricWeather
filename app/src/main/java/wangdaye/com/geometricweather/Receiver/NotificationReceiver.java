package wangdaye.com.geometricweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import wangdaye.com.geometricweather.service.notification.NotificationService;

/**
 * Start the notification service after it wake this receiver up.
 * */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentNotification = new Intent(context, NotificationService.class);
        context.startService(intentNotification);
    }
}