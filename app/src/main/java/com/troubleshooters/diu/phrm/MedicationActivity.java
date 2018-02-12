package com.troubleshooters.diu.phrm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troubleshooters.diu.phrm.Adapter.MedicationReminder;
import com.troubleshooters.diu.phrm.Adapter.Model_medicin_details;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MedicationActivity extends AppCompatActivity {

    Button addPlan;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<Model_medicin_details> medicin_details;
    NetworkChecker networkChecker;
    TextView loading,empty;
    public static Activity medication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        setTitle(getString(R.string.medication_plan_title));
        medicin_details=new ArrayList<>();
        medication=this;



        networkChecker=new NetworkChecker(MedicationActivity.this);
        progressBar=(ProgressBar)findViewById(R.id.progressbar_medication);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_medication);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loading=(TextView)findViewById(R.id.loading_medication);
        empty=(TextView)findViewById(R.id.text_view_medication_layout);




        final SharedPreferences sharedPreferences = getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();




        //checking network connection and getting database ref
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=null;
        if(networkChecker.isConnected())
        {
            ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));
        }
        else {
            Toast.makeText(MedicationActivity.this, "Please turn on your internet connection", Toast.LENGTH_SHORT).show();
        }






        //retrive data from firebase
        if(ref!=null)
        {
            ref.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SharedPreferences profileinfo=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
                            SharedPreferences.Editor status=profileinfo.edit();
                            SharedPreferences updateNID=getSharedPreferences("medication",Context.MODE_PRIVATE);
                            SharedPreferences.Editor update=updateNID.edit();




                            //updating nid if log in first time
                            if(profileinfo.getString("medication reminder status","").equals(""))
                            {
                                if(updateNID.getInt("NID",0)==0)
                                {
                                    update.putInt("NID",110);
                                    update.commit();
                                }
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {
                                    String key=snapshot.getKey();
                                    int value=updateNID.getInt("NID",0)+1;
                                    String NID=String.valueOf(value);
                                    update.putInt("NID",value);
                                    update.commit();
                                    DatabaseReference temp_ref=database.getReference("medication reminder").child(profileinfo.getString("userid","")).child(key);
                                    temp_ref.child("nid").setValue(NID);
                                }
                            }




                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                String name=snapshot.child("medicinesname").getValue().toString();
                                String hour=snapshot.child("hour").getValue().toString();
                                String minute=snapshot.child("minute").getValue().toString();
                                String days=snapshot.child("nameofdays").getValue().toString();
                                String date=snapshot.child("date").getValue().toString();
                                String month=snapshot.child("month").getValue().toString();
                                String year=snapshot.child("year").getValue().toString();
                                String nid=snapshot.child("nid").getValue().toString();
                                String AMorPM=snapshot.child("AMorPM").getValue().toString();
                                String type=snapshot.child("alarmtype").getValue().toString();
                                String numberofalarm=snapshot.child("numberofalarm").getValue().toString();



                                MedicationReminder adapter;
                                medicin_details.add(new Model_medicin_details(name,hour,minute,days,date,month,year,nid,AMorPM,type,numberofalarm));
                                adapter=new MedicationReminder(MedicationActivity.this,medicin_details);
                                recyclerView.setAdapter(adapter);
                            }



                            //checking if there is any alarm
                            if(medicin_details.isEmpty())
                            {
                                empty.setVisibility(View.VISIBLE);//Show no alarm is set
                            }
                            else{
                                //setting up all alarm when first login
                                if(profileinfo.getString("medication reminder status","").equals(""))
                                {
                                    status.putString("medication reminder status","yes");
                                    status.commit();
                                    for(Model_medicin_details object:medicin_details)
                                    {
                                        int NID=Integer.valueOf(object.getNID());
                                        int  numberOfAlarm=Integer.valueOf(object.getNumberofalarm());
                                        String alarmType=object.getType();
                                        String medicinesName=object.getMedicines();
                                        String nameOfDays=object.getDays();
                                        int minute=Integer.valueOf(object.getMinute());
                                        int hour=Integer.valueOf(object.getHour());
                                        if(alarmType.equals("Everyday"))
                                        {
                                            Date date=new Date();
                                            Calendar calendar=Calendar.getInstance();
                                            calendar.setTimeInMillis(System.currentTimeMillis());
                                            Calendar currentTime=Calendar.getInstance();
                                            calendar.setTime(date);
                                            currentTime.setTime(date);
                                            calendar.set(Calendar.HOUR_OF_DAY ,hour);
                                            calendar.set(Calendar.MINUTE,minute);
                                            if(calendar.before(currentTime))
                                            {
                                                calendar.add(Calendar.DATE,1);
                                            }
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            intent.putExtra("NID",NID);
                                            intent.putExtra("text",medicinesName);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);
                                        }
                                        if(alarmType.equals("custom or today"))
                                        {

                                            String[] split=nameOfDays.split("\\s+");
                                            for(int i=0;i<numberOfAlarm;i++)
                                            {
                                                Date date=new Date();
                                                Calendar calendar=Calendar.getInstance();
                                                calendar.setTimeInMillis(System.currentTimeMillis());
                                                Calendar currentTime=Calendar.getInstance();
                                                calendar.setTime(date);
                                                currentTime.setTime(date);
                                                if(split[i].equals("Sun"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,1);
                                                }
                                                if(split[i].equals("Mon"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,2);
                                                }
                                                if(split[i].equals("Tue"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,3);
                                                }
                                                if(split[i].equals("Wed"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,4);
                                                }
                                                if(split[i].equals("Thu"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,5);
                                                }
                                                if(split[i].equals("Fri"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,6);
                                                }
                                                if(split[i].equals("Sat"))
                                                {
                                                    calendar.set(Calendar.DAY_OF_WEEK,7);
                                                }
                                                calendar.set(Calendar.HOUR_OF_DAY ,hour);
                                                calendar.set(Calendar.MINUTE,minute);
                                                if(calendar.before(currentTime))
                                                {
                                                    calendar.add(Calendar.DATE,1);
                                                }
                                                Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                                intent.putExtra("NID",NID);
                                                intent.putExtra("text",medicinesName);
                                                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY*7,pendingIntent);
                                            }
                                        }

                                    }
                                }
                                else{
                                    //Nothing
                                }
                            }




                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            addPlan.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }

        //Move to add plan activity
        addPlan=(Button)findViewById(R.id.addPlan);
        addPlan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MedicationActivity.this,CreatMedicationPlanActivity.class);
                        startActivity(intent);
                    }
                }
        );


    }

}
