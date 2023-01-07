package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.solobeast.R;

public class DetailedTaskAct extends AppCompatActivity {
    TextView taskNameTv,taskDescTv,taskDiffTv,taskTimeTv;
    String taskName,taskDesc,taskDiff,taskTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_task);

        init();
        changeBackgroundColorAsTaskDiff();

    }
    private void init(){
        if(getIntent() != null){
            taskName = getIntent().getStringExtra("selected_task_name");
            taskTime = getIntent().getStringExtra("selected_task_time");
            taskDesc = getIntent().getStringExtra("selected_task_desc");
            taskDiff = getIntent().getStringExtra("selected_task_diff");
        }

        taskNameTv = findViewById(R.id.task_name_et);
//        taskDescTv = findViewById(R.id.);
//        taskDiffTv = findViewById(R.id.);
//        taskTimeTv = findViewById(R.id.);
    }
    private void changeBackgroundColorAsTaskDiff(){

    }
}