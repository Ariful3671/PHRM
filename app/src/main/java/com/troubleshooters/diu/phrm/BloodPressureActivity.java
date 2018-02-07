package com.troubleshooters.diu.phrm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BloodPressureActivity extends AppCompatActivity {


    EditText up,down;
    Button calculate;


    int up_pressure;
    int down_pressure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);


        up=(EditText)findViewById(R.id.systolic);
        down=(EditText)findViewById(R.id.diastolic);
        calculate=(Button)findViewById(R.id.calculate_button);

        calculate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(!up.getText().toString().equals("")&&!down.getText().toString().equals(""))
                        {
                            up_pressure=Integer.parseInt(up.getText().toString());
                            down_pressure=Integer.parseInt(down.getText().toString());
                        }


                        if(up_pressure<90&&down_pressure<60)
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(BloodPressureActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("You have a Low Blood Pressure!!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {



                                        }
                                    }).show();
                        }
                        else if((up_pressure>=90&&up_pressure<=120)&&(down_pressure>=60&&down_pressure<=80))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(BloodPressureActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Your blood pressure reading is ideal and healthy")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {



                                        }
                                    }).show();
                        }
                        else if((up_pressure>=121&&up_pressure<=140)&&(down_pressure>=81&&down_pressure<120))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(BloodPressureActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("You have a normal blood pressure.but it is little higher then usual")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {



                                        }
                                    }).show();
                        }
                        else if(up_pressure>=141&&down_pressure>=121)
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(BloodPressureActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("You have have a High Blood Pressure!!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {



                                        }
                                    }).show();
                        }
                        else{
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(BloodPressureActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Blood pressure reading:\n\n" +
                                    "systolic(less then 90) and diastolic(less then 60\n"
                                    +"LOW BLOOD PRESSURE\n"
                                    +"systolic(90-120) and diastolic(60-80)\n"
                                    +"HEALTHY BLOOD PRESSURE\n"
                                    +"systolic(121-140) and diastolic(81-120)\n"
                                    +"NORMAL BLOOD PRESSURE\n"
                                    +"systolic(141 to more) and diastolic(121 to more)\n"
                                    +"HIGH BLOOD PRESSURE\n")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent=new Intent(BloodPressureActivity.this,HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }).show();
                        }



                    }
                }
        );




    }
}
