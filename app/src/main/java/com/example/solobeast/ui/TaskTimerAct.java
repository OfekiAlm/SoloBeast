package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.solobeast.Extras.Services.CountdownThread;
import com.example.solobeast.Extras.Services.TimerService;
import com.example.solobeast.R;

import java.util.Arrays;
import java.util.List;

public class TaskTimerAct extends AppCompatActivity {
    String time;
    TextView timeTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer);
        if(getIntent() != null){
            time= getIntent().getStringExtra("selected_task_time");
            Log.d("Value","the time is: \t"+time);
        }

        int timerSeconds = indicateTimeAsSeconds(time);
        Intent taskTimer = new Intent(this, TimerService.class);
        taskTimer.putExtra("task_time",timerSeconds);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(taskTimer);
        }
        init();
        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(taskTimer);
            }
        });

    }

    private void init(){
        timeTV = findViewById(R.id.timer_tv);
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