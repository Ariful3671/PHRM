package com.troubleshooters.diu.phrm;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class StartExercise extends AppCompatActivity {

    ProgressBar runningExerciseProgress;
    TextView runningExerciseTimer;
    Button startExerciseBtn;
    int timerDuration;
    int h, m, s;
    int progressTemp;
    int progress;
    boolean timerOn;
    String ma, sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);
        runningExerciseProgress = (ProgressBar)findViewById(R.id.start_exercise_progressbar);
        runningExerciseTimer = (TextView)findViewById(R.id.tv_exercise_timer);
        startExerciseBtn = (Button)findViewById(R.id.exercise_timer_button);
        h=0; m=0; s=0;
        Bundle extras = getIntent().getExtras();
        final int duration = extras.getInt("duration");
        timerDuration = duration*60*1000;
        progressTemp=0;
        timerOn=false;


        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerOn){
                    timerOn=true;
                    startExerciseBtn.setText("Start");
                }
                else {
                    startExerciseBtn.setText("Stop");
                    CountDownTimer countDownTimer = new CountDownTimer(timerDuration, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            s++;
                            progressTemp=s;
                            progress = (timerDuration/progressTemp)*100;
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
                            runningExerciseProgress.setProgress(progress);
                        }

                        @Override
                        public void onFinish() {
                            timerOn=false;
                            startExerciseBtn.setText("Start");
                        }
                    }.start();
                }
            }
        });

    }
}
