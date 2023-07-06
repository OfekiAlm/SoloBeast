package com.example.solobeast.Extras.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.solobeast.ui.Home.MainActivity;

/**
 This class represents a service that runs in the background and implements a {@link #timerThread} functionality.
 @see CountdownThread
 */
public class TimerService extends Service {

    /** The thread that runs the countdown timer. */
    CountdownThread timerThread;

    /** The name of the service. */
    String serviceName = "TimerServices";

    /**
     Default constructor.
     */
    public TimerService() {
    }

    /**
     Called when the service is created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     Called when the service is started.

     @param intent The intent that was used to start the service.
     @param flags Additional data about this start request.
     @param startId A unique integer representing this specific request to start.
     @return The return value indicates what semantics the system should use for the service's current started state.
     */
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

    /**
     Called when a client explicitly starts the service by calling Context.startService(Intent).
     @param intent The Intent supplied to Context.startService(Intent), as given.
     @return IBinder interface for communicating with the service, or null if binding is not supported.
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     Called when the service is destroyed.
     */
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