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

public class SplashAct extends AppCompatActivity {

    private ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Thread splashScreenStarter = new Thread() {
//            public void run() {
//                try {
//                    int delay = 0;
//                    while (delay < 100) { // 5 seconds
//                        sleep(100);
//                        delay = delay + 100;
//                    }
//                    startActivity(new Intent(SplashAct.this, LoginAct.class));
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    finish();
//                }
//            }
//
//        };
//        splashScreenStarter.start();
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