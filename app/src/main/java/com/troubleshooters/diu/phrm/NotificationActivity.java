package com.troubleshooters.diu.phrm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.troubleshooters.diu.phrm.Adapter.MedicationReminder;
import com.troubleshooters.diu.phrm.Adapter.ModelReminder;
import com.troubleshooters.diu.phrm.Adapter.Model_medicin_details;
import com.troubleshooters.diu.phrm.Adapter.ReminderItemAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    TextView noreminder,loading;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<ModelReminder> reminderList;
    public static Activity notification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Reminders");
        notification=this;
        noreminder=(TextView)findViewById(R.id.noreminder_reminder);
        loading=(TextView)findViewById(R.id.loading_reminder);
        progressBar=(ProgressBar)findViewById(R.id.progressbar_reminder);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_reminder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderList=new ArrayList<>();




        NetworkChecker networkChecker =new NetworkChecker(NotificationActivity.this);
        if(networkChecker.isConnected())
        {
            SharedPreferences sharedPreferences = getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference ref=database.getReference("reminder").child(sharedPreferences.getString("userid",""));
            ref.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {

                                String reminderdetails=snapshot.child("remindertext").getValue().toString();
                                String hour=snapshot.child("hour").getValue().toString();
                                String minute=snapshot.child("minute").getValue().toString();
                                String day=snapshot.child("day").getValue().toString();
                                String month=snapshot.child("month").getValue().toString();
                                String year=snapshot.child("year").getValue().toString();
                                String RID=snapshot.child("rid").getValue().toString();



                                ReminderItemAdapter adapter;
                                reminderList.add(new ModelReminder(reminderdetails,hour,minute,day,year,month,RID));
                                adapter=new ReminderItemAdapter(NotificationActivity.this,reminderList);
                                recyclerView.setAdapter(adapter);
                            }
                            if(reminderList.isEmpty())
                            {
                                noreminder.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else{
                                loading.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }
        else {
            Toast.makeText(NotificationActivity.this, "Please turn on your internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.reminder_menu,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.item_reminder)
        {

            Intent intent1=new Intent(NotificationActivity.this,ActivityGenerateReminder.class);
            startActivity(intent1);
        }
        return false;
    }
}
