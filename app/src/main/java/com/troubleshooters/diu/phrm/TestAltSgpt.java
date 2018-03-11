package com.troubleshooters.diu.phrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestAltSgpt extends AppCompatActivity {

    TextView analyseTestData, analyze, analysisResult;
    EditText inputData;
    LinearLayout analysisLayout;
    int analyseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alt_sgpt);

        analysisLayout = (LinearLayout)findViewById(R.id.linearLayout_alt_sgpt);
        analyseTestData=(TextView)findViewById(R.id.analyse_alt_sgpt);
        analyze = (TextView)findViewById(R.id.btn_alt_sgpt);
        analysisResult = (TextView)findViewById(R.id.textView_result);
        inputData = (EditText)findViewById(R.id.editTextAltInput);
        analyseStatus = 0;

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
                    Toast.makeText(TestAltSgpt.this, "Please input data first!", Toast.LENGTH_SHORT).show();
                }
                float inputVal = Float.parseFloat(input);


                analysisResult.setVisibility(View.VISIBLE);
                if(inputVal>=7&&inputVal<=55){
                    analysisResult.setText("Your ALT level is Normal");
                }
                else if(inputVal<7&&inputVal>0){
                    analysisResult.setText("Your ALT level is Low");
                }
                else if(inputVal>55){
                    analysisResult.setText("Your ALT level is High");
                }
                else{
                    analysisResult.setText("Invalid data!");
                }

            }
        });
    }
}
