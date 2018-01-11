package com.troubleshooters.diu.phrm;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class ManageItemActivity extends AppCompatActivity {



    EditText age,height,weight;
    //RadioGroup gender;
    Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_item);


        age=(EditText)findViewById(R.id.age);
        height=(EditText)findViewById(R.id.height);
        weight=(EditText)findViewById(R.id.weight);
        //gender=(RadioGroup)findViewById(R.id.rg);
        calculate=(Button)findViewById(R.id.cal);


        calculate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!age.getText().equals("")||!height.getText().equals("")||!weight.getText().equals(""))
                        {
                            Double w=Double.parseDouble(weight.getText().toString());
                            Double h= Double.parseDouble(height.getText().toString());
                            Double BMI=w/((h/100)*(h/100));
                            if(BMI<18.5)
                            {
                                android.support.v7.app.AlertDialog.Builder builder;
                                builder = new android.support.v7.app.AlertDialog.Builder(ManageItemActivity.this, R.style.CustomDialogTheme);
                                builder.setMessage("Under Weight\n"+BMI)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {



                                            }
                                        }).show();
                            }
                            if(BMI>=18.5&&BMI<=24.9)
                            {
                                android.support.v7.app.AlertDialog.Builder builder;
                                builder = new android.support.v7.app.AlertDialog.Builder(ManageItemActivity.this, R.style.CustomDialogTheme);
                                builder.setMessage("Normal Weight\n"+BMI)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {



                                            }
                                        }).show();
                            }
                            if(BMI>=25.0&&BMI<=29.9)
                            {
                                android.support.v7.app.AlertDialog.Builder builder;
                                builder = new android.support.v7.app.AlertDialog.Builder(ManageItemActivity.this, R.style.CustomDialogTheme);
                                builder.setMessage("Overweight\n"+BMI)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {



                                            }
                                        }).show();
                            }
                            if(BMI>=30.0)
                            {
                                android.support.v7.app.AlertDialog.Builder builder;
                                builder = new android.support.v7.app.AlertDialog.Builder(ManageItemActivity.this, R.style.CustomDialogTheme);
                                builder.setMessage("Obese\n"+BMI)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {



                                            }
                                        }).show();
                            }

                        }
                    }
                }
        );

    }
}
