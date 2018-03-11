package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import io.paperdb.Paper;

public class AddExerciseActivity extends AppCompatActivity {

    TextView selectExercise, exerciseDuration;
    ListView exerciseList, exerciseDurList;
    Button startExercise, doneExercise, pedometerBtn;
    ArrayAdapter<String> adapter;
    String[] exerciseMets;
    int metPosition, exDurPosition;


    SharedPreferences sharedPreferencesExercise;
    SharedPreferences.Editor sPExerciseEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        selectExercise=(TextView)findViewById(R.id.textView_select_exercise);
        exerciseList=(ListView)findViewById(R.id.list_view_exercise);
        exerciseDurList=(ListView)findViewById(R.id.list_view_exercise_duration);
        exerciseDuration=(TextView) findViewById(R.id.editText_exercise_time);
        startExercise=(Button)findViewById(R.id.start_exercise);
        doneExercise=(Button)findViewById(R.id.done_exercise);
        pedometerBtn=(Button)findViewById(R.id.pedometer_btn);
        exerciseMets = getResources().getStringArray(R.array.exercise_met);

        sharedPreferencesExercise = getSharedPreferences("exercise", Context.MODE_PRIVATE);
        sPExerciseEditor = sharedPreferencesExercise.edit();

        final SharedPreferences sharedPreferencesPI=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);

        ArrayList<String> exerciseArrayList=new ArrayList<>();
        exerciseArrayList.addAll(Arrays.asList(getResources().getStringArray(R.array.exercise_list)));
        adapter=new ArrayAdapter<String>(AddExerciseActivity.this,android.R.layout.simple_list_item_1,exerciseArrayList);
        exerciseList.setAdapter(adapter);

        selectExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.setVisibility(View.VISIBLE);
                exerciseList.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectExercise.setText(exerciseList.getItemAtPosition(position).toString());
                                exerciseList.setVisibility(View.GONE);
                                metPosition=position;
                            }
                        }
                );
            }
        });

        final int[] durationArray = {5,10,15,20,25,30,40,45,50,60,75,90};
        ArrayList<String> exerciseDurationList=new ArrayList<>();
        exerciseDurationList.addAll(Arrays.asList(getResources().getStringArray(R.array.exercise_duration)));
        adapter=new ArrayAdapter<String>(AddExerciseActivity.this,android.R.layout.simple_list_item_1,exerciseDurationList);
        exerciseDurList.setAdapter(adapter);

        exerciseDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseDurList.setVisibility(View.VISIBLE);
                exerciseDurList.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                exerciseDuration.setText(exerciseDurList.getItemAtPosition(position).toString());
                                exerciseDurList.setVisibility(View.GONE);
                                exDurPosition=position;
                            }
                        }
                );
            }
        });

        startExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exerciseName = selectExercise.getText().toString();
                String exerciseDurn = exerciseDuration.getText().toString();
                if(exerciseName==""||exerciseDurn==""){
                    Toast.makeText(AddExerciseActivity.this, "Missing Exercise or Exercise Duration", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        sPExerciseEditor.putInt("exercise_duration", durationArray[exDurPosition]);
                        sPExerciseEditor.commit();
                        Intent intent=new Intent(AddExerciseActivity.this,StartExerciseActivity.class);
                        intent.putExtra("weight", sharedPreferencesPI.getString("weight", ""));
                        intent.putExtra("met", exerciseMets[metPosition]);
                        intent.putExtra("exerciseDetail",exerciseName+"\nfor "+exerciseDurn);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(AddExerciseActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        pedometerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddExerciseActivity.this,Pedometer.class);
                startActivity(intent);
            }
        });

        doneExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String exerciseName = selectExercise.getText().toString();
                    String exerciseDurn = exerciseDuration.getText().toString();
                    if(exerciseName==""||exerciseDurn==""){
                        Toast.makeText(AddExerciseActivity.this, "Missing Exercise or Exercise Duration", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Double exerciseDurnVal = Double.valueOf(durationArray[exDurPosition]);
                        Float previouslyBurnedCalorie = sharedPreferencesExercise.getFloat("burnedCalorie", 0.0f);
                        String weight = sharedPreferencesPI.getString("weight", "");
                        Double weightVal = Double.parseDouble(weight);
                        Double met = Double.parseDouble(exerciseMets[metPosition]);

                        Double durationVal = Double.valueOf(durationArray[exDurPosition]);
                        Double burnedCalorie = 0.0175*met*weightVal*durationVal-(0.0175*weightVal*durationVal);
                        String value=new DecimalFormat("##.#").format(burnedCalorie);
                        Toast.makeText(AddExerciseActivity.this, "You burned "+value+" calories", Toast.LENGTH_LONG).show();

                        if(previouslyBurnedCalorie!=0.0){
                            Double pBurnedCalorie = new Double(previouslyBurnedCalorie);
                            burnedCalorie += pBurnedCalorie;
                        }

                        Float value1=new Float(burnedCalorie);
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        Float formatedVal = Float.valueOf(decimalFormat.format(value1));
                        sPExerciseEditor.putFloat("burnedCalorie", formatedVal);
                        sPExerciseEditor.commit();
                    }
                }catch (Exception e){
                    Toast.makeText(AddExerciseActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

    }
}
