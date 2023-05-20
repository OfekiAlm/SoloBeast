package com.example.solobeast.Extras.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Reschedule the alarm for the next day
        //scheduleAlarm(context);
    }

//    public static void scheduleAlarm(Context context) {
//        // Create an intent to be fired by the AlarmManager
//        Intent intent = new Intent(context, AlarmReceiver.class);
//
//        // Create a PendingIntent to wrap the intent
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        // Get the current time and set the alarm to trigger at the desired time
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        // Set the desired time of day
//        calendar.set(Calendar.HOUR_OF_DAY, 8); // Example: 8 AM
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        // Check if the specified time is already past today, if so, add one day
//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        // Get an instance of AlarmManager
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        // Schedule the alarm
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }
//    }
}
