package com.example.solobeast.ui;

import static com.example.solobeast.ui.Home.MainActivity.updateXPtoUserFirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solobeast.Extras.Services.CountdownThread;
import com.example.solobeast.Extras.Services.TimerService;
import com.example.solobeast.R;
import com.example.solobeast.ui.Home.Fragments.ProfileFragment;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * The TaskTimerAct class represents an activity that sets a timer for a task.
 * It extends the AppCompatActivity class.
 * @author Ofek Almog
 */
public class TaskTimerAct extends AppCompatActivity {

    /** The time string for the task. */
    String time;

    /** The TextView for displaying the clock. */
    TextView clock;

    /** The TextView for displaying the task time. */
    TextView timeTV;

    /** The notification counter. */
    static int notificationCounter;

    /** The time difficulty. */
    static String diff;

    /** The time in minutes. */
    static int timeInMinutes;

    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer);
        init();

        // Convert time string to seconds
        int timerSeconds = indicateTimeAsSeconds(time);
        timeInMinutes = timerSeconds / 60;


        // Calculate XP for task
        int xpToAdd = calculatePointsForTask(timeInMinutes,diff);
        sendNotificationToUser("This task will give you", "{ "+ xpToAdd + " } xp" );

        long timerAsMiliseconds = timerSeconds * 1000;
        scheduleAnotification(timerAsMiliseconds);

        // Start the timer
        CountDownTimer timer= new CountDownTimer(timerAsMiliseconds, 1000){
            @Override
            public void onTick(long l) {
                // Convert duration to hours, minutes, and seconds
                long durationMillis = l;
                long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) - TimeUnit.HOURS.toMinutes(hours);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

                // Display duration as a string in the clock TextView

                String durationString = String.format("%02d : %02d : %02d", hours, minutes, seconds);

                clock.setText(durationString);
            }

            @Override
            public void onFinish() {
                // Display XP earned and update Firebase

                int xpToAdd = calculatePointsForTask(timeInMinutes,diff);
                sendNotificationToUser("You did it!", "You're rewarded yourself with more { "+ xpToAdd + " } xp" );
                updateXPtoUserFirebase(xpToAdd,"IncreaseVal");
                finish();
            }
        }.start();

        // Start the TimerService
        Intent taskTimer = new Intent(this, TimerService.class);
        taskTimer.putExtra("task_time",timerSeconds);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(taskTimer);
        }

        // Set up onClickListener for cancel button
        timeTV.setOnClickListener(view -> {
            stopService(taskTimer);
            timer.cancel();
            sendNotificationToUser("You cancelled the task","Waiting for you to come back...");
            finish();
        });

    }

    /**
     * Calculates XP earned for a task.
     * @param taskTimeInMinutes The time taken to complete the task in minutes.
     * @param difficulty The difficulty of the task.
     * @return The XP earned for the task.
     */
    private int calculatePointsForTask(int taskTimeInMinutes, String difficulty) {
        int basePoints = 0;
        switch (difficulty) {
            case "EASY":
                basePoints = 10;
                break;
            case "MEDIUM":
                basePoints = 20;
                break;
            case "HARD":
                basePoints = 30;
                break;
            default:
                break;
        }

        // Calculate points based on time taken to complete task
        double timeInHours = taskTimeInMinutes / 60.0;
        double points = basePoints * timeInHours;
        if ((int)points == 0){
            return 1;
        }
        // Ensure points are capped at 200
        return Math.min(200, (int) points);
    }

    /**
     Schedule notifications for specific time intervals before the end of the timer.
     If the time left is less than a specific interval, a notification will not be scheduled for that interval.
     @param timeAsMilliseconds The remaining time on the timer in milliseconds.
     */
    private void scheduleAnotification(long timeAsMilliseconds){
        if (timeAsMilliseconds >2*4*900000){ //2-hour
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNotificationToUser("KEEP GOING!","2 hours left.");
                    Log.d("Notifications","scheduled a notification for 2 hours mark");
                }
            }, timeAsMilliseconds -2*4*900000);

            // Start the timer
        }
        if (timeAsMilliseconds >4*900000){ //1-hour
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNotificationToUser("KEEP GOING!","1 hour left.");
                    Log.w("Notifications","scheduled a notification for 1 hours mark");

                }
            }, timeAsMilliseconds - 4*900000);

            // Start the timer
        }
        if (timeAsMilliseconds >2*900000){ //30-min
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNotificationToUser("YOU'RE ALMOST DONE!","30 minutes left.");
                    Log.d("Notifications","scheduled a notification for 30 min mark");
                }
            }, timeAsMilliseconds - 2*900000);

            // Start the timer
        }
        if (timeAsMilliseconds > 900000){ //15-min
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNotificationToUser("YOU'RE ALMOST DONE!","15 minutes left.");
                    Log.d("Notifications","scheduled a notification for 15 min mark");

                }
            }, timeAsMilliseconds - 900000);

            // Start the timer
        }
        if (timeAsMilliseconds > 300000){//5-min
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNotificationToUser("YOU'RE ALMOST DONE!","5 minutes left.");
                    Log.d("Notifications","scheduled a notification for 5 minutes mark");
                }
            }, timeAsMilliseconds - 300000);

            // Start the timer
        }

    }

    /**
     * Sends a notification to the user with the specified title and message content.
     *
     * The method creates a notification with the specified title and message content,
     * and sends it to the user. The notification is shown with the app's icon and the current timestamp.
     * When the user taps the notification, the app is launched and the main activity is displayed.
     *
     * @param title the title of the notification.
     * @param msgContent the message content of the notification.
     */
    private void sendNotificationToUser(String title,String msgContent) {
        //phase 1
        int icon = R.drawable.baseline_notification_important;
        long when = System.currentTimeMillis();

        //phase 2
        Intent intent = new Intent(TaskTimerAct.this, TaskTimerAct.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(TaskTimerAct.this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "TimerNotifications";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "human readable title :)",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        //phase 3
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(icon).setWhen(when)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(msgContent).build();

        notificationManager.notify(notificationCounter, notification);
        notificationCounter++;
        // Show the notification to the user

    }

    /**
     Initializes the activity by finding the necessary views and retrieving
     data from the Intent passed to the activity.
     Sets up the initial values for the timer and notification counter.
     */
    private void init(){
        timeTV = findViewById(R.id.timer_tv);
        //airplaneModeTv = findViewById(R.id.airplane_mode_tv);
        clock = findViewById(R.id.text_view_timer);
        notificationCounter = 0;
        if(getIntent() != null){
            time= getIntent().getStringExtra("selected_task_time");
            diff= getIntent().getStringExtra("selected_task_diff");
            Log.d("Value","the time is: \t"+time);
        }
    }

    /**
     Converts a string in the format "hh:mm" to the total number of seconds represented by the time.
     @param time A string representing a time in the format "hh:mm".
     @return An integer representing the total number of seconds represented by the input time.
     */
    public int indicateTimeAsSeconds(String time){
        int timerTime = 0;
        List<String> arrayList = Arrays.asList(time.split(":"));
        String hours = arrayList.get(0);
        String minutes = arrayList.get(1);
        timerTime = Integer.parseInt(minutes) * 60 + Integer.parseInt(hours) * 3600;
        return timerTime;
    }

}