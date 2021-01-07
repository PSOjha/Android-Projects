package com.asw.mysqlreglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 3000;
    SharedPreference session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SharedPreference(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                finish();
              /*  Intent n = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(n);
                finish();*/
                if (new SharedPreference(SplashActivity.this).getEmpMob().equalsIgnoreCase("")) {
                    Intent n = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(n);
                    finish();
                } else {
                    Intent n = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(n);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}