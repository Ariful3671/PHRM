package com.troubleshooters.diu.phrm;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeScreenActivity extends AppCompatActivity {

    private static int splash_time_out=4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //Hide acton bar
        getSupportActionBar().hide();


        //Change the color of status bar
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.actionBarColor));


        }
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("logout status","").equals("yes")||sharedPreferences.getString("logout status","").equals(""))
        {
            //Exit welcome screen after 4 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(WelcomeScreenActivity.this,LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            },splash_time_out);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(WelcomeScreenActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            },splash_time_out);
        }



    }
}
