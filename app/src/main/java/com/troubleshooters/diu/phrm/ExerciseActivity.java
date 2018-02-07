package com.troubleshooters.diu.phrm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ExerciseActivity extends AppCompatActivity {

    TextView burnedCalorie, burnableCalorie;
    ProgressBar calorieBurnProgressbar;
    BarChart exerciseBarChart;
    TextView addExercise, addTarget;
    Switch switchReminder;
    EditText exerciseReminder1, exerciseReminder2, exerciseReminder3;
    TextView setTime1,setTime2, setTime3;
    LinearLayout exerciseReminderLayout;

    SharedPreferences sharedPreferencesExercise;
    SharedPreferences.Editor sPExerciseEditor;

    String burnableCalorieVal;
    Float burnedCalorieVal;

    BarDataSet barDataSetEntries;
    String[] daysOfWeak;

    SimpleDateFormat sdf;
    Date d;
    String dayOfTheWeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setTitle("Exercise plan");

        burnedCalorie=(TextView)findViewById(R.id.burned_cal);
        burnableCalorie=(TextView)findViewById(R.id.burn_target);
        calorieBurnProgressbar=(ProgressBar)findViewById(R.id.calorie_burn_progressbar);
        exerciseBarChart=(BarChart)findViewById(R.id.exercise_barchart);
        addExercise=(TextView)findViewById(R.id.text_view_add_exercise);
        addTarget=(TextView)findViewById(R.id.text_view_add_target);
        switchReminder=(Switch)findViewById(R.id.switch_exercise_reminder);
        exerciseReminder1=(EditText)findViewById(R.id.editText_exercise_reminder1);
        exerciseReminder2=(EditText)findViewById(R.id.editText_exercise_reminder2);
        exerciseReminder3=(EditText)findViewById(R.id.editText_exercise_reminder3);
        setTime1=(TextView)findViewById(R.id.set_time_exercise_reminder1);
        setTime2=(TextView)findViewById(R.id.set_time_exercise_reminder2);
        setTime3=(TextView)findViewById(R.id.set_time_exercise_reminder3);
        exerciseReminderLayout=(LinearLayout)findViewById(R.id.exercise_reminder_layout);
        exerciseReminderLayout.setVisibility(View.GONE);

        sharedPreferencesExercise=getSharedPreferences("exercise", Context.MODE_PRIVATE);
        sPExerciseEditor = sharedPreferencesExercise.edit();

        sdf = new SimpleDateFormat("EEE");
        d = new Date();
        dayOfTheWeek = sdf.format(d);

        //Reset gain value 12:00
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY ,24);
        calendar.set(Calendar.MINUTE,0);
        Intent intent=new Intent(getApplicationContext(),ResetApp.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),2000,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);




        setCalorie();
        setBarChart();



        //Check profile id fully updated
        SharedPreferences sharedPreferences_checker=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        if(sharedPreferences_checker.getString("weight","").equals(""))
        {
            android.support.v7.app.AlertDialog.Builder builder;
            builder = new android.support.v7.app.AlertDialog.Builder(ExerciseActivity.this, R.style.CustomDialogTheme);
            builder.setMessage("Please update your profile first!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent=new Intent(ExerciseActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).show();
        }
        else{
            //Update profile

            if(sharedPreferencesExercise.getString("target","").equals("")){
                Toast.makeText(this, "You don't have any target yet!", Toast.LENGTH_SHORT).show();
            }

        }

        addTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder exerciseTargetDialogBuilder;
                exerciseTargetDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ExerciseActivity.this,R.style.CustomDialogTheme);
                View lview=getLayoutInflater().inflate(R.layout.set_exercise_target_dialog,null);
                final RadioGroup RG=lview.findViewById(R.id.RG_exercise_target_picker);
                final EditText setDailyTarget = lview.findViewById(R.id.editTextDailyExerciseTarget);
                final EditText setWeaklyTarget = lview.findViewById(R.id.editTextWeaklyExerciseTarget);
                exerciseTargetDialogBuilder.setView(lview);
                exerciseTargetDialogBuilder.setMessage("Set target to lose calorie");
                exerciseTargetDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int id = RG.getCheckedRadioButtonId();
                        String exerciseTarget;
                        Double maxVal;
                        if (id == R.id.RB_daily_target) {
                            exerciseTarget = String.valueOf(setDailyTarget.getText());
                            if(exerciseTarget==""){
                                Toast.makeText(ExerciseActivity.this, "You did not select any target option!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                maxVal = Double.parseDouble(exerciseTarget);
                                if (maxVal > 2000.0 || maxVal < 100) {
                                    Toast.makeText(ExerciseActivity.this, "Exercise target must be between 100 to 2000 Calorie", Toast.LENGTH_LONG).show();
                                } else {
                                    sPExerciseEditor.putString("target", exerciseTarget);
                                    sPExerciseEditor.commit();
                                }
                            }

                        } else if (id == R.id.RB_weakly_target) {
                            setDailyTarget.setVisibility(View.GONE);
                            setWeaklyTarget.setVisibility(View.VISIBLE);
                            exerciseTarget = String.valueOf(setWeaklyTarget.getText());
                            if(exerciseTarget==""){
                                Toast.makeText(ExerciseActivity.this, "You did not select any target option!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Double weaklyTarget = Double.parseDouble(exerciseTarget);
                                if (weaklyTarget > 1.0) {
                                    Toast.makeText(ExerciseActivity.this, "Invalid Target!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Double dailyTarget = (weaklyTarget * (3500.0 * 2.2)) / 7.0;
                                    String value = new DecimalFormat("##.#").format(dailyTarget);
                                    sPExerciseEditor.putString("target", value);
                                    sPExerciseEditor.commit();
                                }
                            }


                        } else {
                            Toast.makeText(ExerciseActivity.this, "You did not set any target!", Toast.LENGTH_SHORT).show();
                        }
                        setCalorie();
                        setBarChart();


                    }
                });
                exerciseTargetDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                exerciseTargetDialogBuilder.show();
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this,AddExerciseActivity.class);
                startActivity(intent);

            }
        });

        switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchReminder.isChecked()){
                    exerciseReminderLayout.setVisibility(View.VISIBLE);
                }
                else{
                    exerciseReminderLayout.setVisibility(View.GONE);
                }
            }
        });

}


    private void setBarChart() {
        Float todaysBurnedCalorie = sharedPreferencesExercise.getFloat("burnedCalorie", 0.0f);
        sPExerciseEditor.putFloat(dayOfTheWeek, todaysBurnedCalorie);
        sPExerciseEditor.commit();

        ArrayList<BarEntry> barEntries=new ArrayList<BarEntry>();
        barEntries.add(new BarEntry(0,sharedPreferencesExercise.getFloat("Sun", 0.0f)));
        barEntries.add(new BarEntry(1,sharedPreferencesExercise.getFloat("Mon", 0.0f)));
        barEntries.add(new BarEntry(2,sharedPreferencesExercise.getFloat("Tue", 0.0f)));
        barEntries.add(new BarEntry(3,sharedPreferencesExercise.getFloat("Wed", 0.0f)));
        barEntries.add(new BarEntry(4,sharedPreferencesExercise.getFloat("Thu", 0.0f)));
        barEntries.add(new BarEntry(5,sharedPreferencesExercise.getFloat("Fri", 0.0f)));
        barEntries.add(new BarEntry(6,sharedPreferencesExercise.getFloat("Sat", 0.0f)));

        barDataSetEntries = new BarDataSet(barEntries,"Exercise Level");
        barDataSetEntries.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSetEntries);



        exerciseBarChart.setData(barData);

        daysOfWeak = getResources().getStringArray(R.array.days_of_weak);
        XAxis xAxis = exerciseBarChart.getXAxis();
        xAxis.setValueFormatter(new xAxisValueFormater(daysOfWeak));

    }

    public class  xAxisValueFormater implements IAxisValueFormatter{

        private String[] mValues;
        public xAxisValueFormater(String[] values){
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setCalorie();
        setBarChart();
    }

    private void setCalorie() {

        Double burnableCal, burnedCal, burnedPrcnt;
        int progress;
        burnableCalorieVal = sharedPreferencesExercise.getString("target", "");
        burnedCalorieVal = sharedPreferencesExercise.getFloat("burnedCalorie", 0.0f);
        if(burnableCalorieVal=="") {
            burnableCalorie.setText("0");
        } else {
            burnableCalorie.setText(burnableCalorieVal);
            if(burnedCalorieVal==0.0){
                burnedCalorie.setText("0");
                calorieBurnProgressbar.setProgress(0);
            }
            else {
                burnedCalorie.setText(burnedCalorieVal.toString());
                burnableCal = Double.parseDouble(burnableCalorieVal);
                burnedCal = Double.parseDouble(burnedCalorieVal.toString());
                burnedPrcnt = (burnedCal/burnableCal)*100.0;
                progress = Integer.valueOf(burnedPrcnt.intValue());
                //if(progress>100)
                //    progress=100;
                calorieBurnProgressbar.setProgress(progress);
            }

        }

        //SharedPreferences.Editor sPTargetEditor = sPCalorieUpdate.edit();
    }
}
