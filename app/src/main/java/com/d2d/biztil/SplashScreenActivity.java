package com.d2d.biztil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.d2d.biztil.ExceptionHandler.ExceptionHandler;


public
class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static      int    SPLASH_TIME_OUT = 3000;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.act_splash_screen);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public
            void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
