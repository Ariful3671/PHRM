package com.troubleshooters.diu.phrm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.troubleshooters.diu.phrm.Adapter.ReminderAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityGenerateReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener ,DatePickerDialog.OnDateSetListener{



    String[] timeanddate={"Time","Date"};
    String[] details={"Set time","Set date"};
    int right_arrow[]={R.drawable.ic_action_right_arrow};
    int hour,minute,day,month,year;

    EditText reminderInfo;
    ListView listView;
    Button creat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reminder);



        reminderInfo=(EditText)findViewById(R.id.edit_text_redimder_info);
        listView=(ListView)findViewById(R.id.listview_reminder);
        creat=(Button)findViewById(R.id.button_create_reminder);


        SharedPreferences sharedPreferences_time=getSharedPreferences("remindertime", Context.MODE_PRIVATE);
        sharedPreferences_time.edit().clear().commit();
        ReminderAdapter adapter=new ReminderAdapter(timeanddate,details,right_arrow,ActivityGenerateReminder.this);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position)
                        {
                            case 0:
                                Calendar c=Calendar.getInstance();
                                hour=c.get(Calendar.HOUR_OF_DAY);
                                minute=c.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog=new TimePickerDialog(ActivityGenerateReminder.this,reminder_time,hour,minute,true);
                                timePickerDialog.show();
                                break;
                            case 1:
                                DialogFragment datePicker=new DatePickerFragment();
                                datePicker.show(getFragmentManager(),"date picker");
                                break;
                        }
                    }
                }
        );



        final SharedPreferences sharedPreferences_RID=getSharedPreferences("reminder", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences_RID.edit();
        if(sharedPreferences_RID.getInt("RID",0)==0)
        {
            editor.putInt("RID",500);
            editor.commit();
        }


        creat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkChecker networkChecker =new NetworkChecker(ActivityGenerateReminder.this);
                        if(networkChecker.isConnected())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference ref=database.getReference("reminder").child(sharedPreferences.getString("userid",""));
                            String id=ref.push().getKey();
                            int RID=sharedPreferences_RID.getInt("RID",0)+1;
                            editor.putInt("RID",RID);
                            editor.commit();
                            Map<String, String> reminderData = new HashMap<String, String>();
                            reminderData.put("remindertext",reminderInfo.getText().toString());
                            reminderData.put("hour",String.valueOf(hour));
                            reminderData.put("minute", String.valueOf(minute));
                            reminderData.put("day", String.valueOf(day));
                            reminderData.put("month", String.valueOf(month));
                            reminderData.put("year", String.valueOf(year));
                            reminderData.put("rid",String.valueOf(RID) );
                            ref.child(id).setValue(reminderData);


                            Date date=new Date();
                            Calendar calendar=Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            Calendar currentTime=Calendar.getInstance();
                            calendar.setTime(date);
                            currentTime.setTime(date);
                            calendar.set(Calendar.HOUR_OF_DAY ,hour);
                            calendar.set(Calendar.MINUTE,minute);
                            calendar.set(Calendar.DAY_OF_MONTH,day);
                            calendar.set(Calendar.MONTH,month);
                            calendar.set(Calendar.YEAR,year);
                            if(calendar.before(currentTime))
                            {
                                calendar.add(Calendar.DATE,1);
                            }
                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                            intent.putExtra("NID",RID);
                            intent.putExtra("text",reminderInfo.getText().toString());
                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),RID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

                            NotificationActivity.notification.finish();
                            Intent intent1=new Intent(ActivityGenerateReminder.this,NotificationActivity.class);
                            startActivity(intent1);
                            finish();

                        }
                        else {
                            Toast.makeText(ActivityGenerateReminder.this, "Please turn on your internet connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );



    }

    TimePickerDialog.OnTimeSetListener reminder_time=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {


            hour=hourOfDay;
            minute=minutes;
            SharedPreferences sharedPreferences_time=getSharedPreferences("remindertime",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_time=sharedPreferences_time.edit();
            editor_time.putString("hour",String.valueOf(hourOfDay));
            editor_time.putString("minute",String.valueOf(minute));
            editor_time.commit();

            ReminderAdapter adapter=new ReminderAdapter(timeanddate,details,right_arrow,ActivityGenerateReminder.this);
            listView.setAdapter(adapter);
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        SharedPreferences sharedPreferences_time=getSharedPreferences("remindertime",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_time=sharedPreferences_time.edit();
        day=dayOfMonth;
        this.month=month;
        this.year=year;
        editor_time.putString("day",String.valueOf(dayOfMonth));
        editor_time.putString("month",String.valueOf(month));
        editor_time.putString("year",String.valueOf(year));
        editor_time.commit();

        ReminderAdapter adapter=new ReminderAdapter(timeanddate,details,right_arrow,ActivityGenerateReminder.this);
        listView.setAdapter(adapter);

    }
    //Default method for time picker
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
