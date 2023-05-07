package com.example.solobeast.Objects;

public class User {
    private String phoneNumber;
    private int currentXP;
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
