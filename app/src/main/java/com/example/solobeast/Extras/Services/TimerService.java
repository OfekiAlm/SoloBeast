package com.example.solobeast.Extras.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.solobeast.Extras.Services.CountdownThread;
import com.example.solobeast.ui.Home.MainActivity;

public class TimerService extends Service {
    CountdownThread timerThread;
    String serviceName = "TimerServices";
    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Intent stopServiceIntent = new Intent(this, MainActivity.class);
            stopService(stopServiceIntent);
        }
        timerThread.stopTimer();
        timerThread = null;
    }
}