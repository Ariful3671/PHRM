package com.troubleshooters.diu.phrm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMedication extends AppCompatActivity {


    TextView heading;
    EditText medicines;
    Button ok,delet;
    int NID;
    int numberOfAlarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        heading=(TextView)findViewById(R.id.medicine_heading_edit);
        medicines=(EditText)findViewById(R.id.edit_text_medicine_info_edit);
        ok=(Button)findViewById(R.id.ok_button_edit);
        delet=(Button)findViewById(R.id.delet_button_edit);
        medicines.setText(getIntent().getStringExtra("medInfo"));
        NID=Integer.parseInt(getIntent().getStringExtra("NID"));
        numberOfAlarm=Integer.parseInt(getIntent().getStringExtra("numberofalarm"));
        //Toast.makeText(this, String.valueOf(NID)+"   "+String.valueOf(numberOfAlarm), Toast.LENGTH_SHORT).show();


        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newText=medicines.getText().toString();
                        if(newText.equals(getIntent().getStringExtra("medInfo")))
                        {
                            MedicationActivity.medication.finish();
                            Intent intent=new Intent(EditMedication.this,MedicationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
                            DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));
                            ref.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                            {

                                                if(snapshot.child("nid").getValue().equals(getIntent().getStringExtra("NID")))
                                                {
                                                    DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid","")).child(snapshot.getKey());
                                                    ref.child("medicinesname").setValue(newText);
                                                    MedicationActivity.medication.finish();
                                                    Intent intent=new Intent(EditMedication.this,MedicationActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    }
                }
        );

        delet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(numberOfAlarm==1)
                        {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
                            final DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));

                            ref.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                            {
                                                if(snapshot.child("nid").getValue().equals(getIntent().getStringExtra("NID")))
                                                {
                                                    ref.child(snapshot.getKey()).removeValue();
                                                    Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                                    PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                    AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                                    alarmManager.cancel(pendingIntent);
                                                    MedicationActivity.medication.finish();
                                                    Intent intent1=new Intent(EditMedication.this,MedicationActivity.class);
                                                    startActivity(intent1);
                                                    finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                        if(numberOfAlarm>1)
                        {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
                            final DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));
                            ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                                {
                                                    if(snapshot.child("nid").getValue().equals(getIntent().getStringExtra("NID")))
                                                    {
                                                        ref.child(snapshot.getKey()).removeValue();
                                                        for(int i=0;i<numberOfAlarm;i++)
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
                            MedicationActivity.medication.finish();
                            Intent intent1=new Intent(EditMedication.this,MedicationActivity.class);
                            startActivity(intent1);
                            finish();
                        }

                    }
                }
        );


    }
}
