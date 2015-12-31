package prak.travelerapp.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import prak.travelerapp.MainActivity;
import prak.travelerapp.R;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.Tupel;

public class NotificationService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Trip active_trip = getActiveTrip();

        if (active_trip != null) {
            int remainingItems = 0;
            for (Tupel t : active_trip.getTripItems().getItems()) {
                if (t.getY() == 0) {
                    remainingItems++;
                }
            }
            Bitmap icon = BitmapFactory.decodeResource(getBaseContext().getResources(),
                    R.mipmap.ic_launcher);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setAutoCancel(true);
            builder.setContentTitle("TRAVeL");
            builder.setContentText("Es fehlen noch " + remainingItems + " Dinge");

            //TODO Better icon than checkmark ;)
            builder.setSmallIcon(R.mipmap.ic_check);
            builder.setLargeIcon(icon);

            Intent resultIntent = new Intent(this, MainActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                builder.setContentIntent(resultPendingIntent);
            }

            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, notification);
        }
        return START_NOT_STICKY;
    }

    public Trip getActiveTrip(){
        TripDBAdapter tripDBAdapter = new TripDBAdapter(this);
        tripDBAdapter.open();
        Trip active_trip = tripDBAdapter.getActiveTrip();
        tripDBAdapter.close();
        return active_trip;
    }

}
