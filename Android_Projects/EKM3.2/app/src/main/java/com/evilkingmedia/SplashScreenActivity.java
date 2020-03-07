package com.evilkingmedia;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;

public class SplashScreenActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                setContentView(R.layout.activity_splash);
            } else {
                setContentView(R.layout.activity_splash_portrait);
            }

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreenActivity.this, CategoryActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, 3 * 1000);
        }
}


