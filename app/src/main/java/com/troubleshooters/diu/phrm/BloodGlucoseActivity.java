package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.Helper.LocaleHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class BloodGlucoseActivity extends AppCompatActivity {

    EditText glucoseLevel1, glucoseLevel2;
    Button b, addToRecord;
    Spinner diabetics;
    ArrayAdapter<CharSequence> adapter2;

    String diabeticsStatus, fastingStatus, afterMealStatus;
    Double glucose1, glucose2;

    String month_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_glucose);

        diabetics=(Spinner)findViewById(R.id.spinner_select_diabetics_status);
        adapter2=ArrayAdapter.createFromResource(this,R.array.diabetics_status,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diabetics.setAdapter(adapter2);
        glucoseLevel1=(EditText)findViewById(R.id.glucose_fasting);
        glucoseLevel2 = (EditText)findViewById(R.id.glucose_postfasting);
        b=(Button)findViewById(R.id.cal);
        addToRecord = (Button)findViewById(R.id.add_record_button);
        addToRecord.setVisibility(View.GONE);


        //To override the Bangla language issue in the exercise barchart.
        Paper.init(this);
        String language = Paper.book().read("language");
        if(language.equals("bn")){
            Paper.book().write("language", "en");
            updateView((String)Paper.book().read("language"));

            //getting date
            Calendar cal=Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMM-dd");
            month_name = month_date.format(cal.getTime());

            Paper.book().write("language", "bn");
            updateView((String)Paper.book().read("language"));
        }
        else {
            //getting date
            Calendar cal=Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMM-dd");
            month_name = month_date.format(cal.getTime());
        }



        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        android.support.v7.app.AlertDialog.Builder builder;
                        fastingStatus="Not done";
                        afterMealStatus="Not done";
                        if(diabetics.getSelectedItem().toString().equals("With Diabetes"))
                        {
                            if(glucoseLevel1.getText().toString().equals("")&&glucoseLevel2.getText().toString().equals("")){
                                Toast.makeText(BloodGlucoseActivity.this, "Please Input Values First!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!glucoseLevel1.getText().toString().equals("")){
                                glucose1=Double.valueOf(glucoseLevel1.getText().toString());
                                if(glucose1>1&&glucose1<4){
                                    fastingStatus="Low";
                                }
                                else if (glucose1>=4&&glucose1<=7){
                                    fastingStatus="Normal";
                                }
                                else if (glucose1>7&&glucose1<10){
                                    fastingStatus="Bit high";
                                }
                                else if (glucose1>=10&&glucose1<16){
                                    fastingStatus="High";
                                }
                                else if (glucose1>=16&&glucose1<30){
                                    fastingStatus="Dangerously high";
                                }
                                else{
                                    Toast.makeText(BloodGlucoseActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(!glucoseLevel2.getText().toString().equals("")){
                                glucose2=Double.valueOf(glucoseLevel2.getText().toString());
                                if(glucose2>1&&glucose2<4){
                                    afterMealStatus="Low";
                                }
                                else if (glucose2>=4&&glucose2<=8.5){
                                    afterMealStatus="Normal";
                                }
                                else if (glucose2>8.5&&glucose2<12){
                                    afterMealStatus="Bit high";
                                }
                                else if (glucose2>=12&&glucose2<17){
                                    afterMealStatus="High";
                                }
                                else if (glucose2>=17&&glucose2<30){
                                    afterMealStatus="Dangerously high";
                                }
                                else{
                                    Toast.makeText(BloodGlucoseActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(glucose1>glucose2){
                                Toast.makeText(BloodGlucoseActivity.this, "Abnormal Values!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                addToRecord.setVisibility(View.VISIBLE);
                                builder = new android.support.v7.app.AlertDialog.Builder(BloodGlucoseActivity.this, R.style.CustomDialogTheme);
                                builder.setMessage("Your glucose level is,\nwhile fasting: "+fastingStatus+"\n2 Hrs after meal: "+afterMealStatus)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }


                        }
                        else if (diabetics.getSelectedItem().toString().equals("Without Diabetes")){

                            if(glucoseLevel1.getText().toString().equals("")&&glucoseLevel2.getText().toString().equals("")){
                                Toast.makeText(BloodGlucoseActivity.this, "Please Input Values First!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!glucoseLevel2.getText().toString().equals("")){
                                glucose1=Double.valueOf(glucoseLevel2.getText().toString());
                                if(glucose1>1&&glucose1<4){
                                    fastingStatus="Low";
                                }
                                else if (glucose1>=4&&glucose1<=5.9){
                                    fastingStatus="Normal";
                                }
                                else if (glucose1>=6&&glucose1<20){
                                    fastingStatus="High";
                                }
                                else{

                                }
                            }
                            if(!glucoseLevel2.getText().toString().equals("")){
                                glucose2=Double.valueOf(glucoseLevel2.getText().toString());
                                if(glucose2>1&&glucose2<4){
                                    afterMealStatus="Low";
                                }
                                else if (glucose2>=4&&glucose2<=7.8){
                                    afterMealStatus="Normal";
                                }
                                else if (glucose2>7.8&&glucose2<20){
                                    afterMealStatus="High";
                                }
                                else{
                                    Toast.makeText(BloodGlucoseActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(glucose1>glucose2){
                                Toast.makeText(BloodGlucoseActivity.this, "Abnormal Values!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                addToRecord.setVisibility(View.VISIBLE);
                                builder = new android.support.v7.app.AlertDialog.Builder(BloodGlucoseActivity.this, R.style.CustomDialogTheme);
                                builder.setMessage("Your glucose level is,\nwhile fasting: "+fastingStatus+"\n2 Hrs after meal: "+afterMealStatus)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                        }
                        else {
                            Toast.makeText(BloodGlucoseActivity.this, "All fields are not set", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        addToRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(glucoseLevel1.getText().toString().equals("")&&glucoseLevel2.getText().toString().equals("")){
                    Toast.makeText(BloodGlucoseActivity.this, "Please enter all thae values to add record.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String fastingVal = glucoseLevel1.getText().toString();
                    String afterMealVal = glucoseLevel2.getText().toString();
                    if(fastingVal.length()==1){
                        fastingVal="000"+fastingVal;
                    }
                    else if(fastingVal.length()==2){
                        fastingVal="00"+fastingVal;
                    }
                    else if(fastingVal.length()==3){
                        fastingVal="0"+fastingVal;
                    }
                    else {

                    }
                    if(afterMealVal.length()==1){
                        afterMealVal="000"+afterMealVal;
                    }
                    else if (afterMealVal.length()==2){
                        afterMealVal="00"+afterMealVal;
                    }
                    else if (afterMealVal.length()==3){
                        afterMealVal="0"+afterMealVal;
                    }
                    else {

                    }


                    String entry = month_name+fastingVal+afterMealVal;

                    //storing
                    SharedPreferences bloodGlucoseData = getSharedPreferences("BloodGlucoseData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor bgDataEditor = bloodGlucoseData.edit();
                    String bgString = bloodGlucoseData.getString("bgString", "");
                    if(bgString==""){
                        bgDataEditor.putString("bgString", entry);
                        bgDataEditor.commit();
                    }
                    else{
                        if(bgString.length()==210){
                            bgString=bgString.substring(14,210)+entry;
                        }
                        else {
                            bgString=bgString+entry;
                        }
                        bgDataEditor.putString("bgString", bgString);
                        bgDataEditor.commit();
                    }
                    addToRecord.setVisibility(View.GONE);
                    Toast.makeText(BloodGlucoseActivity.this, "Data successfully stored.", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    //Updating language change
    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();

    }
}
