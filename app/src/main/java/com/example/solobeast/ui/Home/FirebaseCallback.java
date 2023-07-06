package com.example.solobeast.ui.Home;

/**
 Interface definition for a callback to be invoked when Firebase database operations complete.
 @author Ofek Almog
 */
public interface FirebaseCallback {

    /**
     Called when user details are received from the Firebase database.
     */
    void onUserDetailsReceived();
}
