package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.solobeast.Objects.Reward;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedRewardAct extends AppCompatActivity {

    TextView rewardNameTv, rewardDescTv,rewardXpTv;
    Reward reward;
    FloatingActionButton submitFloatingFormBtn;

    int counter = 0;

    String fromActivity;
    String userChoice;

    DatabaseReference listRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_reward);

        init();
        reward = new Reward();
        determineEditOrAdd(fromActivity);

        submitFloatingFormBtn.setOnClickListener(view -> {
            counter++;
            if(counter == 1){
                openEditTexts();
                setUpdateIconDrawable();
            }
            else if(counter >= 2){
                if(userChoice.equals("AddReward")){
                    addRewardToFirebase();
                }
                else{
                    updateRewardToFirebase();
                }
            }
        });
    }


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

    private void getValuesFromPrevActivityReward() {
        reward.setRewardName(getIntent().getStringExtra("selected_reward_name"));
        reward.setDesc(getIntent().getStringExtra("selected_reward_desc"));
        reward.setXP(getIntent().getIntExtra("selected_reward_xp",0));
        reward.setKey(getIntent().getStringExtra("selected_reward_key"));
    }
    public int strToInt(String str){return Integer.parseInt(str);}
    private void insertEditTextsValues() {
        rewardNameTv.setText(reward.getRewardName());
        rewardDescTv.setText(reward.getDescription());
        rewardXpTv.setText(String.valueOf(reward.getXP()));
    }

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

    private void setAddIconDrawable() {
        if(submitFloatingFormBtn != null) {
            submitFloatingFormBtn.setImageResource(R.drawable.ic_baseline_add);
        }
    }
    private void setUpdateIconDrawable() {
        if(submitFloatingFormBtn != null) {
            submitFloatingFormBtn.setImageResource(R.drawable.ic_baseline_update);
        }
    }

    private void openEditTexts(){
        rewardNameTv.setFocusable(true);
        rewardNameTv.setFocusableInTouchMode(true);

        rewardDescTv.setFocusable(true);
        rewardDescTv.setFocusableInTouchMode(true);

        rewardXpTv.setFocusable(true);
        rewardXpTv.setFocusableInTouchMode(true);
    }
}