package com.expenses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferenceManager.init(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferenceManager.read(SharedPreferenceManager.IS_ANY_RECORD, false))
                    startActivity(new Intent(Splash.this, MainListViewScreen.class));
                else
                    startActivity(new Intent(Splash.this, MainActivity.class));

                finish();
            }
        }, 3000);
    }
}