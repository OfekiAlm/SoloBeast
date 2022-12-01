package com.example.solobeast;

import java.util.ArrayList;

public class User {
    public String picture;
    public ArrayList<task> tasks;
    public ArrayList<Reward> rewards;


    public User(){

    }

    public User(String pic) {
        this.picture = pic;
    }
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
