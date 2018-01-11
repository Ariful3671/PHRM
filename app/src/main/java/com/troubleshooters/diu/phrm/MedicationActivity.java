package com.troubleshooters.diu.phrm;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MedicationActivity extends AppCompatActivity {

    Button addPlan;
    RecyclerView recyclerView;
    List<Model_medicin_details> medicin_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        setTitle("Medication plan");
        medicin_details=new ArrayList<>();



        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_medication);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences sharedPreferences = getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



        /*Gson gson = new Gson();

        f(!sharedPreferences1.getString("MyObject", "").equals(""))
        {
            String json = sharedPreferences1.getString("MyObject", "");
            Type type = new TypeToken<List<Model_medicin_details>>(){}.getType();
            medicin_details= gson.fromJson(json, type);
        }*/

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));


        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );





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
