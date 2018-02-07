package com.troubleshooters.diu.phrm;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.troubleshooters.diu.phrm.Adapter.ProfileAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ListView list_view_profile;


    int icon[]={R.drawable.ic_action_name,R.drawable.phone_icon,R.drawable.ic_birthday_cake,R.drawable.ic_birthday_signup,R.drawable.ic_height,R.drawable.ic_weight,R.drawable.ic_gender,R.drawable.ic_action_activity,R.drawable.ic_action_bmi};
    String[] options={"Nickname","Phone","Birthday"," Age"," Height"," Weight"," Gender"," Activity level"," BMI"};
    String[] value={"Set","Set","Set","Set","Set","Set","Set","Set","Set"};
    int right_arrow[]={R.drawable.ic_action_right_arrow};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        list_view_profile=(ListView)findViewById(R.id.list_view_profile);

        //setting adapter for profile
        final ProfileAdapter adapter=new ProfileAdapter(options,value,icon,right_arrow,ProfileActivity.this);
        list_view_profile.setAdapter(adapter);

        //setting profile information(on click for profile listview)
        list_view_profile.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor=sharedPreferences.edit();
                final String user=sharedPreferences.getString("userid","");
                final DatabaseReference ref=database.getReference("users").child(user);
                AlertDialog.Builder builder;
                View mview;
                final TextView text_view_custom_dialog;
                switch (position)
                {
                    case 0:
                        builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                        mview=getLayoutInflater().inflate(R.layout.custom_dialog,null);
                        text_view_custom_dialog=(TextView)mview.findViewById(R.id.text_view_custom_dialog);
                        text_view_custom_dialog.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_action_name, 0, 0, 0);
                        text_view_custom_dialog.setHint("Name");
                        builder.setView(mview);
                        builder.setMessage("Enter your name")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!text_view_custom_dialog.getText().toString().trim().equals(""))
                                        {
                                            String name=text_view_custom_dialog.getText().toString().trim();
                                            ref.child("name").setValue(name);
                                            editor.putString("name",name);
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                        break;
                    case 1:
                        builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                        mview=getLayoutInflater().inflate(R.layout.custom_dialog,null);
                        text_view_custom_dialog=(TextView)mview.findViewById(R.id.text_view_custom_dialog);
                        text_view_custom_dialog.setCompoundDrawablesWithIntrinsicBounds( R.drawable.phone_icon, 0, 0, 0);
                        text_view_custom_dialog.setHint("Phone");
                        text_view_custom_dialog.setInputType(InputType.TYPE_CLASS_PHONE);
                        builder.setView(mview);
                        builder.setMessage("Enter your Phone number")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!text_view_custom_dialog.getText().toString().trim().equals(""))
                                        {
                                            String name=text_view_custom_dialog.getText().toString().trim();
                                            ref.child("phone").setValue(name);
                                            editor.putString("phone",name);
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                        break;
                    case 2:

                        DialogFragment datePicker=new DatePickerFragment();
                        datePicker.show(getFragmentManager(),"date picker");
                        break;
                    case 3:
                        Toast.makeText(ProfileActivity.this, "Please enter your birthday to change your age!", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                        mview=getLayoutInflater().inflate(R.layout.custom_dialog,null);
                        text_view_custom_dialog=(TextView)mview.findViewById(R.id.text_view_custom_dialog);
                        text_view_custom_dialog.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_height, 0, 0, 0);
                        text_view_custom_dialog.setHint("Height(cm)");
                        text_view_custom_dialog.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(mview);
                        builder.setMessage("Enter your height(cm)")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!text_view_custom_dialog.getText().toString().trim().equals(""))
                                        {
                                            String name=text_view_custom_dialog.getText().toString().trim();
                                            ref.child("height").setValue(name);
                                            editor.putString("height",name);
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                        break;
                    case 5:
                        builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                        mview=getLayoutInflater().inflate(R.layout.custom_dialog,null);
                        text_view_custom_dialog=(TextView)mview.findViewById(R.id.text_view_custom_dialog);
                        text_view_custom_dialog.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_height, 0, 0, 0);
                        text_view_custom_dialog.setHint("Weight(kg)");
                        text_view_custom_dialog.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(mview);
                        builder.setMessage("Enter your weight(kg)")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!text_view_custom_dialog.getText().toString().trim().equals(""))
                                        {
                                            String name=text_view_custom_dialog.getText().toString().trim();
                                            ref.child("weight").setValue(name);
                                            editor.putString("weight",name);
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                        break;
                    case 6:
                        builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                        mview=getLayoutInflater().inflate(R.layout.gender_picker_dialog,null);
                        final RadioGroup RG=(RadioGroup)mview.findViewById(R.id.RG_gender);
                        builder.setView(mview);
                        builder.setMessage("Select your gender")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id=RG.getCheckedRadioButtonId();
                                        if(id==R.id.RB_male)
                                        {
                                            ref.child("gender").setValue("Male");
                                            editor.putString("gender","Male");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }
                                        if(id==R.id.RB_female)
                                        {
                                            ref.child("gender").setValue("Female");
                                            editor.putString("gender","Female");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();

                        break;
                    case 7:

                        builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                        mview=getLayoutInflater().inflate(R.layout.dialog_activity_level,null);
                        final RadioGroup RGA=(RadioGroup)mview.findViewById(R.id.RG_activity);
                        builder.setView(mview);
                        builder.setMessage("Select your activity level")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id=RGA.getCheckedRadioButtonId();
                                        if(id==R.id.RB_little)
                                        {
                                            ref.child("activity").setValue("Little");
                                            editor.putString("activity","Little");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }
                                        if(id==R.id.RB_light)
                                        {
                                            ref.child("activity").setValue("Light");
                                            editor.putString("activity","Light");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }
                                        if(id==R.id.RB_moderate)
                                        {
                                            ref.child("activity").setValue("moderate");
                                            editor.putString("activity","Moderate");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }
                                        if(id==R.id.RB_heavy)
                                        {
                                            ref.child("activity").setValue("Heavy");
                                            editor.putString("activity","Heavy");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }
                                        if(id==R.id.RB_very_heavy)
                                        {
                                            ref.child("activity").setValue("Very heavy");
                                            editor.putString("activity","Very heavy");
                                            editor.commit();
                                            adapter.notifyDataSetChanged();
                                            list_view_profile.setAdapter(adapter);
                                        }

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();

                        break;


                }
                }
            }
        );
    }



    //date picker for birthday in profile
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c=Calendar.getInstance();
        c.set(c.YEAR,year);
        c.set(c.MONTH,month);
        c.set(c.DAY_OF_MONTH,dayOfMonth);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final String user=sharedPreferences.getString("userid","");
        final DatabaseReference ref=database.getReference("users").child(user);

        ref.child("bday").setValue(String.valueOf(dayOfMonth));
        ref.child("bmonth").setValue(String.valueOf(month+1));
        ref.child("byear").setValue(String.valueOf(year));


        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int age=currentYear-year;
        if((currentMonth-(month+1))<0)
        {
            age--;
        }
        if(currentMonth-(month+1)==0)
        {
            if(currentDay-dayOfMonth<0)
            {
                age--;
            }
        }
        editor.putString("birthday",String.valueOf(dayOfMonth)+":"+String.valueOf(month+1)+":"+String.valueOf(year));
        editor.putString("age",String.valueOf(age));
        editor.commit();
        final ProfileAdapter adapter=new ProfileAdapter(options,value,icon,right_arrow,ProfileActivity.this);
        list_view_profile.setAdapter(adapter);



    }

}
