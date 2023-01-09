package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.solobeast.ui.Home.Fragments.HomeFragment;

public class DetailedTaskAct extends AppCompatActivity {
    TextView taskNameTv,taskDescTv,taskDiffTv,taskTimeTv;
    Task task;
    String taskName, taskTime, taskDesc, taskDiff;
    FloatingActionButton submitFormBtn;

    int counter = 0;

    String fromActivity;
    String userChoice;

    DatabaseReference listRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_task);

        init();
        task = new Task();
        determineEditOrAdd(fromActivity);
        changeBackgroundColorAsTaskDiff();


        submitFormBtn.setOnClickListener(view -> {
           counter++;
           if(counter == 1){
               openEditTexts();
               setUpdateIconDrawable();
           }
           else if(counter >= 2){
               if(userChoice.equals("AddTask")){
                   addTaskToFirebase();
               }
               else{
                   updateTaskToFirebase();
               }
//                Intent i = new Intent(this, MainActivity.class);
//                startActivity(i);
           }
        });
    }


    private void init(){
        if(getIntent() != null){
            fromActivity = getIntent().getStringExtra("from_intent");
        }
        taskNameTv = findViewById(R.id.task_name_et);
        taskDescTv = findViewById(R.id.task_desc_et);
        taskDiffTv = findViewById(R.id.task_diff_et);
        taskTimeTv = findViewById(R.id.task_time_et);
        submitFormBtn = findViewById(R.id.fab_edit_add_task);
    }

    private void determineEditOrAdd(String activityName){
        boolean edit = false;
        if(activityName.equals("Edit")) edit = true;
        if(edit){
            userChoice = "EditTask";
            getValuesFromPrevActivityTask();
            insertEditTextsValues();

        }
        else{
            userChoice = "AddTask";

            setAddIconDrawable();
            openEditTexts();
        }
    }

    private void getValuesFromPrevActivityTask() {
        //taskName = getIntent().getStringExtra("selected_task_name");
        task.setName(getIntent().getStringExtra("selected_task_name"));
        //taskTime = getIntent().getStringExtra("selected_task_time");
        task.setTime(getIntent().getStringExtra("selected_task_time"));
        //taskDesc = getIntent().getStringExtra("selected_task_desc");
        task.setDesc(getIntent().getStringExtra("selected_task_desc"));
        //taskDiff = getIntent().getStringExtra("selected_task_diff");
        task.setDiff(getIntent().getStringExtra("selected_task_diff"));
        task.setKey(getIntent().getStringExtra("selected_task_key"));
    }

    private void insertEditTextsValues() {
        taskNameTv.setText(task.getName());
        taskTimeTv.setText(task.getTime());
        taskDescTv.setText(task.getDescription());
        taskDiffTv.setText(task.getDifficulty());
    }

    private void updateTaskToFirebase() {
        listRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        listRef = listRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Tasks"
                );
        listRef = listRef.child(task.getKey());
        Task t = new Task(
                taskNameTv.getEditableText().toString()
                ,taskTimeTv.getEditableText().toString()
                ,taskDescTv.getEditableText().toString()
                ,taskDiffTv.getEditableText().toString()
                ,task.getKey()
        );
        listRef.setValue(t);
    }

    private void addTaskToFirebase() {
        listRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        listRef = listRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Tasks");

        String key = listRef.push().getKey();

        listRef = listRef.child(key);
        Task t = new Task(
                taskNameTv.getEditableText().toString()
                ,taskTimeTv.getEditableText().toString()
                ,taskDescTv.getEditableText().toString()
                ,taskDiffTv.getEditableText().toString()
                ,key
        );
        listRef.setValue(t);
        finish();
    }

    private void setAddIconDrawable() {
        submitFormBtn.setImageResource(R.drawable.ic_baseline_add);
    }
    private void setUpdateIconDrawable() {
        submitFormBtn.setImageResource(R.drawable.ic_baseline_update);
    }

    private void openEditTexts(){
        taskNameTv.setFocusable(true);
        taskNameTv.setFocusableInTouchMode(true);

        taskDescTv.setFocusable(true);
        taskDescTv.setFocusableInTouchMode(true);

        taskTimeTv.setFocusable(true);
        taskTimeTv.setFocusableInTouchMode(true);

        taskDiffTv.setFocusable(true);
        taskDiffTv.setFocusableInTouchMode(true);
    }

    private void changeBackgroundColorAsTaskDiff(){

    }

}