package com.github.jotask.kimo.util.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import com.github.jotask.kimo.Kimos;
import com.github.jotask.kimo.R;

/**
 * Alarm
 *
 * @author Jose Vives Iznardo
 * @since 28/04/2017
 */
public class Alarm extends Service{

    @Override
    public void onCreate() {
        notification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        notification();
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notification();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        notification();
        return super.onUnbind(intent);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void notification() {
        final Message msg = Message.getRandom();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(0, new Notification.Builder(this)
                .setContentTitle(msg.title)
                .setContentText(msg.text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, Kimos.class), 0))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true).build());
    }

}
