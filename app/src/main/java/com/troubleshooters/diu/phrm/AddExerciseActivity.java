package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import io.paperdb.Paper;

public class AddExerciseActivity extends AppCompatActivity {

    TextView selectExercise;
    ListView exerciseList;
    EditText exerciseDuration;
    Button startExercise, doneExercise;
    ArrayAdapter<String> adapter;
    String[] exerciseMets;
    int metPosition;

    SharedPreferences sharedPreferencesExercise;
    SharedPreferences.Editor sPExerciseEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        selectExercise=(TextView)findViewById(R.id.textView_select_exercise);
        exerciseList=(ListView)findViewById(R.id.list_view_exercise);
        exerciseDuration=(EditText) findViewById(R.id.editText_exercise_time);
        startExercise=(Button)findViewById(R.id.start_exercise);
        doneExercise=(Button)findViewById(R.id.done_exercise);
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

        doneExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exerciseDurn = exerciseDuration.getText().toString();
                if(exerciseDurn==""){
                    Toast.makeText(AddExerciseActivity.this, "Please enter Exercise Duration", Toast.LENGTH_SHORT).show();
                }
                else{
                    Double exerciseDurnVal = Double.parseDouble(exerciseDurn);
                    if(exerciseDurnVal>120){
                        Toast.makeText(AddExerciseActivity.this, "Exercise duration must be less than or equal 120", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String previouslyBurnedCalorie = sharedPreferencesExercise.getString("burnedCalorie", "");
                        String weight = sharedPreferencesPI.getString("weight", "");
                        Double weightVal = Double.parseDouble(weight);
                        Double met = Double.parseDouble(exerciseMets[metPosition]);
                        String duration = exerciseDuration.getText().toString();
                        Double durationVal = Double.parseDouble(duration);
                        Double burnedCalorie = 0.0175*met*weightVal*durationVal;
                        String value=new DecimalFormat("##.#").format(burnedCalorie);
                        Toast.makeText(AddExerciseActivity.this, "You burned "+value+" calories", Toast.LENGTH_LONG).show();

                        if(previouslyBurnedCalorie!=""){
                            Double pBurnedCalorie = Double.parseDouble(previouslyBurnedCalorie);
                            burnedCalorie += pBurnedCalorie;
                        }

                        String value1=new DecimalFormat("##.#").format(burnedCalorie);
                        sPExerciseEditor.putString("burnedCalorie", value1);
                        sPExerciseEditor.commit();
                    }
                }
                finish();
            }
        });

    }
}
