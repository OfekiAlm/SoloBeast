package com.example.solobeast.Objects;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 Represents a reward that can be earned by a user.
 @author Ofek Almog
 */
@IgnoreExtraProperties
public class Reward {

    /** amount of experience points earned by completing the reward */
    private int xpAmount;

    /** name of the reward */
    private String rewardName;

    /** description of the reward */
    private String description;

    /** key used for identification in the database */
    private String key;

    /**
     Default constructor for Firebase.
     */
    public Reward(){
        //DEFAULT FOR FIREBASE.\\
    }

    /**
     Constructor to create a new Reward object.
     @param name The name of the reward.
     @param desc The description of the reward.
     @param xp The amount of XP to be rewarded.
     @param key The key of the reward.
     */
    public Reward(String name, String desc ,int xp,String key){
        if(nameIsValid(name)) {this.rewardName = name;}else {this.rewardName = "Default name";}
        if(descIsValid(desc)){this.description = desc;}else {this.description = "Default description cause none was provided";}
        if (xpIsValid(xp)){this.xpAmount = xp;}else{this.xpAmount = 1;}
        this.key = key;
    }

    /**
     Checks if a reward description is valid.
     @param description description of the reward
     @return true if the description is not empty, false otherwise
     */
    private boolean descIsValid(String description) {
        if(description.isEmpty()) return false;
        return true;
    }

    /**
     Checks if an experience point value is valid.
     @param xp amount of experience points earned by completing the reward
     @return true if the experience point value is not 0, false otherwise
     */
    private boolean xpIsValid(int xp){
        boolean a;
        a = (xp != 0) ? true : false;
        return a;
    }

    /**
     Checks if a reward name is valid.
     @param name name of the reward
     @return true if the name is not empty, false otherwise
     */
    private boolean nameIsValid(String name) {
        if(name.isEmpty()) return false;
        return true;
    }

    /**
     Returns the name of the reward.
     @return name of the reward
     */
    public String getRewardName() {
        return this.rewardName;
    }

    /**
     Sets the name of the reward.
     @param name name of the reward
     */
    public void setRewardName(String name) {
        this.rewardName = name;
    }

    /**
     Sets the amount of experience points earned by completing the reward.
     @param xpAmount amount of experience points earned by completing the reward
     */
    public void setXP(int xpAmount){ this.xpAmount = xpAmount;}

    /**
     Returns the amount of experience points earned by completing the reward.
     @return amount of experience points earned by completing the reward
     */
    public int getXP() {
        return this.xpAmount;
    }

    /**
     Sets the description of the reward.
     @param description description of the reward
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     Returns the description of the reward.
     @return the description of the reward as a string.
     */
    public String getDescription() {
        return this.description;
    }

    /**

     Returns the key of the reward.
     @return the key of the reward as a string.
     */
    public String getKey() {
        return this.key;
    }

    /**
     Sets the key of the reward.
     @param key the new key to set for the reward.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     Returns a string representation of the reward object.
     @return a string representation of the reward object.
     */
    @Override
    public String toString() {
        return "Reward{" +
                ", name='" + this.rewardName + '\'' +
                ", description='" + this.description + '\'' +
                ", xp='" + this.xpAmount + '\'' +
                '}';
    }

}
