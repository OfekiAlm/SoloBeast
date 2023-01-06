package com.example.solobeast.Extras.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.solobeast.Extras.Services.CountdownThread;

public class TimerService extends Service {
    CountdownThread timerThread;
    String serviceName = "TimerServices";
    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Timer notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").
                    build();

            startForeground(1,notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(timerThread == null){
            timerThread = new CountdownThread();
            timerThread.setTime(intent.getIntExtra("task_time",0));
            timerThread.start();


        }
        Log.i("Value","STARTED");

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Value","FINIHED");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        }
        timerThread.stopTimer();
        timerThread = null;

    }
}