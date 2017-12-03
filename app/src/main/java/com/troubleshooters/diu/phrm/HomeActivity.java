package com.troubleshooters.diu.phrm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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



public class HomeActivity extends AppCompatActivity {


    String grid_daily_routin_text[]={"Nutrition","Exercise","Medication"};
    int grid_daily_routin_icon[]={R.drawable.nutrition_icon,R.drawable.exercise_icon,R.drawable.medication_icon};
    String grid_daily_routin_button_status[]={"CREAT","UPDATE"};

    ViewFlipper flipper;
    GridView grid_daily_routin;
    Animation fade_in,fade_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        flipper=(ViewFlipper)findViewById(R.id.flipper);
        grid_daily_routin=(GridView)findViewById(R.id.grid_daily_routin);
        fade_in= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fade_out= AnimationUtils.loadAnimation(this,R.anim.fade_out);
        flipper.setAutoStart(true);
        flipper.setInAnimation(fade_in);
        flipper.setOutAnimation(fade_out);
        flipper.setFlipInterval(20000);
        flipper.startFlipping();




        HealthPlanGridAdapter adapter=new HealthPlanGridAdapter(grid_daily_routin_text,grid_daily_routin_icon,grid_daily_routin_button_status,HomeActivity.this);
        grid_daily_routin.setAdapter(adapter);

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

