package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.troubleshooters.diu.phrm.Adapter.MedicationPlanGridAdapter;

public class CreatMedicationPlanActivity extends AppCompatActivity {


    Button add;
    RelativeLayout relativeLayout;
    //LinearLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_medication_plan);
        setTitle("Set Plan");

        //final LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout=(RelativeLayout) findViewById(R.id.medication_details_realtive_layout);
        add=(Button)findViewById(R.id.add_button_medication_details);
        //layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View new_layout=inflater.inflate(R.layout.medication_deatils,null);
                        relativeLayout.addView(new_layout,relativeLayout.getChildCount()-1);
                    }
                }
        );

    }
}
