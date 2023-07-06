package com.example.solobeast.Objects;

/**
 * Represents a user
 * @author Ofek Almog
 */
public class User {

    /** The phone number of the user*/
    private String phoneNumber;

    /** The current XP amount of the user*/
    private int currentXP;

    /**
     * Default constructor for Firebase.
     */
    public User(){

    }

    /**
     * Constructor for creating a user with a given phone number.
     * @param phoneNum the phone number of the user
     */
    public User(String phoneNum) {
        this.phoneNumber = phoneNum;
        this.currentXP = 0;
    }

    /**
     * Returns the current XP of the user.
     * @return the current XP
     */
    public int getCurrentXP() {
        return currentXP;
    }

    /**
     * Sets the current XP of the user.
     * @param currentXP the current XP to set
     */
    public void setCurrentXP(int currentXP) {
        this.currentXP = currentXP;
    }

    /**
     * Sets the phone number of the user.
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the phone number of the user.
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns a string representation of the User object.
     * @return a string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" +
                "phoneNumber='" + this.phoneNumber + '\'' +
                ", currentXP=" + this.currentXP +
                '}';
    }
}
