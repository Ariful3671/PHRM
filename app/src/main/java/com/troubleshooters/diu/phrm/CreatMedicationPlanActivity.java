package com.troubleshooters.diu.phrm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.troubleshooters.diu.phrm.Adapter.Model_medicin_details;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreatMedicationPlanActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener ,DatePickerDialog.OnDateSetListener{



    CheckBox custom,everyday,today,sa,su,mo,tu,we,th,fr;
    CardView cardViewDate,cardViewTime;
    EditText medicineInfoEditText;
    TextView time,startingDate;
    Button creatPlan,timeButton,dateButton;
    int initialValue;
    int numberOfAlarm;



    String medicinesName;
    int s_hour=0,s_minute=0,hour,minute;
    int month,year,dayOfMonth;
    String AMorPM;
    String[] days=new String[7];
    String alarmType;
    String nameOfDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_medication_plan);
        setTitle(getString(R.string.create_medication_plan_title));

        custom=(CheckBox)findViewById(R.id.custom_checkbox);
        everyday=(CheckBox)findViewById(R.id.repeat_everyday_checkbox);
        today=(CheckBox)findViewById(R.id.repeat_today_checkbox);
        sa=(CheckBox)findViewById(R.id.sa);
        su=(CheckBox)findViewById(R.id.su);
        mo=(CheckBox)findViewById(R.id.mo);
        tu=(CheckBox)findViewById(R.id.tu);
        we=(CheckBox)findViewById(R.id.we);
        th=(CheckBox)findViewById(R.id.th);
        fr=(CheckBox)findViewById(R.id.fr);

        medicineInfoEditText=(EditText)findViewById(R.id.edit_text_medicine_info);

        time=(TextView)findViewById(R.id.time);
        startingDate=(TextView)findViewById(R.id.date);

        cardViewDate=(CardView)findViewById(R.id.card_view_date);
        cardViewTime=(CardView)findViewById(R.id.card_view_time);

        creatPlan=(Button)findViewById(R.id.create_plan);
        timeButton=(Button)findViewById(R.id.time_button);
        dateButton=(Button)findViewById(R.id.date_button);




        //Initilizing initialValue
        final SharedPreferences sharedPreferences=getSharedPreferences("medication", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.getInt("NID",0)==0)
        {
            editor.putInt("NID",110);
            editor.commit();
        }




        //Initilizing days array
        for(int i=0;i<7;i++)
        {
            days[i]="0";
        }





        final SharedPreferences sharedPreferences1=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences1.getString("userid",""));






        //On click listener for time
        timeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c=Calendar.getInstance();
                        hour=c.get(Calendar.HOUR_OF_DAY);
                        minute=c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(CreatMedicationPlanActivity.this,medicationTime,hour,minute,true);
                        timePickerDialog.show() ;
                    }
                }
        );






        //On click listener for date
        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment datePicker=new DatePickerFragment();
                        datePicker.show(getFragmentManager(),"date picker");
                    }
                }
        );






        creatPlan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alarmType=checkAlarmType();
                        nameOfDays=getNameOfDays();
                        numberOfAlarm=getNumberOfAlarm();
                        //Toast.makeText(CreatMedicationPlanActivity.this, nameOfDays, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CreatMedicationPlanActivity.this, alarmType, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CreatMedicationPlanActivity.this, String.valueOf(numberOfAlarm), Toast.LENGTH_SHORT).show();
                        medicinesName=medicineInfoEditText.getText().toString();
                        if(medicinesName.equals(""))
                        {
                            Toast.makeText(CreatMedicationPlanActivity.this, "Please enter your required medicines", Toast.LENGTH_SHORT).show();
                        }
                        else if(s_hour==0)
                        {
                            Toast.makeText(CreatMedicationPlanActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
                        }
                        else if(alarmType.equals(""))
                        {
                            Toast.makeText(CreatMedicationPlanActivity.this, "Please select a repeat option", Toast.LENGTH_SHORT).show();
                        }
                        else{



                            if(alarmType.equals("Everyday"))
                            {
                                initialValue=sharedPreferences.getInt("NID",0)+1;
                                editor.putInt("NID",initialValue);
                                editor.commit();
                                String id=ref.push().getKey();
                                Map<String, String> reminderData = new HashMap<String, String>();
                                reminderData.put("medicinesname",medicinesName);
                                reminderData.put("minute",String.valueOf(s_minute));
                                reminderData.put("hour", String.valueOf(s_hour));
                                reminderData.put("nameofdays", "Everyday");
                                reminderData.put("date", String.valueOf(dayOfMonth));
                                reminderData.put("month", String.valueOf(month+1));
                                reminderData.put("year", String.valueOf(year));
                                reminderData.put("nid", String.valueOf(initialValue));
                                reminderData.put("AMorPM", AMorPM);
                                reminderData.put("alarmtype",alarmType);
                                reminderData.put("numberofalarm",String.valueOf(1));
                                ref.child(id).setValue(reminderData);


                                //Setting alarm for everyday
                                Date date=new Date();
                                Calendar calendar=Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                Calendar currentTime=Calendar.getInstance();
                                calendar.setTime(date);
                                currentTime.setTime(date);
                                calendar.set(Calendar.HOUR_OF_DAY ,s_hour);
                                calendar.set(Calendar.MINUTE,s_minute);
                                if(calendar.before(currentTime))
                                {
                                    calendar.add(Calendar.DATE,1);
                                }
                                Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                intent.putExtra("NID",initialValue);
                                intent.putExtra("text",medicinesName);
                                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),initialValue,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);



                                //medication status off status
                                SharedPreferences profileinfo=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
                                SharedPreferences.Editor status=profileinfo.edit();
                                status.putString("medication reminder status","yes");
                                status.commit();



                                //Changing activity after setting alarm
                                Intent intent1=new Intent(CreatMedicationPlanActivity.this,MedicationActivity.class);
                                startActivity(intent1);
                                finish();
                            }



                            if(alarmType.equals("custom or today"))
                            {
                                initialValue=sharedPreferences.getInt("NID",0)+1;
                                editor.putInt("NID",initialValue);
                                editor.commit();
                                String id=ref.push().getKey();
                                Map<String, String> reminderData = new HashMap<String, String>();
                                reminderData.put("medicinesname",medicinesName);
                                reminderData.put("minute",String.valueOf(s_minute));
                                reminderData.put("hour", String.valueOf(s_hour));
                                reminderData.put("nameofdays", nameOfDays);
                                reminderData.put("date", String.valueOf(dayOfMonth));
                                reminderData.put("month", String.valueOf(month+1));
                                reminderData.put("year", String.valueOf(year));
                                reminderData.put("nid", String.valueOf(initialValue));
                                reminderData.put("AMorPM", AMorPM);
                                reminderData.put("alarmtype",alarmType);
                                reminderData.put("numberofalarm",String.valueOf(numberOfAlarm));
                                ref.child(id).setValue(reminderData);




                                for(int i=0;i<7;i++)
                                {
                                    if(days[i]=="1")
                                    {
                                        Date date=new Date();
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.setTimeInMillis(System.currentTimeMillis());
                                        Calendar currentTime=Calendar.getInstance();
                                        calendar.setTime(date);
                                        currentTime.setTime(date);
                                        calendar.set(Calendar.DAY_OF_WEEK,i+1);
                                        calendar.set(Calendar.HOUR_OF_DAY ,s_hour);
                                        calendar.set(Calendar.MINUTE,s_minute);
                                        if(calendar.before(currentTime))
                                        {
                                            calendar.add(Calendar.DATE,1);
                                        }
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        intent.putExtra("NID",initialValue);
                                        intent.putExtra("text",medicinesName);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),initialValue,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY*7,pendingIntent);
                                        initialValue=sharedPreferences.getInt("NID",0)+1;
                                        editor.putInt("NID",initialValue);
                                        editor.commit();
                                    }
                                }



                                //medication reminder on status
                                SharedPreferences profileinfo=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
                                SharedPreferences.Editor status=profileinfo.edit();
                                status.putString("medication reminder status","yes");
                                status.commit();



                                MedicationActivity.medication.finish();
                                Intent intent1=new Intent(CreatMedicationPlanActivity.this,MedicationActivity.class);
                                startActivity(intent1);
                                finish();
                            }



                        }
                    }
                }
        );

    }

    //checking custom checkbox
    public void customCheckBoxStatus(View view) {

        LinearLayout day=(LinearLayout)findViewById(R.id.day);

        if(custom.isChecked())
        {
            today.setChecked(false);
            everyday.setChecked(false);
            day.setVisibility(View.VISIBLE);
        }
        else{
            day.setVisibility(View.GONE);
        }

    }






    //Checking today checkbox
    public void getStatusToday(View view) {


        if(today.isChecked())
        {
            LinearLayout day=(LinearLayout)findViewById(R.id.day);
            custom.setChecked(false);
            everyday.setChecked(false);
            day.setVisibility(View.GONE);
            Calendar c=Calendar.getInstance();
            int num=c.get(Calendar.DAY_OF_WEEK);
            if(num==1)
            {
                days[0]="1";
            }
            else if (num==2)
            {
                days[1]="1";
            }
            else if (num==3)
            {
                days[2]="1";
            }
            else if (num==4)
            {
                days[3]="1";
            }
            else if (num==5)
            {
                days[4]="1";
            }
            else if (num==6)
            {
                days[5]="1";
            }
            else{
                days[6]="1";
            }
            //Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(this, "Unchecked", Toast.LENGTH_SHORT).show();
            days[0]="0";
            days[1]="0";
            days[2]="0";
            days[3]="0";
            days[4]="0";
            days[5]="0";
            days[6]="0";
        }

    }




    //Everyday checkbox
    public void getStatusEveryday(View view) {

        if(everyday.isChecked())
        {
            LinearLayout day=(LinearLayout)findViewById(R.id.day);
            custom.setChecked(false);
            today.setChecked(false);
            day.setVisibility(View.GONE);
            days[0]="1";
            days[1]="1";
            days[2]="1";
            days[3]="1";
            days[4]="1";
            days[5]="1";
            days[6]="1";
            //Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
        }
        else
        {
            days[0]="0";
            days[1]="0";
            days[2]="0";
            days[3]="0";
            days[4]="0";
            days[5]="0";
            days[6]="0";
            //Toast.makeText(this, "Unchecked", Toast.LENGTH_SHORT).show();
        }

    }







    //Method for checking today and everyday checkbox
    public void getStatus(View view) {


        //Layout of custom checkbox
        LinearLayout day=(LinearLayout)findViewById(R.id.day);


        //Checking the custom checkbox
        if(sa.isChecked())
        {
            days[6]="1";
        }
        else {
            days[6]="0";
        }
        if(su.isChecked())
        {
            days[0]="1";
        }
        else {
            days[0]="0";
        }
        if(mo.isChecked())
        {
            days[1]="1";
        }
        else {
            days[1]="0";
        }
        if(tu.isChecked())
        {
            days[2]="1";
        }
        else {
            days[2]="0";
        }
        if(we.isChecked())
        {
            days[3]="1";
        }
        else {
            days[3]="0";
        }
        if(th.isChecked())
        {
            days[4]="1";
        }
        else {
            days[4]="0";
        }
        if(fr.isChecked())
        {
            days[5]="1";
        }
        else {
            days[5] = "0";
        }

    }






    //Dialog thrown by time picker
    TimePickerDialog.OnTimeSetListener medicationTime=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            AMorPM="AM";
            s_hour=hourOfDay;
            s_minute=minute;
            String minuteS=String.valueOf(minute);
            if(hourOfDay>12)
            {
                Integer value=hourOfDay-12;
                hourOfDay=value;
                AMorPM="PM";

            }
            if(hourOfDay==12)
            {
                AMorPM="PM";
            }
            if(hourOfDay==24)
            {
                AMorPM="AM";
            }

            if(minute<10)
            {
                time.setText(String.valueOf(hourOfDay)+":0"+minuteS+":"+AMorPM);
            }
            else {
                time.setText(String.valueOf(hourOfDay)+":"+minuteS+":"+AMorPM);
            }


        }
    };









    //Default time picker
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }








    //Date picker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c=Calendar.getInstance();
        c.set(c.YEAR,year);
        c.set(c.MONTH,month);
        c.set(c.DAY_OF_MONTH,dayOfMonth);
        startingDate=(TextView)findViewById(R.id.date);
        this.month=month;
        this.year=year;
        this.dayOfMonth=dayOfMonth;
        startingDate.setText(dayOfMonth+" : "+(month+1)+" : "+year);
    }







    //Checks type of alarm Everyday or Custom or today
    public String checkAlarmType()
    {
        String returnValue="";
        if(days[0]=="1"&&days[1]=="1"&&days[2]=="1"&&days[3]=="1"&&days[4]=="1"&&days[5]=="1"&&days[6]=="1")
        {
            returnValue="Everyday";
        }
        else if(days[0]=="0"&&days[1]=="0"&&days[2]=="0"&&days[3]=="0"&&days[4]=="0"&&days[5]=="0"&&days[6]=="0")
        {
            returnValue="";
        }
        else{
            returnValue="custom or today";
        }
        return returnValue;
    }






    //get the name of day of week
    public String getNameOfDays()
    {
        String names="";
        for(int i=0;i<7;i++)
        {
            if(days[i]=="1")
            {
                if(i==0)
                {
                    names+="Sun ";
                }
                if(i==1)
                {
                    names+="Mon ";
                }
                if(i==2)
                {
                    names+="Tue ";
                }
                if(i==3)
                {
                    names+="Wed ";
                }
                if(i==4)
                {
                    names+="Thu ";
                }
                if(i==5)
                {
                    names+="Fri ";
                }
                if(i==6)
                {
                    names+="Sat ";
                }
            }
        }
        return  names;
    }






    //Checks the number of alarm if alarm type is custom or today
    public int getNumberOfAlarm()
    {
        int num=0;
        for(int i=0;i<7;i++)
        {
            if(days[i]=="1")
            {
                num=num+1;
            }
        }
        return num;
    }
}
