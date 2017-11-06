package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeScreenActivity extends AppCompatActivity {

    private static int splash_time_out=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeScreenActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
            }
        },splash_time_out);
    }
}
