package com.example.solobeast;

import java.util.ArrayList;

public class User {
    public String picture;
    public ArrayList<task> tasks;
    public ArrayList<Reward> rewards;


    public User(){

    }

    public User(String pic, ArrayList<task> tasks, ArrayList<Reward> rewards) {
        this.picture = pic;
        this.tasks = tasks;
        this.rewards = rewards;

//        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(this.tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
//                if(task.isSuccessful()){
//                    Log.d("AuthData", "add list tasks:success");
//                }else
//                {
//                    Log.d("AuthData", "add list tasks:failure");
//
//                }
//            }
//        });
    }
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
