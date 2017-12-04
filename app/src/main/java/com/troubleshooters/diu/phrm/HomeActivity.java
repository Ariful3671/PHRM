package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.troubleshooters.diu.phrm.Adapter.TestRecordGridAdapter;


public class HomeActivity extends AppCompatActivity {


    String grid_daily_routin_text[]={"Nutrition","Exercise","Medication"};
    String grid_test_record_text[]={"Blood Pressure","Glucose","Manage Items"};
    int grid_test_record_icon[]={R.drawable.blood_pressure_icon,R.drawable.glucose_test_icon,R.drawable.add};
    int grid_daily_routin_icon[]={R.drawable.nutrition_icon,R.drawable.exercise_icon,R.drawable.medication_icon};
    String grid_daily_routin_button_status[]={"CREATE","UPDATE"};
    String grid_record_test_button_status[]={"RECORD","UPDATE","ADD"};

    ViewFlipper flipper;
    GridView grid_daily_routin;
    GridView grid_test_records;
    Animation fade_in,fade_out;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        flipper=(ViewFlipper)findViewById(R.id.flipper);
        grid_daily_routin=(GridView)findViewById(R.id.grid_daily_routin);
        grid_test_records=(GridView)findViewById(R.id.grid_test_record);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        fade_in= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fade_out= AnimationUtils.loadAnimation(this,R.anim.fade_out);
        flipper.setAutoStart(true);
        flipper.setInAnimation(fade_in);
        flipper.setOutAnimation(fade_out);
        flipper.setFlipInterval(20000);
        flipper.startFlipping();



        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.bottom_navigation_home:

                                break;
                            case R.id.bottom_navigation_notification:

                                intent=new Intent(HomeActivity.this,NotificationActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.bottom_navigation_report:
                                intent=new Intent(HomeActivity.this,ReportActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return false;
                    }

                }
        );
        HealthPlanGridAdapter adapter=new HealthPlanGridAdapter(grid_daily_routin_text,grid_daily_routin_icon,grid_daily_routin_button_status,HomeActivity.this);
        grid_daily_routin.setAdapter(adapter);

        TestRecordGridAdapter adapter_test_record=new TestRecordGridAdapter(grid_test_record_text,grid_test_record_icon,grid_record_test_button_status,HomeActivity.this);
        grid_test_records.setAdapter(adapter_test_record);
        grid_daily_routin.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent intent;
                        if(position==0)
                        {
                            intent=new Intent(HomeActivity.this,NutritionActivity.class);
                            startActivity(intent);
                        }
                        if(position==1)
                        {
                            intent=new Intent(HomeActivity.this,ExerciseActivity.class);
                            startActivity(intent);
                        }
                        if(position==2)
                        {
                            intent=new Intent(HomeActivity.this,MedicationActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );
        grid_test_records.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent intent;
                        if(position==0)
                        {
                            intent=new Intent(HomeActivity.this,BloodPressureActivity.class);
                            startActivity(intent);
                        }
                        if(position==1)
                        {
                            intent=new Intent(HomeActivity.this,GlucoseActivity.class);
                            startActivity(intent);
                        }
                        if(position==2)
                        {
                            intent=new Intent(HomeActivity.this,ManageItemActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.item_setting)
        {
            Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.item_profile)
        {
            Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
}

