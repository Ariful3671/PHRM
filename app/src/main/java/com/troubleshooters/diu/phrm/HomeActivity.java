package com.troubleshooters.diu.phrm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.troubleshooters.diu.phrm.Adapter.HealthPlanGridAdapter;
import com.troubleshooters.diu.phrm.Adapter.MedicationReminder;
import com.troubleshooters.diu.phrm.Adapter.Model_medicin_details;
import com.troubleshooters.diu.phrm.Adapter.TestRecordGridAdapter;
import com.troubleshooters.diu.phrm.Helper.LocaleHelper;

import java.util.Calendar;

import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity {




    //Initialization for Test record and health plan adapter
    String grid_daily_routin_text[]={"Nutrition","Exercise","Medication"};
    String grid_test_record_text[]={"Blood Pressure","Glucose","BMI"};
    int grid_test_record_icon[]={R.drawable.blood_pressure_icon,R.drawable.glucose_test_icon,R.drawable.add};
    int grid_daily_routin_icon[]={R.drawable.nutrition_icon,R.drawable.exercise_icon,R.drawable.medication_icon};
    String grid_daily_routin_button_status[]={"CREATE","UPDATE"};
    String grid_record_test_button_status[]={"Update","Update","Update"};




    ViewFlipper flipper;
    GridView grid_daily_routin;
    GridView grid_test_records;
    Animation fade_in,fade_out;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Setting language
        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");
        else if(language == "en")
            Paper.book().write("language", "en");
        else if(language == "bn")
            Paper.book().write("language", "bn");
        updateView((String)Paper.book().read("language"));

        setTitle(getString(R.string.home_title));//Changing activity name.
        flipper=(ViewFlipper)findViewById(R.id.flipper);
        grid_daily_routin=(GridView)findViewById(R.id.grid_daily_routin);
        grid_test_records=(GridView)findViewById(R.id.grid_test_record);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);





        //Animating view flipper
        fade_in= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fade_out= AnimationUtils.loadAnimation(this,R.anim.fade_out);
        flipper.setAutoStart(true);
        flipper.setInAnimation(fade_in);
        flipper.setOutAnimation(fade_out);
        flipper.setFlipInterval(20000);
        flipper.startFlipping();




        //Creating firebase and sharedpreference for updating profileinfo sharedpreference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        String user=sharedPreferences.getString("userid","");
        DatabaseReference ref = database.getReference("users").child(user);





        //Getting profile data from fire base to sharedpreference
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name,phone,birthday,height,weight,gender,activity;

                        if(dataSnapshot.hasChild("name"))
                        {
                            name=dataSnapshot.child("name").getValue().toString();
                            editor.putString("name",name);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "name:"+name, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            name="";
                            editor.putString("name",name);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "name:", Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild("phone"))
                        {
                            phone=dataSnapshot.child("phone").getValue().toString();
                            editor.putString("phone",phone);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "phone:"+phone, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            phone="";
                            editor.putString("phone",phone);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "phone:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("byear"))
                        {
                            String bYear=dataSnapshot.child("byear").getValue().toString();
                            String bMonth=dataSnapshot.child("bmonth").getValue().toString();
                            String bDay=dataSnapshot.child("bday").getValue().toString();
                            editor.putString("birthday",bDay+":"+bMonth+":"+bYear);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "birthday: exist", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            birthday="";
                            editor.putString("birthday",birthday);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "birthday:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("byear"))
                        {
                            int currentYear= Calendar.getInstance().get(Calendar.YEAR);
                            int currentMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
                            int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                            int bYear=Integer.parseInt(dataSnapshot.child("byear").getValue().toString());
                            int bMonth=Integer.parseInt(dataSnapshot.child("bmonth").getValue().toString());
                            int bDay=Integer.parseInt(dataSnapshot.child("bday").getValue().toString());

                            int age=currentYear-bYear;
                            if(currentMonth-bMonth<0)
                            {
                                age--;
                            }
                            if(currentMonth-bMonth==0)
                            {
                                if(currentDay-bDay<0)
                                {
                                    age--;
                                }
                            }
                            editor.putString("age",String.valueOf(age));
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "age:"+age, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String age="";
                            editor.putString("age",age);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "age:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("height"))
                        {
                            height=dataSnapshot.child("height").getValue().toString();
                            editor.putString("height",height);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "height:"+height, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            height="";
                            editor.putString("height",height);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "height:", Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild("weight"))
                        {
                            weight=dataSnapshot.child("weight").getValue().toString();
                            editor.putString("weight",weight);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "weight:"+weight, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            weight="";
                            editor.putString("weight",weight);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "weight:", Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild("gender"))
                        {
                            gender=dataSnapshot.child("gender").getValue().toString();
                            editor.putString("gender",gender);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "gender:"+gender, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            gender="";
                            editor.putString("gender",gender);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "gender:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("activity"))
                        {
                            activity=dataSnapshot.child("activity").getValue().toString();
                            editor.putString("activity",activity);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "activity:"+activity, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            activity="";
                            editor.putString("activity",activity);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "activity:", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );






        //Setting activity for bottom navigation menu
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




        //Setting adapter for grid views
        HealthPlanGridAdapter adapter=new HealthPlanGridAdapter(grid_daily_routin_text,grid_daily_routin_icon,grid_daily_routin_button_status,HomeActivity.this);
        grid_daily_routin.setAdapter(adapter);
        TestRecordGridAdapter adapter_test_record=new TestRecordGridAdapter(grid_test_record_text,grid_test_record_icon,grid_record_test_button_status,HomeActivity.this);
        grid_test_records.setAdapter(adapter_test_record);




        //setting activity for grid view health plan
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







        //setting activity for grid view test record
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

    //Updating language change
    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();

    }


    //Actionbar menu setting
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
        if(id==R.id.item_log_out)
        {

            SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_nutrition=getSharedPreferences("nutrition", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_time=getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_medicine=getSharedPreferences("medication",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();




            //Save password and dont show dialog
            if(sharedPreferences.getString("save password status","").equals("yes"))
            {

                //Canceling meal reminder
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);



                //Canceling alarm of medication activity
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));




                ref.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {

                                    int NID=Integer.parseInt(snapshot.child("nid").getValue().toString());
                                    int numberofalarm=Integer.parseInt(snapshot.child("numberofalarm").getValue().toString());
                                    if(numberofalarm==1)
                                    {
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                    }
                                    if(numberofalarm>1)
                                    {
                                        for(int i=0;i<numberofalarm;i++)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            NID=NID+1;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );



                String userid=sharedPreferences.getString("userid","");
                String password=sharedPreferences.getString("password","");
                sharedPreferences.edit().clear().commit();
                sharedPreferences_nutrition.edit().clear().commit();
                sharedPreferences_time.edit().clear().commit();
                sharedPreferences_medicine.edit().clear().commit();
                editor.putString("userid",userid);
                editor.putString("password",password);
                editor.putString("save password status", "yes");
                editor.putString("save password dialog status", "no");
                editor.putString("logout status", "yes");
                editor.commit();
            }














            //Dont save password and dont show dialog
            else if(sharedPreferences.getString("save password status","").equals("no")&&sharedPreferences.getString("save password dialog status","").equals("no"))
            {
                //Canceling meal reminder
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);





                //Canceling alarm of medication activity
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));




                ref.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {

                                    int NID=Integer.parseInt(snapshot.child("nid").getValue().toString());
                                    int numberofalarm=Integer.parseInt(snapshot.child("numberofalarm").getValue().toString());
                                    if(numberofalarm==1)
                                    {
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                    }
                                    if(numberofalarm>1)
                                    {
                                        for(int i=0;i<numberofalarm;i++)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            NID=NID+1;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );







                String userid=sharedPreferences.getString("userid","");
                String password=sharedPreferences.getString("password","");
                sharedPreferences.edit().clear().commit();
                sharedPreferences_nutrition.edit().clear().commit();
                sharedPreferences_time.edit().clear().commit();
                sharedPreferences_medicine.edit().clear().commit();
                editor.putString("userid",userid);
                editor.putString("password",password);
                editor.putString("save password status", "no");
                editor.putString("save password dialog status", "no");
                editor.putString("logout status", "yes");
                editor.commit();
            }









            //dont save password and show dialog
            else{
                //Canceling meal reminder
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);





                //Canceling alarm of medication activity
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));




                ref.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {

                                    int NID=Integer.parseInt(snapshot.child("nid").getValue().toString());
                                    int numberofalarm=Integer.parseInt(snapshot.child("numberofalarm").getValue().toString());
                                    if(numberofalarm==1)
                                    {
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                    }
                                    if(numberofalarm>1)
                                    {
                                        for(int i=0;i<numberofalarm;i++)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            NID=NID+1;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );






                String userid=sharedPreferences.getString("userid","");
                String password=sharedPreferences.getString("password","");
                sharedPreferences.edit().clear().commit();
                sharedPreferences_nutrition.edit().clear().commit();
                sharedPreferences_time.edit().clear().commit();
                sharedPreferences_medicine.edit().clear().commit();
                editor.putString("userid",userid);
                editor.putString("password",password);
                editor.putString("save password status", "no");
                editor.putString("save password dialog status", "yes");
                editor.putString("logout status", "yes");
                editor.commit();
            }
            Intent intent=new Intent(HomeActivity.this,LogInActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return true;
    }
}

