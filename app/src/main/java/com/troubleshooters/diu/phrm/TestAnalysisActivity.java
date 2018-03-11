package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import io.paperdb.Paper;

public class TestAnalysisActivity extends AppCompatActivity {

    ListView listViewTest;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_analysis);
        setTitle("Test Analysis");

        listViewTest = (ListView) findViewById(R.id.list_view_test);
        ArrayList<String> testNames=new ArrayList<>();
        testNames.addAll(Arrays.asList(getResources().getStringArray(R.array.test_list)));
        adapter=new ArrayAdapter<String>(TestAnalysisActivity.this,android.R.layout.simple_list_item_1,testNames);
        listViewTest.setAdapter(adapter);

        listViewTest.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        switch (position){
                            case 0:
                                Intent intentSerum = new Intent(TestAnalysisActivity.this,TestSerumCreatinine.class);
                                startActivity(intentSerum);
                                break;
                            case 1:
                                Intent intentBilirubin = new Intent(TestAnalysisActivity.this,TestBilirubin.class);
                                startActivity(intentBilirubin);
                                break;
                            case 2:
                                Intent intentCholesterol = new Intent(TestAnalysisActivity.this,TestCompleteCholestrol.class);
                                startActivity(intentCholesterol);
                                break;
                            case 3:
                                Intent intentAltSgpt = new Intent(TestAnalysisActivity.this,TestAltSgpt.class);
                                startActivity(intentAltSgpt);
                                break;
                            case 4:
                                Intent intentAstSgot = new Intent(TestAnalysisActivity.this,TestAstSgot.class);
                                startActivity(intentAstSgot);
                                break;
                            case 5:
                                Intent intentUricAcid = new Intent(TestAnalysisActivity.this, TestUricAcid.class);
                                startActivity(intentUricAcid);
                                break;
                            case 6:
                                Intent intentEsr = new Intent(TestAnalysisActivity.this, TestESR.class);
                                startActivity(intentEsr);
                                break;
                            case 7:
                                Intent intentUrineRoutineExam = new Intent(TestAnalysisActivity.this, TestUrineRoutineExam.class);
                                startActivity(intentUrineRoutineExam);
                                break;
                        }
                    }
                }
        );
    }
}
