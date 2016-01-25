package prak.travelerapp.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

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

        NotificationTask task = new NotificationTask(getBaseContext());
        task.execute();
        return START_NOT_STICKY;
    }

    private class NotificationTask extends AsyncTask<Void, Void, Integer> {
        private Context context;
        public NotificationTask(Context context){
            this.context = context;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            //Log.d("Started Task", "Started");
            // get Active Trip
            TripDBAdapter tripDBAdapter = new TripDBAdapter(context);
            tripDBAdapter.open();
            Trip active_trip = tripDBAdapter.getActiveTrip();
            tripDBAdapter.close();

            int remainingItems = 0;
            if (active_trip != null) {
                for (Tupel t : active_trip.getTripItems().getItems()) {
                    if (t.getY() == 0) {
                        remainingItems++;
                    }
                }
            }

            // Put in sharedPref that Push Notification will be fired
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(String.valueOf(R.bool.day_before_notification), true);
            editor.apply();

            return remainingItems;
        }


        @Override
        protected void onPostExecute(Integer remainingItems) {
            if (remainingItems>0) {
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setAutoCancel(true);
                builder.setContentTitle("TRAVeL Packliste");
                builder.setContentText("Es fehlen noch " + remainingItems + " Dinge");

                builder.setSmallIcon(R.mipmap.ic_notification);
                builder.setLargeIcon(icon);

                Intent resultIntent = new Intent(context, MainActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
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
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notification.defaults|= Notification.DEFAULT_SOUND;
                notification.defaults|= Notification.DEFAULT_LIGHTS;
                notification.defaults|= Notification.DEFAULT_VIBRATE;
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                manager.notify(0, notification);
            }
            //Log.d("Pushed Notification", "Successfully");
            stopSelf();
        }
    }
}