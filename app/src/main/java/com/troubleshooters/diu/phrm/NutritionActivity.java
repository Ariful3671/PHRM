package com.troubleshooters.diu.phrm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.troubleshooters.diu.phrm.Adapter.NutritionCountAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NutritionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    GridView gridView;
    LinearLayout linearLayout;
    Switch switch_reminder;
    TextView add_meal;
    TextView gained_cal;
    TextView breakfast,lunch,snake,dinner;

    Double necessary_calorie=0.0;
    int hour,munite;
    String nutrition_name[]={"Carbohydrate","Fat","Protein"};
    String gain_nutrition[]={"0","0","0","0"};
    String necessary_nutrition[]={"0","0","0","0"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        setTitle("Nutrition plan");

        gridView=(GridView)findViewById(R.id.grid_view_home);
        switch_reminder=(Switch)findViewById(R.id.switch_reminder);
        linearLayout=(LinearLayout)findViewById(R.id.reminder_item_linear_layout);
        add_meal=(TextView)findViewById(R.id.text_view_add_meal);
        gained_cal=(TextView)findViewById(R.id.gained_cal);
        breakfast=(TextView)findViewById(R.id.breakfast);
        lunch=(TextView)findViewById(R.id.lunch);
        snake=(TextView)findViewById(R.id.snake);
        dinner=(TextView)findViewById(R.id.dinner);
        //NutritionCountAdapter adapter=new NutritionCountAdapter(nutrition_name,gain_nutrition,necessary_nutrition,NutritionActivity.this);
        //gridView.setAdapter(adapter);
        switch_reminder.setChecked(true);
        switch_reminder.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(switch_reminder.isChecked())
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                }
        );
        /*setCalorie();
        setFat();
        setCarbohydrate();
        setProtein();
        update();*/
        /*add_meal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(NutritionActivity.this,AddMealActivity.class);
                        startActivity(intent);
                    }
                }
        );*/
        String hour_s;
        String minute_s;
        SharedPreferences sharedPreferences=getSharedPreferences("time", Context.MODE_PRIVATE);
        hour_s=sharedPreferences.getString("breakfast_hour","");
        minute_s=sharedPreferences.getString("breakfast_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            breakfast.setText(hour_s+":"+minute_s);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("lunch_hour","");
        minute_s=sharedPreferences.getString("lunch_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            lunch.setText(hour_s+":"+minute_s);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("snake_hour","");
        minute_s=sharedPreferences.getString("snake_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            snake.setText(hour_s+":"+minute_s);
        }
        hour_s="";
        minute_s="";
        hour_s=sharedPreferences.getString("dinner_hour","");
        minute_s=sharedPreferences.getString("dinner_minute","");
        if(!hour_s.equals("")&&!minute_s.equals(""))
        {
            dinner.setText(hour_s+":"+minute_s);
        }
        hour_s="";
        minute_s="";

        /*Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY ,3);
        calendar.set(Calendar.MINUTE,41);
        Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);*/




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



    TimePickerDialog.OnTimeSetListener t_breakfast=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("breakfast_hour", String.valueOf(hourOfDay));
            editor.putString("breakfast_minute", String.valueOf(minute));
            editor.commit();
            breakfast.setText(hourOfDay+":"+minute);
        }
    };

    TimePickerDialog.OnTimeSetListener t_lunch=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("lunch_hour", String.valueOf(hourOfDay));
            editor.putString("lunch_minute", String.valueOf(minute));
            editor.commit();
            lunch.setText(hourOfDay+":"+minute);
        }
    };
    TimePickerDialog.OnTimeSetListener t_snake=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("snake_hour", String.valueOf(hourOfDay));
            editor.putString("snake_minute", String.valueOf(minute));
            editor.commit();
            snake.setText(hourOfDay+":"+minute);
        }
    };
    TimePickerDialog.OnTimeSetListener t_dinner=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPreferences=getSharedPreferences("time",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("dinner_hour", String.valueOf(hourOfDay));
            editor.putString("dinner_minute", String.valueOf(minute));
            editor.commit();
            dinner.setText(hourOfDay+":"+minute);
        }
    };

    public void update()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("Nutrition",Context.MODE_PRIVATE);
        String gained_calorie=sharedPreferences.getString("gained_calorie","");
        String gained_fat=sharedPreferences.getString("gained_fat","");
        String gained_carbohydrate=sharedPreferences.getString("gained_carbohydrate","");
        String gained_protein=sharedPreferences.getString("gained_protein","");

        if(!gained_calorie.equals("")||!gained_carbohydrate.equals("")||!gained_fat.equals("")||gained_protein.equals(""))
        {
            if(gained_calorie=="")
            {
                gained_cal=(TextView)findViewById(R.id.gained_cal);
                gained_cal.setText("0");
            }
            else
            {
                gained_cal=(TextView)findViewById(R.id.gained_cal);
                gained_cal.setText(gained_calorie);
            }

            NutritionCountAdapter adapter=new NutritionCountAdapter(nutrition_name,gain_nutrition,necessary_nutrition,NutritionActivity.this);
            adapter.notifyDataSetChanged();
            gridView.setAdapter(adapter);
        }

    }
    public void setCalorie()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("ProfileData", Context.MODE_PRIVATE);
        String age=sharedPreferences.getString("age","");
        String height=sharedPreferences.getString("height","");
        String weight=sharedPreferences.getString("weight","");
        String gender=sharedPreferences.getString("gender","");
        String activity=sharedPreferences.getString("activity level","");
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
        SharedPreferences sharedPreferences1=getSharedPreferences("Nutrition", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences1.edit();
        TextView TV_ness_cal=(TextView)findViewById(R.id.necessary_cal);
        String value=new DecimalFormat("##.##").format(necessary_calorie);
        editor.putString("necessary_calorie",value);
        editor.commit();
        TV_ness_cal.setText("of "+necessary_calorie.toString()+"Kcal");

    }
    public void setCarbohydrate()
    {
        Double value=(necessary_calorie*(65.0/100.0))/4.0;
        SharedPreferences sharedPreferences=getSharedPreferences("Nutrition",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String necessary_carbohydrate=new DecimalFormat("##.##").format(value);
        editor.putString("necessary_carbohydrate",necessary_carbohydrate.toString());
        editor.commit();
        NutritionCountAdapter adapter=new NutritionCountAdapter(nutrition_name,gain_nutrition,necessary_nutrition,NutritionActivity.this);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }
    public void setFat()
    {
        Double value=(necessary_calorie*(30.0/100.0))/9.0;
        SharedPreferences sharedPreferences=getSharedPreferences("Nutrition",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String necessary_fat=new DecimalFormat("##.##").format(value);
        editor.putString("necessary_fat",necessary_fat.toString());
        editor.commit();
        NutritionCountAdapter adapter=new NutritionCountAdapter(nutrition_name,gain_nutrition,necessary_nutrition,NutritionActivity.this);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }

    public void setProtein()
    {
        if(necessary_calorie!=0)
        {
            Double value;
            SharedPreferences sharedPreferences=getSharedPreferences("ProfileData",Context.MODE_PRIVATE);
            String weight=sharedPreferences.getString("weight","");
            value=Double.parseDouble(weight)*1.5;
            String necessary_protein=new DecimalFormat("##.##").format(value);
            SharedPreferences sharedPreferences1=getSharedPreferences("Nutrition",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences1.edit();
            editor.putString("necessary_protein",necessary_protein);
            editor.commit();
        }

        NutritionCountAdapter adapter=new NutritionCountAdapter(nutrition_name,gain_nutrition,necessary_nutrition,NutritionActivity.this);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.nutrition_activity_menu,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.item_profile)
        {
            Intent intent=new Intent(NutritionActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.item_refresh)
        {
            Intent intent=new Intent(NutritionActivity.this,HomeActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        return true;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}


