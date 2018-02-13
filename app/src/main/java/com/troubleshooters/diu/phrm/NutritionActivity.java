package com.troubleshooters.diu.phrm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.Adapter.NutritionCountAdapter;
import com.troubleshooters.diu.phrm.Helper.LocaleHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.paperdb.Paper;

public class NutritionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    ListView listView;
    LinearLayout linearLayout;
    Switch switch_reminder;
    TextView add_meal;
    TextView breakfast,lunch,snake,dinner;
    RelativeLayout Rbreakfast,Rlunch,Rsnack,Rdinner;
    int id;




    int hour,munite;
    Double necessary_calorie;
    public static  Activity nutrition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        setTitle(getString(R.string.nutration_plan_title));
        nutrition=this;


        final String[] nutrition_name=getResources().getStringArray(R.array.nutrition_items);
        final String[] gain_nutrition=getResources().getStringArray(R.array.gained_and_necessary_nutrition);
        final String[] necessary_nutrition=getResources().getStringArray(R.array.gained_and_necessary_nutrition);
        final String[] nutrition_unit={"cal","gm","gm","gm"};


        listView=(ListView) findViewById(R.id.list_view_nutrition);
        switch_reminder=(Switch)findViewById(R.id.switch_reminder);
        linearLayout=(LinearLayout)findViewById(R.id.reminder_item_linear_layout);
        add_meal=(TextView)findViewById(R.id.text_view_add_meal);
        breakfast=(TextView)findViewById(R.id.breakfast);
        lunch=(TextView)findViewById(R.id.lunch);
        snake=(TextView)findViewById(R.id.snake);
        dinner=(TextView)findViewById(R.id.dinner);

        Rbreakfast=(RelativeLayout)findViewById(R.id.relative_layout_breakfast);
        Rlunch=(RelativeLayout)findViewById(R.id.relative_layout_lunch);
        Rsnack=(RelativeLayout)findViewById(R.id.relative_layout_snack);
        Rdinner=(RelativeLayout)findViewById(R.id.relative_layout_dinner);





        //Check profile id fully updated
        SharedPreferences sharedPreferences_checker=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        if(sharedPreferences_checker.getString("birthday","").equals("")
                ||sharedPreferences_checker.getString("activity","").equals("")
                ||sharedPreferences_checker.getString("height","").equals("")
                ||sharedPreferences_checker.getString("weight","").equals("")
                ||sharedPreferences_checker.getString("gender","").equals(""))
        {

            android.support.v7.app.AlertDialog.Builder builder;
            builder = new android.support.v7.app.AlertDialog.Builder(NutritionActivity.this, R.style.CustomDialogTheme);
            builder.setMessage("Please update your profile first!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent=new Intent(NutritionActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).show();
        }
        else{
            //Update profile
            setCalorie();
            setFat();
            setCarbohydrate();
            setProtein();
        }





        //Reset gain value 12:00
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY ,24);
        calendar.set(Calendar.MINUTE,0);
        Intent intent=new Intent(getApplicationContext(),ResetApp.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),1000,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);











        //Move to add meal activity
        add_meal.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent=new Intent(NutritionActivity.this,AddMealActivity.class);
                startActivity(intent);
                }
            }
        );


        NutritionCountAdapter adapter=new NutritionCountAdapter(nutrition_name,gain_nutrition,necessary_nutrition,nutrition_unit,NutritionActivity.this);
        listView.setAdapter(adapter);







        //on off reminder layout visiblity
        final SharedPreferences reminder_status=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        SharedPreferences reminder_time=getSharedPreferences("time",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=reminder_status.edit();
        if(reminder_status.getString("reminder switch status","").equals("")||reminder_status.getString("reminder switch status","").equals("off"))
        {
            reminder_time.edit().clear().commit();
            switch_reminder.setChecked(false);
            linearLayout.setVisibility(View.GONE);
        }
        else{
            switch_reminder.setChecked(true);
        }







        //switch_reminder.setChecked(true);
        switch_reminder.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(switch_reminder.isChecked())
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            editor.putString("reminder switch status","on");
                            editor.commit();
                        }
                        else
                        {
                            linearLayout.setVisibility(View.GONE);
                            editor.putString("reminder switch status","off");
                            editor.commit();
                            SharedPreferences reminder_time=getSharedPreferences("time",Context.MODE_PRIVATE);
                            reminder_time.edit().clear().commit();

                            breakfast.setText(R.string._meal_reminder);
                            lunch.setText(R.string._meal_reminder);
                            snake.setText(R.string._meal_reminder);
                            dinner.setText(R.string._meal_reminder);

                            AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                            PendingIntent pendingIntent1=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingIntent2=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingIntent3=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingIntent4=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            aManager.cancel(pendingIntent1);
                            aManager.cancel(pendingIntent2);
                            aManager.cancel(pendingIntent3);
                            aManager.cancel(pendingIntent4);

                        }
                    }
                }
        );



        //updating meal  alarm time in display every time appear in nutrition activity
        String hour_s;
        String minute_s;
        final SharedPreferences sharedPreferences=getSharedPreferences("time", Context.MODE_PRIVATE);
        final SharedPreferences.Editor meal_reminder_editor=sharedPreferences.edit();
        hour_s=sharedPreferences.getString("breakfast_hour","");
        minute_s=sharedPreferences.getString("breakfast_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.parseInt(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            breakfast.setText(hour_s+":"+minute_s+" "+AMorPM);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("lunch_hour","");
        minute_s=sharedPreferences.getString("lunch_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.parseInt(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            lunch.setText(hour_s+":"+minute_s+" "+AMorPM);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("snake_hour","");
        minute_s=sharedPreferences.getString("snake_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.parseInt(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            snake.setText(hour_s+":"+minute_s+" "+AMorPM);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("dinner_hour","");
        minute_s=sharedPreferences.getString("dinner_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            String AMorPM="AM";
            if(Integer.valueOf(hour_s)>12)
            {
                Integer value=Integer.parseInt(hour_s)-12;
                hour_s=value.toString();
                AMorPM="PM";
            }
            if(Integer.valueOf(minute_s)<10)
            {
                minute_s="0"+minute_s;
            }
            dinner.setText(hour_s+":"+minute_s+" "+AMorPM);
        }


        //canceling meal reminder
        Rbreakfast.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if(!sharedPreferences.getString("breakfast_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(NutritionActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Cancel your reminder for breakfast!")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            breakfast.setText(R.string.click_icon_to_set_time_medication);
                                            meal_reminder_editor.putString("breakfast_hour","");
                                            meal_reminder_editor.putString("breakfast_minute","");
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
        Rlunch.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!sharedPreferences.getString("lunch_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(NutritionActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Cancel your reminder for lunch!")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            lunch.setText(R.string.click_icon_to_set_time_medication);
                                            meal_reminder_editor.putString("lunch_hour","");
                                            meal_reminder_editor.putString("lunch_minute","");
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
        Rsnack.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!sharedPreferences.getString("snack_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(NutritionActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Cancel your reminder for snack!")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            snake.setText(R.string.click_icon_to_set_time_medication);
                                            meal_reminder_editor.putString("snack_hour","");
                                            meal_reminder_editor.putString("snack_minute","");
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
        Rdinner.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!sharedPreferences.getString("dinner_hour","").equals(""))
                        {
                            android.support.v7.app.AlertDialog.Builder builder;
                            builder = new android.support.v7.app.AlertDialog.Builder(NutritionActivity.this, R.style.CustomDialogTheme);
                            builder.setMessage("Do you really want to cancel your reminder for dinner!")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            dinner.setText(R.string.click_icon_to_set_time_medication);
                                            meal_reminder_editor.putString("dinner_hour","");
                                            meal_reminder_editor.putString("dinner_minute","");
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


        //Setting up alarm
        breakfast.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        munite=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(NutritionActivity.this,t_breakfast,hour,munite,true);
                        timePickerDialog.show();

                    }
                }
        );

        lunch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        munite=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(NutritionActivity.this,t_lunch,hour,munite,true);
                        timePickerDialog.show();

                    }
                }
        );

        snake.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        munite=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(NutritionActivity.this,t_snake,hour,munite,true);
                        timePickerDialog.show();

                    }
                }
        );

        dinner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        munite=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(NutritionActivity.this,t_dinner,hour,munite,true);
                        timePickerDialog.show();

                    }
                }
        );
    }




    //generate time picker saving reminder time in sharedpreference and settinng time in text view
    TimePickerDialog.OnTimeSetListener t_breakfast=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("snake_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }


            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("breakfast_hour", String.valueOf(hourOfDay));
            editor.putString("breakfast_minute", String.valueOf(minute));
            editor.commit();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",101);
            intent.putExtra("text","Time to Breakfast");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
            breakfast.setText(hourOfDay+":"+minuteS+" "+AMorPM);

        }
    };

    TimePickerDialog.OnTimeSetListener t_lunch=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("snake_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("lunch_hour", String.valueOf(hourOfDay));
            editor.putString("lunch_minute", String.valueOf(minute));
            editor.commit();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",102);
            intent.putExtra("text","Time for Lunch");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
            lunch.setText(hourOfDay+":"+minuteS+" "+AMorPM);
        }
    };
    TimePickerDialog.OnTimeSetListener t_snake=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("snake_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("snake_hour", String.valueOf(hourOfDay));
            editor.putString("snake_minute", String.valueOf(minute));
            editor.commit();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",103);
            intent.putExtra("text","Time for Snack");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
            snake.setText(hourOfDay+":"+minuteS+" "+AMorPM);
        }
    };
    TimePickerDialog.OnTimeSetListener t_dinner=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            if(!sharedPreferences.getString("dinner_hour","").equals(""))
            {
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("dinner_hour", String.valueOf(hourOfDay));
            editor.putString("dinner_minute", String.valueOf(minute));
            editor.commit();

            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar currentTime=Calendar.getInstance();
            calendar.setTime(date);
            currentTime.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            if(calendar.before(currentTime))
            {
                calendar.add(Calendar.DATE,1);
            }
            Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
            intent.putExtra("NID",104);
            intent.putExtra("text","Time to Dinner");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
            dinner.setText(hourOfDay+":"+minuteS+" "+AMorPM);
        }
    };


    //default time picker dialog
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    //updating necessaty Nutrition and saving it in shared preference
    public void setCalorie()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        String age=sharedPreferences.getString("age","");
        String height=sharedPreferences.getString("height","");
        String weight=sharedPreferences.getString("weight","");
        String gender=sharedPreferences.getString("gender","");
        String activity=sharedPreferences.getString("activity","");
        if(gender.equals("Male"))
        {
            necessary_calorie=(10*Double.parseDouble(weight))+(6.25*Double.parseDouble(height))-(5*Double.parseDouble(age))+5;

        }
        if(gender.equals("Female"))
        {
            necessary_calorie=(10*Double.parseDouble(weight))+(6.25*Double.parseDouble(height))-(5*Double.parseDouble(age))-161;
        }
        if(activity.equals("Little"))
        {
            necessary_calorie=necessary_calorie*1.2;
        }
        else if(activity.equals("Light"))
        {
            necessary_calorie=necessary_calorie*1.375;
        }
        else if(activity.equals("Moderate"))
        {
            necessary_calorie=necessary_calorie*1.55;
        }
        else if(activity.equals("Heavy"))
        {
            necessary_calorie=necessary_calorie*1.725;
        }
        else if(activity.equals("very heavy"))
        {
            necessary_calorie=necessary_calorie*1.9;
        }
        SharedPreferences sharedPreferences1=getSharedPreferences("nutrition", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences1.edit();
        String value=new DecimalFormat("##.#").format(necessary_calorie);
        editor.putString("necessary_calorie",value);
        editor.commit();
    }
    public void setCarbohydrate()
    {
        Double value=(necessary_calorie*(65.0/100.0))/4.0;
        SharedPreferences sharedPreferences=getSharedPreferences("nutrition",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String necessary_carbohydrate=new DecimalFormat("##.#").format(value);
        editor.putString("necessary_carbohydrate",necessary_carbohydrate.toString());
        editor.commit();
    }
    public void setFat()
    {
        Double value=(necessary_calorie*(30.0/100.0))/9.0;
        SharedPreferences sharedPreferences=getSharedPreferences("nutrition",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String necessary_fat=new DecimalFormat("##.#").format(value);
        editor.putString("necessary_fat",necessary_fat.toString());
        editor.commit();
    }
    public void setProtein()
    {
        if(necessary_calorie!=0)
        {
            Double value;
            SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
            String weight=sharedPreferences.getString("weight","");
            value=Double.parseDouble(weight)*1.5;
            String necessary_protein=new DecimalFormat("##.#").format(value);
            SharedPreferences sharedPreferences1=getSharedPreferences("nutrition",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences1.edit();
            editor.putString("necessary_protein",necessary_protein);
            editor.commit();
        }
    }
}


