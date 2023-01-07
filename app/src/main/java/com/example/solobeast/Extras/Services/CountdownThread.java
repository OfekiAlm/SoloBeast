package com.example.solobeast.Extras.Services;

import android.util.Log;

public class CountdownThread extends Thread{

    private int time;
    private boolean stopped = false;
    @Override
    public void run() {
        super.run();
        this.time = getTime();
        while(this.time > 0 && !stopped){
            try {
                Thread.sleep(1000);
                Log.d("services","timer : " + time);
                time--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        Log.i("services","TimerStopped:Success");
    }

    public int getTime(){return this.time;}
    public void setTime(int time){
        this.time = time;
    }

    public void stopTimer() {
        this.stopped = true;
    }
}
