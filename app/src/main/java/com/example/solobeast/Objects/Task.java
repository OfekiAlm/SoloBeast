package com.example.solobeast.Objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Arrays;
import java.util.List;

@IgnoreExtraProperties
//FOR FIREBASE\\
public class Task {

    public String time;
    public String name;
    public String description;
    public String difficulty;
    public String key;
    public Task(){
        //DEFAULT FOR FIREBASE.\\
    }
    public Task(String name,String time,String description,String difficulty,String key) {
        if(timeIsValid(time)){ this.time = time;}else{this.time = "01:00";}
        if(nameIsValid(name)){ this.name = name;}else{this.name = "Default name";}
        if(descIsValid(description)){this.description = description;}else{this.description = "Default description because none was provided";}
        if(diffIsValid(difficulty)){this.difficulty = difficulty;}else {this.difficulty = "EASY";}
        this.key = key;
    }
    private boolean diffIsValid(String difficulty) {
        if(difficulty.equals("EASY") || difficulty.equals("MEDIUM") || difficulty.equals("HARD"))
            return true;
        return false;
    }
    private boolean descIsValid(String description) {
        if(description.isEmpty()) return false;
        return true;
    }
    private boolean nameIsValid(String name) {
        if(name.isEmpty()) return false;
        return true;
    }
    private boolean timeIsValid(String time) {
        if(time.isEmpty()) return false;
        return true;
    }
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return this.time;
    }
    public String getDifficulty() {
        return this.difficulty;
    }
    public String getDescription() {
        return this.description;
    }
    public String getKey() {
        return this.key;
    }
    public void setDesc(String description) {
        this.description = description;
    }
    public void setDiff(String diff) {
        this.difficulty = diff;
    }
    public void setKey(String key) {
        this.key = key;
    }
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
