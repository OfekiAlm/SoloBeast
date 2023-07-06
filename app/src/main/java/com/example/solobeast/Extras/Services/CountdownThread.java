package com.example.solobeast.Extras.Services;

import android.util.Log;

/**
 This class represents a thread that runs a countdown timer functionality.
 It counts down from a specified time in seconds and stops when the timer reaches zero or is stopped manually.
 @author Ofek Almog
 */
public class CountdownThread extends Thread{

    /**
     The remaining time on the timer.
     */
    private int time;

    /**
     A boolean flag indicating whether the timer has been stopped.
     */
    private boolean stopped = false;

    /**
     The method that runs when the thread is started.
     It counts down from the specified time in seconds and stops when the timer reaches zero or is stopped manually.
     */
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

    /**
     Returns the remaining time in seconds of the countdown timer.
     @return The remaining time in seconds of the countdown timer.
     */
    public int getTime(){return this.time;}

    /**
     Sets the remaining time in seconds of the countdown timer.
     @param time The remaining time in seconds of the countdown timer to set.
     */
    public void setTime(int time){
        this.time = time;
    }

    /**
     Stops the countdown timer manually.
     */
    public void stopTimer() {
        this.stopped = true;
    }
}
