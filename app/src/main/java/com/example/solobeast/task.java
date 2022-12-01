package com.example.solobeast;


public class task {



    private int time;
    private String name;

    public task(int time, String name) {
        this.time = time;
        this.name = name;
    }
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
