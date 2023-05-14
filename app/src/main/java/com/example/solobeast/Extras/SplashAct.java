package com.example.solobeast.Extras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.solobeast.R;
import com.example.solobeast.ui.Auth.LoginAct;

/**
 The SplashAct is an activity that shows a splash screen with an animation and then starts the main activity.
 @author Ofek Almog
 */
public class SplashAct extends AppCompatActivity {

    /**
     The ImageView displaying the splash screen animation.
     */
    private ImageView splashImage;


    /**
     Called when the activity is starting.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImage = findViewById(R.id.splash_image_imgv);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_rainbow);
        splashImage.startAnimation(animation);

        // Wait for the animation to finish and start the main activity
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashAct.this, LoginAct.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}