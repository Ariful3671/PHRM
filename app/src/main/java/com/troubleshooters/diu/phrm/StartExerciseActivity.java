package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class StartExerciseActivity extends AppCompatActivity {

    ProgressBar runningExerciseProgress;
    TextView runningExerciseTimer;
    TextView exerciseDetails;
    Button startExerciseBtn, addBtn;
    int timerDuration, durationTemp;
    int h, m, s;
    int progressTemp;
    boolean timerOn;
    Double weightVal, metVal;
    String ma, sa;
    CountDownTimer countDownTimer;

    SharedPreferences sharedPreferencesExercise;
    SharedPreferences.Editor sPExerciseEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);
        runningExerciseProgress = (ProgressBar)findViewById(R.id.start_exercise_progressbar);
        runningExerciseTimer = (TextView)findViewById(R.id.tv_exercise_timer);
        exerciseDetails = (TextView)findViewById(R.id.exercise_details);
        startExerciseBtn = (Button)findViewById(R.id.exercise_timer_button);
        addBtn = (Button)findViewById(R.id.exercise_add_btn);
        weightVal = Double.parseDouble(getIntent().getStringExtra("weight"));
        metVal = Double.parseDouble(getIntent().getStringExtra("met"));
        progressTemp=0;
        sharedPreferencesExercise = getSharedPreferences("exercise", Context.MODE_PRIVATE);
        sPExerciseEditor = sharedPreferencesExercise.edit();
        h=0; m=0; s=1;
        final int duration = sharedPreferencesExercise.getInt("exercise_duration", 0);
        exerciseDetails.setText(getIntent().getStringExtra("exerciseDetail"));

        timerDuration = duration*60;
        durationTemp=timerDuration;
        runningExerciseProgress.setMax(timerDuration);
        timerOn=false;


        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerOn){
                    timerOn=false;
                    countDownTimer.cancel();
                    startExerciseBtn.setText("Restart");
                    addBtn.setVisibility(View.VISIBLE);
                }
                else {
                    try{
                        startExerciseBtn.setText("Pause");
                        addBtn.setVisibility(View.GONE);
                        timerOn=true;
                        countDownTimer = new CountDownTimer((timerDuration*1000), 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                s++;
                                timerDuration--;
                                progressTemp+=1;

                                if(s<10)
                                    sa="0";
                                else
                                    sa="";

                                if(m<10)
                                    ma="0";
                                else
                                    ma="";

                                if(s==60){
                                    m++;
                                    s=0;
                                }
                                if(m==60){
                                    h++;
                                    m=0;
                                }
                                runningExerciseTimer.setText("0"+h+":"+ma+m+":"+sa+s);
                                runningExerciseProgress.setProgress(progressTemp);
                            }

                            @Override
                            public void onFinish() {
                                timerOn=false;
                                startExerciseBtn.setText("Start");
                                addBtn.setVisibility(View.VISIBLE);
                                startExerciseBtn.setVisibility(View.GONE);
                                Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(2000);
                                try {
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }.start();
                    }catch (Exception e){
                        Toast.makeText(StartExerciseActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double duration = Double.valueOf((durationTemp-timerDuration)/60.0);
                //duration = duration/60;
                if(duration>1){
                    Double burnedCal = 0.0175*metVal*weightVal*duration-(0.0175*weightVal*duration);
                    String value=new DecimalFormat("##.#").format(burnedCal);
                    Toast.makeText(StartExerciseActivity.this, "You burned "+value+" calories", Toast.LENGTH_LONG).show();
                    Float previouslyBurnedCalorie = sharedPreferencesExercise.getFloat("burnedCalorie", 0.0f);
                    if(previouslyBurnedCalorie!=0.0){
                        Double pBurnedCalorie = new Double(previouslyBurnedCalorie);
                        burnedCal += pBurnedCalorie;
                    }
                    Float value1=new Float(burnedCal);
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    Float formatedVal = Float.valueOf(decimalFormat.format(value1));
                    sPExerciseEditor.putFloat("burnedCalorie", formatedVal);
                    sPExerciseEditor.commit();
                    finish();
                }
                else{
                    Toast.makeText(StartExerciseActivity.this, "Go on! You exercised too little!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
