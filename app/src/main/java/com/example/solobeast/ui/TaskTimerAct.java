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

public class TaskTimerAct extends AppCompatActivity {
    String time;
    TextView clock;
    TextView timeTV,airplaneModeTv;
    static int notificationCounter;
    static String diff;
    static int timeInMinutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer);
        init();

        int timerSeconds = indicateTimeAsSeconds(time);
        timeInMinutes = timerSeconds / 60;



        int xpToAdd = calculatePointsForTask(timeInMinutes,diff);
        sendNotificationToUser("This task will give you", "{ "+ xpToAdd + " } xp" );

        long timerAsMiliseconds = timerSeconds * 1000;
        scheduleAnotification(timerAsMiliseconds);
        CountDownTimer timer= new CountDownTimer(timerAsMiliseconds, 1000){
            @Override
            public void onTick(long l) {
                long durationMillis = l; // replace with your duration in milliseconds

                long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) - TimeUnit.HOURS.toMinutes(hours);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

                String durationString = String.format("%02d : %02d : %02d", hours, minutes, seconds);

                clock.setText(durationString);
            }

            @Override
            public void onFinish() {
                //Toast.makeText(TaskTimerAct.this,"lol this is ended quickly",Toast.LENGTH_LONG).show();
                int xpToAdd = calculatePointsForTask(timeInMinutes,diff);
                sendNotificationToUser("You did it!", "You're rewarded yourself with more { "+ xpToAdd + " } xp" );
                updateXPtoUserFirebase(xpToAdd,"IncreaseVal");
                finish();
            }
        }.start();
        Intent taskTimer = new Intent(this, TimerService.class);
        taskTimer.putExtra("task_time",timerSeconds);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(taskTimer);
        }
        airplaneModeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //applyAirplaneMode();
            }
        });
        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(taskTimer);
                timer.cancel();
                sendNotificationToUser("You cancelled the task","Waiting for you to come back...");
                finish();
            }
        });


    }

//    private void applyAirplaneMode() {
//        // Get the system service for Airplane Mode
//        Settings.Global.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
//
//        // Broadcast the intent to inform the system that airplane mode has changed
//        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        intent.putExtra("state", true);
//        sendBroadcast(intent);
//    }

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



    private void scheduleAnotification(long timerAsMiliseconds){
        if (timerAsMiliseconds >2*4*900000){ //2-hour
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // This code will be executed when the timer reaches 5 minutes left
                    sendNotificationToUser("KEEP GOING!","2 hours left.");
                    Log.d("Notifications","scheduled a notification for 2 hours mark");
                }
            }, timerAsMiliseconds -2*4*900000); // schedule the task to run when there are 5 minutes left (300000 milliseconds)

            // Start the timer
        }
        if (timerAsMiliseconds >4*900000){ //1-hour
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // This code will be executed when the timer reaches 5 minutes left
                    sendNotificationToUser("KEEP GOING!","1 hour left.");
                    Log.w("Notifications","scheduled a notification for 1 hours mark");

                }
            }, timerAsMiliseconds - 4*900000); // schedule the task to run when there are 5 minutes left (300000 milliseconds)

            // Start the timer
        }
        if (timerAsMiliseconds >2*900000){ //30-min
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // This code will be executed when the timer reaches 5 minutes left
                    sendNotificationToUser("YOU'RE ALMOST DONE!","5 minutes left.");
                    Log.d("Notifications","scheduled a notification for 30 min mark");

                }
            }, timerAsMiliseconds - 2*900000); // schedule the task to run when there are 5 minutes left (300000 milliseconds)

            // Start the timer
        }
        if (timerAsMiliseconds > 900000){ //15-min
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // This code will be executed when the timer reaches 5 minutes left
                    sendNotificationToUser("YOU'RE ALMOST DONE!","15 minutes left.");
                    Log.d("Notifications","scheduled a notification for 15 min mark");

                }
            }, timerAsMiliseconds - 900000); // schedule the task to run when there are 5 minutes left (300000 milliseconds)

            // Start the timer
        }
        if (timerAsMiliseconds > 300000){//5-min
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // This code will be executed when the timer reaches 5 minutes left
                    sendNotificationToUser("YOU'RE ALMOST DONE!","5 minutes left.");
                    Log.d("Notifications","scheduled a notification for 5 minutes mark");
                }
            }, timerAsMiliseconds - 300000); // schedule the task to run when there are 5 minutes left (300000 milliseconds)

            // Start the timer
        }

    }
    /**
     * Sends a notification to the user with the specified title and message content.
     *
     * The method creates a notification with the specified title and message content, and sends it to the user. The notification is shown with the app's icon and the current timestamp. When the user taps the notification, the app is launched and the main activity is displayed.
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

    private void init(){
        timeTV = findViewById(R.id.timer_tv);
        airplaneModeTv = findViewById(R.id.airplane_mode_tv);
        clock = findViewById(R.id.text_view_timer);
        notificationCounter = 0;
        if(getIntent() != null){
            time= getIntent().getStringExtra("selected_task_time");
            diff= getIntent().getStringExtra("selected_task_diff");
            Log.d("Value","the time is: \t"+time);
        }
    }
    public int indicateTimeAsSeconds(String time){
        int timerTime = 0;
        List<String> arrayList = Arrays.asList(time.split(":"));
        String hours = arrayList.get(0);
        String minutes = arrayList.get(1);
        timerTime = Integer.parseInt(minutes) * 60 + Integer.parseInt(hours) * 3600;
        return timerTime;
    }

}