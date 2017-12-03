package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    Button creatProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        creatProfile=(Button)findViewById(R.id.button_creat_profile_sign_up_screen);
        creatProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
