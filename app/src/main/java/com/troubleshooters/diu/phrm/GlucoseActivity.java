package com.troubleshooters.diu.phrm;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GlucoseActivity extends AppCompatActivity {


    EditText weight,height;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose);


        weight=(EditText)findViewById(R.id.weight);
        height=(EditText)findViewById(R.id.height);
        b=(Button)findViewById(R.id.cal);


        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int w=Integer.parseInt(weight.getText().toString());
                        int h=Integer.parseInt(height.getText().toString());


                        Double volume=w*70.0;

                        android.support.v7.app.AlertDialog.Builder builder;
                        builder = new android.support.v7.app.AlertDialog.Builder(GlucoseActivity.this, R.style.CustomDialogTheme);
                        builder.setMessage(volume+" ml")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                }).show();
                    }
                }
        );

    }
}
