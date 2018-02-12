package com.troubleshooters.diu.phrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestCompleteCholestrol extends AppCompatActivity {

    TextView analyseTestData, analyze, analysisResult;
    EditText ldl, hdl, total, triglycerides;
    LinearLayout analysisLayout;
    int analyseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_complete_cholestrol);

        analysisLayout = (LinearLayout)findViewById(R.id.Complete_Cholesterol_analysis);
        analyseTestData=(TextView)findViewById(R.id.analyse_complete_cholesterol);
        analyze = (TextView)findViewById(R.id.btn_complete_cholesterol_analyse);
        analysisResult = (TextView)findViewById(R.id.textView_result);
        ldl = (EditText)findViewById(R.id.editTextLdl);
        hdl = (EditText)findViewById(R.id.editTextHdl);
        total = (EditText)findViewById(R.id.editTextTotalCholesterol);
        triglycerides = (EditText)findViewById(R.id.editTextivTriglycerides);
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
                String inputLdl = ldl.getText().toString();
                String inputhdl = hdl.getText().toString();
                String inputTotal = total.getText().toString();
                String inputTriglycerides = triglycerides.getText().toString();

                String result = "";

                if(inputLdl.equals("")&&inputhdl.equals("")&&inputTotal.equals("")&&inputTriglycerides.equals("")){
                    Toast.makeText(TestCompleteCholestrol.this, "Please input data first!", Toast.LENGTH_SHORT).show();
                }
                else {
                    analysisResult.setVisibility(View.VISIBLE);
                    if(!inputLdl.equals("")){
                        float ldlVal = Float.parseFloat(inputLdl);
                        if(ldlVal>=70&&ldlVal<=130){
                            result+="LDL level is Normal.\n";
                        }
                        else if(ldlVal<70){
                            result+="LDL level is Low.\n";
                        }
                        else if(ldlVal>130){
                            result+="LDL level is High.\n";
                        }
                        else{
                            result+="Abnormal LDL data!\n";
                        }
                    }
                    if(!inputhdl.equals("")){
                        float hdlVal = Float.parseFloat(inputhdl);
                        if(hdlVal>=40&&hdlVal<=60){
                            result+="HDL level is Normal.\n";
                        }
                        else if(hdlVal<40){
                            result+="HDL level is Low.\n";
                        }
                        else if(hdlVal>60){
                            result+="HDL level is High.\n";
                        }
                        else{
                            result+="Abnormal HDL data!\n";
                        }
                    }
                    if(!inputTotal.equals("")){
                        float totalVal = Float.parseFloat(inputTotal);
                        if(totalVal>=40&&totalVal<=150){
                            result+="Total cholesterol level is Normal.\n";
                        }
                        else if(totalVal>=150&&totalVal<200){
                            result+="Total cholesterol level is Little High.\n";
                        }
                        else if(totalVal>200){
                            result+="Total cholesterol level is High.\n";
                        }
                        else{
                            result+="Abnormal Total cholesterol data!\n";
                        }
                    }
                    if(!inputTriglycerides.equals("")){
                        float triglyceridesVal = Float.parseFloat(inputTriglycerides);
                        if(triglyceridesVal>=10&&triglyceridesVal<=100){
                            result+="Triglycerides level is Normal.\n";
                        }
                        else if(triglyceridesVal>=101&&triglyceridesVal<=150){
                            result+="Triglycerides level is Little High.\n";
                        }
                        else if(triglyceridesVal<10){
                            result+="Triglycerides level is Low.\n";
                        }
                        else if(triglyceridesVal>150){
                            result+="Triglycerides level is High.\n";
                        }
                        else{
                            result+="Abnormal Triglycerides data!\n";
                        }
                    }
                    analysisResult.setText(result);
                }

            }
        });
    }
}
