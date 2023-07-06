package com.example.solobeast.Objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Arrays;
import java.util.List;

/**
 Task class represents a task with a name, description, difficulty, and estimated time to complete it.
 This class is used in the context of a Firebase database.
 @author Ofek Almog
 */
@IgnoreExtraProperties
//FOR FIREBASE\\
public class Task {

    /** The estimated time to complete the task, represented as a string. */
    private String time;

    /**The name of the task, represented as a string.*/
    private String name;

    /**The description of the task, represented as a string.*/
    private String description;

    /**The difficulty of the task, represented as a string.*/
    private String difficulty;

    /** key used for identification in the database. */
    private String key;

    /**
     Default constructor for Firebase.
     */
    public Task(){
        //DEFAULT FOR FIREBASE.\\
    }

    /**
     Constructor for Task class.
     @param name the name of the task
     @param time the estimated time to complete the task
     @param description the description of the task
     @param difficulty the difficulty level of the task
     @param key the key associated with the task in Firebase
     */
    public Task(String name,String time,String description,String difficulty,String key) {
        if(timeIsValid(time)){ this.time = time;}else{this.time = "01:00";}
        if(nameIsValid(name)){ this.name = name;}else{this.name = "Default name";}
        if(descIsValid(description)){this.description = description;}else{this.description = "Default description because none was provided";}
        if(diffIsValid(difficulty)){this.difficulty = difficulty;}else {this.difficulty = "EASY";}
        this.key = key;
    }

    /**
     Check if the difficulty level provided is valid.
     @param difficulty the difficulty level
     @return true if the difficulty is EASY, MEDIUM, or HARD; false otherwise
     */
    private boolean diffIsValid(String difficulty) {
        if(difficulty.equals("EASY") || difficulty.equals("MEDIUM") || difficulty.equals("HARD"))
            return true;
        return false;
    }

    /**
     Check if the task description provided is valid.
     @param description the task description
     @return true if the description is not empty; false otherwise
     */
    private boolean descIsValid(String description) {
        if(description.isEmpty()) return false;
        return true;
    }

    /**
     Check if the task name provided is valid.
     @param name the task name
     @return true if the name is not empty; false otherwise
     */
    private boolean nameIsValid(String name) {
        if(name.isEmpty()) return false;
        return true;
    }

    /**
     Check if the estimated time provided is valid.
     @param time the estimated time
     @return true if the time is not empty; false otherwise
     */
    private boolean timeIsValid(String time) {
        if(time.isEmpty()) return false;
        return true;
    }

    /**
     Get the color associated with the difficulty level.
     @param diff the difficulty level
     @return the color as a hexadecimal string
     */
    public static String changeColorAsTaskDifficulty(String diff){
        String greenHex = "#0eeb0e";
        String orangeHex = "#eba10e";
        String redHex = "#db200b";

        switch (diff){
            case "EASY":
                return greenHex;
            case "MEDIUM":
                return orangeHex;
            case "HARD":
                return redHex;
        }
        return "";
    }

    /**
     * Converts a time string in the format "hh:mm" into a human-readable representation of hours and minutes.
     * @param hourAndMinute the time string in the format "hh:mm"
     * @return a human-readable representation of hours and minutes, such as "1 hour and 30 minutes"
     */
    public static String timeRepresentation(String hourAndMinute) {
        String[] parts = hourAndMinute.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        String hourString = hours == 1 ? "hour" : "hours";
        String minuteString = minutes == 1 ? "minute" : "minutes";

        if (hours == 0 && minutes == 1) {
            return "1 minute";
        } else if (hours == 0) {
            return minutes + " " + minuteString;
        } else if (minutes == 0) {
            return hours + " " + hourString;
        } else {
            return hours + " " + hourString + " and " + minutes + " " + minuteString;
        }
    }

    /**
     * Returns the name of the task.
     * @return the name of the task.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the task.
     * @param name the name of the task.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the time required for the task.
     * @param time the time required for the task.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns the time required for the task.
     * @return the time required for the task.
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Returns the difficulty level of the task.
     * @return the difficulty level of the task.
     */
    public String getDifficulty() {
        return this.difficulty;
    }

    /**
     * Returns the description of the task.
     * @return the description of the task.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the key of the task.
     * @return the key of the task.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Sets the description of the task.
     * @param description the description of the task.
     */
    public void setDesc(String description) {
        this.description = description;
    }

    /**
     * Sets the difficulty level of the task.
     * @param diff the difficulty level of the task.
     */
    public void setDiff(String diff) {
        this.difficulty = diff;
    }

    /**
     * Sets the key of the task.
     * @param key the key of the task.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns a string representation of the task, including its time, name, description, and difficulty level.
     * @return a string representation of the task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "time='" + this.time + '\'' +
                ", name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", difficulty='" + this.difficulty + '\'' +
                '}';
    }
}
