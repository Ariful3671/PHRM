package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Pedometer extends AppCompatActivity implements SensorEventListener, StepListener {

    private TextView TvSteps, TvTimer, TvpedoCalorie;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private CountDownTimer countDownTimer;
    private int numSteps;
    private Button BtnStart, BtnStop, BtnReset;
    int h, m, s;
    Double burnedCal;
    Double weightVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
        setTitle("Pedometer");


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        TvTimer = (TextView)findViewById(R.id.tv_pedo_time);
        TvpedoCalorie = (TextView) findViewById(R.id.tv_pedo_calorie);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnReset = (Button)findViewById(R.id.btn_reset);

        numSteps = 0;

        BtnStart.setVisibility(View.VISIBLE);
        BtnStop.setVisibility(View.GONE);

        SharedPreferences sharedPreferencesPI=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        String weight = sharedPreferencesPI.getString("weight", "");
        weightVal = Double.parseDouble(weight);
        burnedCal = 0.0;

        h=0; m=0; s=0;

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                BtnStart.setVisibility(View.GONE);
                BtnStop.setVisibility(View.VISIBLE);
                BtnReset.setVisibility(View.GONE);

                sensorManager.registerListener(Pedometer.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                countDownTimer = new CountDownTimer(5400000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        s++;
                        if(s==60){
                            m++;
                            s=0;
                            burnedCal += 0.0175*3.5*weightVal-(0.0175*weightVal);
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            Double formatedVal = Double.valueOf(decimalFormat.format(burnedCal));
                            TvpedoCalorie.setText(formatedVal.toString()+"Cal");
                        }
                        if(m==60){
                            h++;
                            m=0;
                        }
                        TvTimer.setText(h+":"+m+":"+s);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BtnStart.setVisibility(View.VISIBLE);
                BtnStart.setText("Restart");
                BtnStop.setVisibility(View.GONE);
                BtnReset.setVisibility(View.VISIBLE);
                sensorManager.unregisterListener(Pedometer.this);
                countDownTimer.cancel();

            }
        });

        BtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSteps =0;
                burnedCal = 0.0;
                h=0; m=0; s=0;
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });



    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(""+numSteps);
    }

    private class StepDetector {
        private static final int ACCEL_RING_SIZE = 50;
        private static final int VEL_RING_SIZE = 10;

        // change this threshold according to your sensitivity preferences
        private static final float STEP_THRESHOLD = 35f;

        private static final int STEP_DELAY_NS = 250000000;

        private int accelRingCounter = 0;
        private float[] accelRingX = new float[ACCEL_RING_SIZE];
        private float[] accelRingY = new float[ACCEL_RING_SIZE];
        private float[] accelRingZ = new float[ACCEL_RING_SIZE];
        private int velRingCounter = 0;
        private float[] velRing = new float[VEL_RING_SIZE];
        private long lastStepTimeNs = 0;
        private float oldVelocityEstimate = 0;

        private StepListener listener;

        public void registerListener(StepListener listener) {
            this.listener = listener;
        }


        public void updateAccel(long timeNs, float x, float y, float z) {
            float[] currentAccel = new float[3];
            currentAccel[0] = x;
            currentAccel[1] = y;
            currentAccel[2] = z;

            // First step is to update our guess of where the global z vector is.
            accelRingCounter++;
            accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
            accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
            accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

            float[] worldZ = new float[3];
            worldZ[0] = SensorFilter.sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
            worldZ[1] = SensorFilter.sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
            worldZ[2] = SensorFilter.sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

            float normalization_factor = SensorFilter.norm(worldZ);

            worldZ[0] = worldZ[0] / normalization_factor;
            worldZ[1] = worldZ[1] / normalization_factor;
            worldZ[2] = worldZ[2] / normalization_factor;

            float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
            velRingCounter++;
            velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

            float velocityEstimate = SensorFilter.sum(velRing);

            if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD
                    && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
                listener.step(timeNs);
                lastStepTimeNs = timeNs;
            }
            oldVelocityEstimate = velocityEstimate;
        }
    }
}
