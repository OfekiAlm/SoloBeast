package com.example.solobeast.Objects;

import java.util.Arrays;
import java.util.List;

public class Task {

    private String time;
    private String name;
    private String description;
    private String difficulty;

    public Task(String name,String time,String description,String difficulty) {
        if(timeIsValid(time)){ this.time = time;}
        if(nameIsValid(name)){ this.name = name;}
        if(descIsValid(description)){this.description = description;}
        if(diffIsValid(difficulty)){this.difficulty = difficulty;}
    }


    ///VALIDATION FUNCTIONS\\\


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
        //HH:MM\\
        return true;
    }
    ///\\\

    public static String timeRepresentation(String time){
        List<String> arrayList = Arrays.asList(time.split(":"));
        String hours = arrayList.get(0);
        String minutes = arrayList.get(1);
        if(hours.equals("00")){ return minutes + " minutes";}
        else if(hours.startsWith("0")){


            return Arrays.asList(hours.split("0")).get(1) + " hours";
        }
        else if(minutes.equals("00")){ return hours + " hours";}
        return hours+" hours" + " and " + minutes + " minutes";
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

    public String getDesc() {
        return this.description;
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
