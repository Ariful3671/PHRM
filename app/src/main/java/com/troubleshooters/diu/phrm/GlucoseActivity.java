package com.troubleshooters.diu.phrm;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GlucoseActivity extends AppCompatActivity {


    EditText glucouselevl;
    Button b;
    Spinner fasting,diabatics;
    ArrayAdapter<CharSequence> adapter1,adapter2;

    String fastingStatus,diabaticsStatus;
    Double glucouse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose);
        fasting=(Spinner)findViewById(R.id.spinner_select_fasting);
        diabatics=(Spinner)findViewById(R.id.spinner_select_diabatics_status);
        adapter1=ArrayAdapter.createFromResource(this,R.array.fasting_status,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fasting.setAdapter(adapter1);
        adapter2=ArrayAdapter.createFromResource(this,R.array.diabatics_status,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diabatics.setAdapter(adapter2);
        glucouselevl=(EditText)findViewById(R.id.weight);
        b=(Button)findViewById(R.id.cal);


        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        android.support.v7.app.AlertDialog.Builder builder;
                        if(!glucouselevl.getText().toString().equals("")&&!fasting.getSelectedItem().toString().equals("Select option")&&!diabatics.getSelectedItem().toString().equals("Select option"))
                        {
                            glucouse=Double.valueOf(glucouselevl.getText().toString());
                            fastingStatus=fasting.getSelectedItem().toString();
                            diabaticsStatus=diabatics.getSelectedItem().toString();
                            if(fasting.getSelectedItem().toString().equals("Fasting"))
                            {

                                if (diabatics.getSelectedItem().toString().equals("With Diabatics"))
                                {
                                    if(glucouse>=4.4||glucouse<=7.2)
                                    {
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is NORMAL")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                    else if(glucouse<4.4)
                                    {
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is LOW")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                    else{
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is HIGH")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                }
                                else {
                                    if(glucouse>=3.9||glucouse<=5.5)
                                    {
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is NORMAL")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                    else if(glucouse<3.9)
                                    {
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is LOW")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                    else{
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is HIGH")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                }
                            }
                            else{
                                if (diabatics.getSelectedItem().toString().equals("With Diabatics"))
                                {
                                    if(glucouse<=10.0)
                                    {
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is NORMAL")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                    else{
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is HIGH")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                }
                                else {
                                    if(glucouse<=7.8)
                                    {
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is NORMAL")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                    else{
                                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                                        builder.setMessage("Your glucose level is HIGH")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).show();
                                    }
                                }
                            }
                        }
                        else {
                            Toast.makeText(GlucoseActivity.this, "All fields are not set", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

    }
}
