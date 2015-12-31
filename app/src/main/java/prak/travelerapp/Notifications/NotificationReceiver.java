package prak.travelerapp.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notification_intent = new Intent(context,NotificationService.class);
        context.startService(notification_intent);
    }
}
