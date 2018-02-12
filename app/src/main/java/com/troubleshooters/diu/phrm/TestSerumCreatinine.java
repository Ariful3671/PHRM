package com.troubleshooters.diu.phrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestSerumCreatinine extends AppCompatActivity {

    TextView analyseTestData, analyze, analysisResult;
    EditText inputData;
    RadioGroup genderPicker;
    LinearLayout analysisLayout;
    int analyseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_serum_creatinine);

        analysisLayout = (LinearLayout)findViewById(R.id.serum_creatinine_analysis);
        analyseTestData=(TextView)findViewById(R.id.analyse_serum);
        analyze = (TextView)findViewById(R.id.btn_serum_analyse);
        analysisResult = (TextView)findViewById(R.id.textView_result);
        inputData = (EditText)findViewById(R.id.editTextSerumInput);
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
                    Toast.makeText(TestSerumCreatinine.this, "Please input data first!", Toast.LENGTH_SHORT).show();
                }
                float inputVal = Float.parseFloat(input);

                int rBId=genderPicker.getCheckedRadioButtonId();
                if(rBId==R.id.RB_male){
                    analysisResult.setVisibility(View.VISIBLE);
                    if(inputVal>=0.7&&inputVal<=1.3){
                        analysisResult.setText("Your Serum Creatinine level is Normal");
                    }
                    else if(inputVal<0.7){
                        analysisResult.setText("Your Serum Creatinine level is Low");
                    }
                    else if(inputVal>1.3){
                        analysisResult.setText("Your Serum Creatinine level is High");
                    }
                    else{
                        analysisResult.setText("Invalid data!");
                    }
                }
                else if(rBId==R.id.RB_female){
                    analysisResult.setVisibility(View.VISIBLE);
                    if(inputVal>=0.6&&inputVal<=1.1){
                        analysisResult.setText("Your Serum Creatinine level is Normal");
                    }
                    else if(inputVal<0.6){
                        analysisResult.setText("Your Serum Creatinine level is Low");
                    }
                    else if(inputVal>1.1){
                        analysisResult.setText("Your Serum Creatinine level is High");
                    }
                    else{
                        analysisResult.setText("Abnormal data!");
                    }
                }
                else{
                    Toast.makeText(TestSerumCreatinine.this, "Please select Gender!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
