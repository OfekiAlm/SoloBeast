package com.example.solobeast.Objects;

import java.util.ArrayList;

public class User {
    public String phoneNumber;
    public int currentXP;
    public ArrayList<Task> tasks;
    public ArrayList<Reward> rewards;


    public User(){

    }

    public User(String phoneNum) {
        this.phoneNumber = phoneNum;
        this.currentXP = 0;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(int currentXP) {
        this.currentXP = currentXP;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
