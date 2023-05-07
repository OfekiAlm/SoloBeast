package com.example.solobeast.Objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Reward {
    private int xpAmount;
    private String rewardName;
    private String description;
    private String key;
    public Reward(){
        //DEFAULT FOR FIREBASE.\\
    }
    public Reward(String name, String desc ,int xp,String key){
        if(nameIsValid(name)) {this.rewardName = name;}else {this.rewardName = "Default name";}
        if(descIsValid(desc)){this.description = desc;}else {this.description = "Default description cause none was provided";}
        if (xpIsValid(xp)){this.xpAmount = xp;}else{this.xpAmount = 1;}
        this.key = key;
    }
    private boolean descIsValid(String description) {
        if(description.isEmpty()) return false;
        return true;
    }
    private boolean xpIsValid(int xp){
        boolean a;
        a = (xp != 0) ? true : false;
        return a;
    }
    private boolean nameIsValid(String name) {
        if(name.isEmpty()) return false;
        return true;
    }
    public String getRewardName() {
        return this.rewardName;
    }
    public void setRewardName(String name) {
        this.rewardName = name;
    }
    public void setXP(int xpAmount){ this.xpAmount = xpAmount;}
    public int getXP() {
        return this.xpAmount;
    }
    public void setDesc(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public String toString() {
        return "Reward{" +
                ", name='" + this.rewardName + '\'' +
                ", description='" + this.description + '\'' +
                ", xp='" + this.xpAmount + '\'' +
                '}';
    }

}
