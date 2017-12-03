package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MedicationActivity extends AppCompatActivity {

    Button addPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        setTitle("Medication plan");

        addPlan=(Button)findViewById(R.id.addPlan);
        addPlan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MedicationActivity.this,CreatMedicationPlanActivity.class);
                        startActivity(intent);
                    }
                }
        );


    }
}
