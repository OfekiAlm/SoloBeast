package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.solobeast.Extras.GuiderDialog;
import com.example.solobeast.Extras.PickerDialog;
import com.example.solobeast.Extras.onPickerSelectedListener;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

/**
 * This activity class is responsible for displaying the details of a task, including its name,
 * description, difficulty, and duration. It also provides functionality to add a new task or
 * update an existing one.
 * @author Ofek Almog
 */
public class DetailedTaskAct extends AppCompatActivity {

    /**
     * The TextInputEditTexts for the task name, description, difficulty and duration.
     */
    TextInputEditText taskNameTv,taskDescTv,taskDiffTv,taskTimeTv;

    /**
     * The task being edited or added.
     */
    Task task;

    /**
     * The FloatingActionButton used to submit the form.
     */
    FloatingActionButton submitFormBtn;

    /**
     * A counter used to determine if the form has been submitted before. a UI updating solution.
     */
    int counter = 0;

    /**
     * The name of the activity that started this one.
     */
    String fromActivity;

    /**
     * Indicates whether the user is adding a new task or editing an existing one.
     */
    String userChoice;

    /**
     * A reference to the Firebase Realtime Database.
     */
    DatabaseReference listRef;

    /**
     * Called when the activity is starting. Sets up the views and initializes the task.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_task);

        init();
        task = new Task();
        determineEditOrAdd(fromActivity);
        GuiderDialog gd = new GuiderDialog(this,"DetailedTaskAct", "This is a explanation for you 😀");
        gd.startDialog();
        taskTimeTv.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus)
                popTimePicker(view);

        });

        taskDiffTv.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus)
                popDiffSelection(view);

        });
        submitFormBtn.setOnClickListener(view -> {
            ++counter;
           if(counter == 1){
               openEditTexts();
               setUpdateIconDrawable();
           }
           else if(counter >= 2){
               if(userChoice.equals("AddTask")){
                   addTaskToFirebase();
                   finish();
               }
               else{
                   updateTaskToFirebase();
                   finish();
               }
           }
        });
    }

    /**
     * Opens the time picker dialog.
     * @param view The view that triggered the method.
     */
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> taskTimeTv.setText(String.format(Locale.getDefault(), "%02d:%02d",selectedHour, selectedMinute));
        TimePickerDialog.OnCancelListener onCancelListener = (timePicker)  -> taskDescTv.requestFocus();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, 1,0, true);
        timePickerDialog.setOnCancelListener(onCancelListener);
        timePickerDialog.setTitle("Select Task Duration (HH:MM)");
        timePickerDialog.show();
    }

    /**
     * Opens the difficulty picker dialog.
     * @param view The view that triggered the method.
     */
    public void popDiffSelection(View view){
        // Instantiate the PickerDialog
        PickerDialog pickerDialog = new PickerDialog(this, "diff");

        pickerDialog.setOnNameSelectedListener(new onPickerSelectedListener() {
            @Override
            public void onNameSelected(String name) {
                // Display the selected nam
                taskDiffTv.setText(name);
                }

            @Override
            public void onNumberSelected(int num) {
                // This method is not used in this example
            }
        });

        pickerDialog.show();
    }

    /**
     Initializes the activity by setting up views and retrieving data from the incoming intent.
     */
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

    /**
     Determines whether the user is editing or adding a task based on the name of the activity.
     If the activity name is "Edit", it sets edit to true and calls the {@link #getValuesFromPrevActivityTask()}
     method to get the previous task values, and {@link #insertEditTextsValues()} method to insert the values
     into the edit texts.
     If the activity name is not "Edit", it sets edit to false and calls the {@link #setAddIconDrawable()}
     method to set the add icon drawable to the submit button, {@link #openEditTexts()} method to open the edit texts,
     and increments the counter by one.
     @param activityName the name of the activity, either "Edit" or "Add"
     */
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
            ++counter;
        }
    }

    /**
     This method retrieves the values of the selected task from the previous activity
     and sets them to the corresponding fields in the current activity's task object.
     */
    private void getValuesFromPrevActivityTask() {
        task.setName(getIntent().getStringExtra("selected_task_name"));
        task.setTime(getIntent().getStringExtra("selected_task_time"));
        task.setDesc(getIntent().getStringExtra("selected_task_desc"));
        task.setDiff(getIntent().getStringExtra("selected_task_diff"));
        task.setKey(getIntent().getStringExtra("selected_task_key"));
    }

    /**
     This method sets the task data to the corresponding fields in the current activity's task object.
     This method should be called after the {@link #insertEditTextsValues()} method.
     */
    private void insertEditTextsValues() {
        taskNameTv.setText(task.getName());
        taskTimeTv.setText(task.getTime());
        taskDescTv.setText(task.getDescription());
        taskDiffTv.setText(task.getDifficulty());
    }

    /**
     * This method updates the selected task in the Firebase Realtime Database.
     * It creates a reference to the database and updates the corresponding task node
     * with the new values entered by the user in the EditText fields.
     */
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

    /**
     Adds a new task to Firebase by creating a new reference in the Firebase database
     with the task details provided by the user. The newly created task is associated
     with the currently authenticated user.
     */
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

    /**
     Sets the add icon to the submit button in the current activity.
     */
    private void setAddIconDrawable() {
        submitFormBtn.setImageResource(R.drawable.ic_baseline_add);
    }

    /**
     Sets the update icon to the submit button in the current activity.
     */
    private void setUpdateIconDrawable() {
        submitFormBtn.setImageResource(R.drawable.ic_baseline_update);
    }

    /**
     Opens the edit texts in the current activity, allowing the user to modify the values of the fields.
     */
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
}