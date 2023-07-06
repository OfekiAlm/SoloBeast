package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.solobeast.Extras.PickerDialog;
import com.example.solobeast.Extras.onPickerSelectedListener;
import com.example.solobeast.Objects.Reward;
import com.example.solobeast.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This activity class is responsible for displaying the details of a reward, including its name,
 * description and experience points. It also provides functionality to add a new reward or
 * update an existing one.
 * @author Ofek Almog
 */
public class DetailedRewardAct extends AppCompatActivity {

    /**
     * The TextInputEditTexts for the reward name, description, difficulty and experience points.
     */
    TextView rewardNameTv, rewardDescTv,rewardXpTv;

    /**
     * The reward being edited or added.
     */
    Reward reward;

    /**
     * The FloatingActionButton used to submit the form.
     */
    FloatingActionButton submitFloatingFormBtn;

    /**
     * A counter used to determine if the form has been submitted before. a UI updating solution.
     */
    int counter = 0;

    /**
     * The name of the activity that started this one.
     */
    String fromActivity;

    /**
     * Indicates whether the user is adding a new reward or editing an existing one.
     */
    String userChoice;

    /**
     * A reference to the Firebase Realtime Database.
     */
    DatabaseReference listRef;

    /**
     * Called when the activity is starting. Sets up the views and initializes the reward.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_reward);

        init();
        reward = new Reward();
        determineEditOrAdd(fromActivity);


        rewardXpTv.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus)
                popXpSelection(view);


        });
        submitFloatingFormBtn.setOnClickListener(view -> {
            counter++;
            if(counter == 1){
                openEditTexts();
                setUpdateIconDrawable();
            }
            else if(counter >= 2){
                if(userChoice.equals("AddReward")){
                    addRewardToFirebase();
                    finish();
                }
                else{
                    updateRewardToFirebase();
                    finish();
                }
            }
        });
    }

    /**
     Displays a number picker dialog allowing the user to select an experience point value for the reward.
     The selected value is displayed in the experience point TextInputEditText.
     @param view The view that called this method, typically the experience point TextInputEditText.
     */
    private void popXpSelection(View view){
        // Instantiate the PickerDialog
        PickerDialog pickerDialog = new PickerDialog(this, "numbers");
        DialogInterface.OnCancelListener onCancelListener = (picker) -> rewardDescTv.requestFocus();
        pickerDialog.setOnCancelListener(onCancelListener);

        // Set a listener to be notified when a name is selected
        pickerDialog.setOnNameSelectedListener(new onPickerSelectedListener() {
            @Override
            public void onNameSelected(String name) {
                // Display the selected name

            }

            @Override
            public void onNumberSelected(int num) {
                rewardXpTv.setText(String.valueOf(num));
            }
        });

        // Show the dialog
        pickerDialog.show();
    }

    /**
     Initializes the activity by setting up views and retrieving data from the incoming intent.
     */
    private void init(){
        if(getIntent() != null){
            fromActivity = getIntent().getStringExtra("from_intent");
        }
        //put values of edittexts in variables
        rewardNameTv = findViewById(R.id.reward_name_et);
        rewardDescTv = findViewById(R.id.reward_desc_et);
        rewardXpTv = findViewById(R.id.reward_et_xp1);
        submitFloatingFormBtn = findViewById(R.id.fab_edit_add_reward_1);
    }

    /**
     Determines whether the user is editing or adding a reward based on the name of the activity.
     If the activity name is "Edit", it sets edit to true and calls the {@link #getValuesFromPrevActivityReward()}
     method to get the previous reward values, and {@link #insertEditTextsValues()} method to insert the values
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
            userChoice = "EditReward";
            getValuesFromPrevActivityReward();
            insertEditTextsValues();

        }
        else{
            userChoice = "AddReward";
            setAddIconDrawable();
            openEditTexts();
        }
    }

    /**
     This method retrieves the values of the selected reward from the previous activity
     and sets them to the corresponding fields in the current activity's task object.
     */
    private void getValuesFromPrevActivityReward() {
        reward.setRewardName(getIntent().getStringExtra("selected_reward_name"));
        reward.setDescription(getIntent().getStringExtra("selected_reward_desc"));
        reward.setXP(getIntent().getIntExtra("selected_reward_xp",0));
        reward.setKey(getIntent().getStringExtra("selected_reward_key"));
    }


    /**
     Converts a string to an integer. If the input string is empty or null, returns the integer 1.
     @param str the string to be converted to an integer.
     @return the integer representation of the input string, or 1 if the input string is empty or null.
     */
    public int strToInt(String str){
        if(!str.equals(""))
            return Integer.parseInt(str);
        else {
            return 1;
        }
    }

    /**
     This method sets the reward data to the corresponding fields in the current activity's reward object.
     This method should be called after the {@link #insertEditTextsValues()} method.
     */
    private void insertEditTextsValues() {
        rewardNameTv.setText(reward.getRewardName());
        rewardDescTv.setText(reward.getDescription());
        rewardXpTv.setText(String.valueOf(reward.getXP()));
    }

    /**
     * This method updates the selected task in the Firebase Realtime Database.
     * It creates a reference to the database and updates the corresponding reward node
     * with the new values entered by the user in the EditText fields.
     */
    private void updateRewardToFirebase() {
        listRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        listRef = listRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Rewards"
        );
        System.out.println("---- the key is --- "+reward.getKey());
        listRef = listRef.child(reward.getKey());
        Reward t = new Reward(
                rewardNameTv.getEditableText().toString()
                ,rewardDescTv.getEditableText().toString()
                ,Integer.parseInt(rewardXpTv.getEditableText().toString())
                ,reward.getKey()
        );
        listRef.setValue(t);
        finish();
    }

    /**
     Adds a new reward to Firebase by creating a new reference in the Firebase database
     with the reward details provided by the user. The newly created reward is associated
     with the currently authenticated user.
     */
    private void addRewardToFirebase() {
        listRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        listRef = listRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Rewards");

        String key = listRef.push().getKey();

        listRef = listRef.child(key);
        Reward t = new Reward(
                rewardNameTv.getEditableText().toString()
                , rewardDescTv.getEditableText().toString()
                ,strToInt(rewardXpTv.getEditableText().toString())
                ,key
        );
        listRef.setValue(t);
        finish();
    }

    /**
     Sets the add icon to the add button in the current activity.
     */
    private void setAddIconDrawable() {
        if(submitFloatingFormBtn != null) {
            submitFloatingFormBtn.setImageResource(R.drawable.ic_baseline_add);
        }
    }

    /**
     Sets the update icon to the update button in the current activity.
     */
    private void setUpdateIconDrawable() {
        if(submitFloatingFormBtn != null) {
            submitFloatingFormBtn.setImageResource(R.drawable.ic_baseline_update);
        }
    }

    /**
     Opens the edit texts in the current activity, allowing the user to modify the values of the fields.
     */
    private void openEditTexts(){
        rewardNameTv.setFocusable(true);
        rewardNameTv.setFocusableInTouchMode(true);

        rewardDescTv.setFocusable(true);
        rewardDescTv.setFocusableInTouchMode(true);

        rewardXpTv.setFocusable(true);
        rewardXpTv.setFocusableInTouchMode(true);
    }
}