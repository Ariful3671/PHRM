package com.troubleshooters.diu.phrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestBilirubin extends AppCompatActivity {

    TextView analyseTestData, analyze, analysisResult;
    EditText inputData;
    RadioGroup genderPicker;
    LinearLayout analysisLayout;
    int analyseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bilirubin);

        analysisLayout = (LinearLayout)findViewById(R.id.bilirubin_analysis);
        analyseTestData=(TextView)findViewById(R.id.analyse_bilirubin);
        analyze = (TextView)findViewById(R.id.btn_bilirubin_analyse);
        analysisResult = (TextView)findViewById(R.id.textView_result);
        inputData = (EditText)findViewById(R.id.editTextbilirubinInput);
        genderPicker = (RadioGroup)findViewById(R.id.RG_bilirubin_type);
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
                    Toast.makeText(TestBilirubin.this, "Please input data first!", Toast.LENGTH_SHORT).show();
                }
                float inputVal = Float.parseFloat(input);

                int rBId=genderPicker.getCheckedRadioButtonId();
                if(rBId==R.id.RB_direct_bilirubin){
                    analysisResult.setVisibility(View.VISIBLE);
                    if(inputVal>=0.0&&inputVal<=0.4){
                        analysisResult.setText("Your bilirubin level is Normal");
                    }
                    else if(inputVal<0.0){
                        analysisResult.setText("Your bilirubin level is Low");
                    }
                    else if(inputVal>0.4){
                        analysisResult.setText("Your bilirubin level is High");
                    }
                    else{
                        analysisResult.setText("Abnormal data!");
                    }
                }
                else if(rBId==R.id.RB_Total_bilirubin){
                    analysisResult.setVisibility(View.VISIBLE);
                    if(inputVal>=0.3&&inputVal<=1.0){
                        analysisResult.setText("Your bilirubin level is Normal");
                    }
                    else if(inputVal<0.3){
                        analysisResult.setText("Your bilirubin level is Low");
                    }
                    else if(inputVal>1.0){
                        analysisResult.setText("Your bilirubin level is High");
                    }
                    else{
                        analysisResult.setText("Invalid data!");
                    }
                }
                else{
                    Toast.makeText(TestBilirubin.this, "Please select test type!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
