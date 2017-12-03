package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {


    Button buttonLogIn,buttoSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();
        buttoSignUp=(Button)findViewById(R.id.button_sign_up);
        buttonLogIn=(Button)findViewById(R.id.button_log_in);
        buttoSignUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent signUpIntent=new Intent(LogInActivity.this,SignUpActivity.class);
                        startActivity(signUpIntent);
                    }
                }
        );
        buttonLogIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent logInIntent=new Intent(LogInActivity.this,HomeActivity.class);
                        startActivity(logInIntent);
                    }
                }
        );
    }
}
