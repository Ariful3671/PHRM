package com.troubleshooters.diu.phrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestUricAcid extends AppCompatActivity {

    TextView analyseTestData, analyze, analysisResult;
    EditText inputData;
    RadioGroup genderPicker;
    LinearLayout analysisLayout;
    int analyseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_uric_acid);

        analysisLayout = (LinearLayout)findViewById(R.id.linearLayout_uric_acid);
        analyseTestData=(TextView)findViewById(R.id.analyse_uric_acid);
        analyze = (TextView)findViewById(R.id.btn_calculate_uric_acid);
        analysisResult = (TextView)findViewById(R.id.textView_result);
        inputData = (EditText)findViewById(R.id.editTextUricAcidInput);
        genderPicker = (RadioGroup)findViewById(R.id.RG_gender);
        analyseStatus=0;

        analysisLayout.setVisibility(View.GONE);
        analyseTestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(analyseStatus==1){
                    analysisLayout.setVisibility(View.GONE);
                    analyseStatus=0;
                    analyseTestData.setText("Analyse Test Data");
                }
                else {
                    analysisLayout.setVisibility(View.VISIBLE);
                    analyseStatus = 1;
                    analyseTestData.setText("Close analysis");

                }
            }
        });

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputData.getText().toString();
                if(input.equals("")){
                    Toast.makeText(TestUricAcid.this, "Please input data first!", Toast.LENGTH_SHORT).show();
                }
                float inputVal = Float.parseFloat(input);

                int rBId=genderPicker.getCheckedRadioButtonId();
                if(rBId==R.id.RB_male){
                    analysisResult.setVisibility(View.VISIBLE);
                    if(inputVal>=4.0&&inputVal<=8.5){
                        analysisResult.setText("Your Uric Acid level is Normal");
                    }
                    else if(inputVal<4.0&&inputVal>0.0){
                        analysisResult.setText("Your Uric Acid level is Low");
                    }
                    else if(inputVal>8.5){
                        analysisResult.setText("Your Uric Acid level is High");
                    }
                    else{
                        analysisResult.setText("Invalid data!");
                    }
                }
                else if(rBId==R.id.RB_female){
                    analysisResult.setVisibility(View.VISIBLE);
                    if(inputVal>=2.5&&inputVal<=7.5){
                        analysisResult.setText("Your Uric Acid level is Normal");
                    }
                    else if(inputVal<2.5&&inputVal>0.0){
                        analysisResult.setText("Your Uric Acid level is Low");
                    }
                    else if(inputVal>7.5){
                        analysisResult.setText("Your Uric Acid level is High");
                    }
                    else{
                        analysisResult.setText("Abnormal data!");
                    }
                }
                else{
                    Toast.makeText(TestUricAcid.this, "Please select Gender!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
