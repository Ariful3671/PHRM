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
import android.widget.RelativeLayout;
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

public class ExerciseActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    TextView burnedCalorie, burnableCalorie;
    ProgressBar calorieBurnProgressbar;
    BarChart exerciseBarChart;
    TextView addExercise, addTarget;
    Switch switchReminder;
    EditText exerciseReminder1, exerciseReminder2, exerciseReminder3;
    TextView setTime1,setTime2, setTime3;
    LinearLayout exerciseReminderLayout;
    RelativeLayout reminderlayout1,reminderlayout2,reminderlayout3;

    SharedPreferences sharedPreferencesExercise;
    SharedPreferences.Editor sPExerciseEditor;

    String burnableCalorieVal;
    Float burnedCalorieVal;

    BarDataSet barDataSetEntries;
    String[] daysOfWeak;
    int hour,minute;

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
        reminderlayout1=(RelativeLayout)findViewById(R.id.exercise_reminder1);
        reminderlayout2=(RelativeLayout)findViewById(R.id.exercise_reminder2);
        reminderlayout3=(RelativeLayout)findViewById(R.id.exercise_reminder3);
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




        final SharedPreferences reminder_status=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        SharedPreferences reminder_time=getSharedPreferences("timeExercise",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=reminder_status.edit();
        if(reminder_status.getString("exercise reminder switch status","").equals("")||reminder_status.getString("exercise reminder switch status","").equals("off"))
        {
            reminder_time.edit().clear().commit();
            switchReminder.setChecked(false);
            exerciseReminderLayout.setVisibility(View.GONE);
        }
        else{
            exerciseReminderLayout.setVisibility(View.VISIBLE);
            switchReminder.setChecked(true);
        }







        switchReminder.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(switchReminder.isChecked())
                        {
                            exerciseReminderLayout.setVisibility(View.VISIBLE);
                            editor.putString("exercise reminder switch status","on");
                            editor.commit();
                        }
                        else
                        {
                            exerciseReminderLayout.setVisibility(View.GONE);
                            editor.putString("exercise reminder switch status","off");
                            editor.commit();
                            SharedPreferences reminder_time=getSharedPreferences("timeExercise",Context.MODE_PRIVATE);
                            reminder_time.edit().clear().commit();
                            setTime1.setText("Set time");
                            setTime2.setText("Set time");
                            setTime3.setText("Set time");
                            AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                            PendingIntent pendingIntent1=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingIntent2=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingIntent3=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            aManager.cancel(pendingIntent1);
                            aManager.cancel(pendingIntent2);
                            aManager.cancel(pendingIntent3);
                        }
                    }
                }
        );







        //Update the exercise alarm display when Exercise activity appear..................................
        String hour_s;
        String minute_s;
        final SharedPreferences sharedPreferences=getSharedPreferences("timeExercise", Context.MODE_PRIVATE);
        final SharedPreferences.Editor meal_reminder_editor=sharedPreferences.edit();
        hour_s=sharedPreferences.getString("exercise_first_hour","");
        minute_s=sharedPreferences.getString("exercise_first_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.valueOf(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            setTime1.setText(hour_s+":"+minute_s+" "+AMorPM);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("exercise_second_hour","");
        minute_s=sharedPreferences.getString("exercise_second_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.valueOf(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            setTime2.setText(hour_s+":"+minute_s+" "+AMorPM);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("exercise_third_hour","");
        minute_s=sharedPreferences.getString("exercise_third_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.valueOf(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            setTime3.setText(hour_s+":"+minute_s+" "+AMorPM);
        }
        //Updating alarm textview ends here............................



        //Setting up onclick listener for exercise reminder...............................................
        Calendar c=Calendar.getInstance();
        setTime1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ExerciseActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        minute=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(ExerciseActivity.this,reminder1,hour,minute,true);
                        timePickerDialog.show();
                    }
                }
        );
        setTime2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        minute=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(ExerciseActivity.this,reminder2,hour,minute,true);
                        timePickerDialog.show();
                    }
                }
        );
        setTime3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        minute=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(ExerciseActivity.this,reminder3,hour,minute,true);
                        timePickerDialog.show();
                    }
                }
        );
        //On click listener for time picker ends here.......................................








        //Delete Reminder
        reminderlayout1.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!sharedPreferences.getString("exercise_first_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(ExerciseActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Cancel your reminder for "+exerciseReminder1.getHint().toString()+" !")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            setTime1.setText("Set time");
                                            meal_reminder_editor.putString("exercise_first_hour","");
                                            meal_reminder_editor.putString("exercise_first_minute","");
                                            meal_reminder_editor.commit();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                        }
                        return true;
                    }
                }
        );
        reminderlayout2.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!sharedPreferences.getString("exercise_second_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(ExerciseActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Cancel your reminder for "+exerciseReminder2.getText().toString()+" !")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            setTime2.setText("Set time");
                                            meal_reminder_editor.putString("exercise_second_hour","");
                                            meal_reminder_editor.putString("exercise_second_minute","");
                                            meal_reminder_editor.commit();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                        }
                        return true;
                    }
                }
        );
        reminderlayout3.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!sharedPreferences.getString("exercise_third_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(ExerciseActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Cancel your reminder for "+exerciseReminder3.getHint().toString()+" !")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            setTime3.setText("Set time");
                                            meal_reminder_editor.putString("exercise_third_hour","");
                                            meal_reminder_editor.putString("exercise_third_minute","");
                                            meal_reminder_editor.commit();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                        }
                        return true;
                    }
                }
        );



}//onCreate method ends..............



//Code for time picker of exercise 1 2 and 3..............................................................
    TimePickerDialog.OnTimeSetListener reminder1=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences sharedPreferences=getSharedPreferences("timeExercise",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("exercise_first_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }


            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("exercise_first_hour", String.valueOf(hourOfDay));
            editor.putString("exercise_first_minute", String.valueOf(minute));
            editor.commit();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            //Toast.makeText(ExerciseActivity.this, String.valueOf(hourOfDay), Toast.LENGTH_SHORT).show();
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",105);
            intent.putExtra("text","Time to Exercise1");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);

            String minuteS=String.valueOf(minute);
            String AMorPM="AM";
            if(hourOfDay>12)
            {
                Integer value=hourOfDay-12;
                hourOfDay=value;
                AMorPM="PM";

            }
            if(minute<10)
            {
                minuteS="0"+String.valueOf(minute);
            }
            setTime1.setText(hourOfDay+":"+minuteS+" "+AMorPM);

        }
    };


    TimePickerDialog.OnTimeSetListener reminder2=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences sharedPreferences=getSharedPreferences("timeExercise",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("exercise_second_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }


            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("exercise_second_hour", String.valueOf(hourOfDay));
            editor.putString("exercise_second_minute", String.valueOf(minute));
            editor.commit();

            //Toast.makeText(ExerciseActivity.this, sharedPreferences.getString("exercise_second",""), Toast.LENGTH_SHORT).show();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            //Toast.makeText(ExerciseActivity.this, String.valueOf(hourOfDay), Toast.LENGTH_SHORT).show();
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",106);
            intent.putExtra("text","Time to Exercise2");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);

            String minuteS=String.valueOf(minute);
            String AMorPM="AM";
            if(hourOfDay>12)
            {
                Integer value=hourOfDay-12;
                hourOfDay=value;
                AMorPM="PM";

            }
            if(minute<10)
            {
                minuteS="0"+String.valueOf(minute);
            }
            setTime2.setText(hourOfDay+":"+minuteS+" "+AMorPM);

        }
    };



    TimePickerDialog.OnTimeSetListener reminder3=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences sharedPreferences=getSharedPreferences("timeExercise",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("exercise_third_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }


            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("exercise_third_hour", String.valueOf(hourOfDay));
            editor.putString("exercise_third_minute", String.valueOf(minute));
            editor.commit();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            //Toast.makeText(ExerciseActivity.this, String.valueOf(hourOfDay), Toast.LENGTH_SHORT).show();
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",107);
            intent.putExtra("text","Time to Exercise3");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);

            String minuteS=String.valueOf(minute);
            String AMorPM="AM";
            if(hourOfDay>12)
            {
                Integer value=hourOfDay-12;
                hourOfDay=value;
                AMorPM="PM";

            }
            if(minute<10)
            {
                minuteS="0"+String.valueOf(minute);
            }
            setTime3.setText(hourOfDay+":"+minuteS+" "+AMorPM);

        }
    };
    //Time picker code end s here.....................................................








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






    //Default Time set Listener
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

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
