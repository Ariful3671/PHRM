package com.troubleshooters.diu.phrm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    EditText name,phone,password;
    TextView DOB,gender;
    Button signup;
    ProgressBar progressBar;
    TextView signingin;

    String userBDay;
    String userBMonth;
    String userByear;


    NetworkChecker networkChecker;//To check inter net connection



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();//hide actionbar

        name=(EditText)findViewById(R.id.edit_text_userName_sign_up_screen);
        phone=(EditText)findViewById(R.id.edit_text_userPhone_sign_up_screen);
        password=(EditText)findViewById(R.id.edit_text_password_sign_up_screen);
        DOB=(TextView)findViewById(R.id.edit_text_userDOB_sign_up_screen);
        gender=(TextView)findViewById(R.id.edit_text_userGender_sign_up_screen);
        signup=(Button)findViewById(R.id.button_create_profile_sign_up_screen);
        signingin=(TextView)findViewById(R.id.signing_in_text_view);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar_sign_up);
        networkChecker=new NetworkChecker(SignUpActivity.this);







        //Create a date picker
        DOB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DOB.setError(null);
                        DialogFragment datePicker=new DatePickerFragment();
                        datePicker.show(getFragmentManager(),"date picker");
                    }
                }
        );






        //Select gender from dialog
        gender.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gender.setError(null);
                        android.support.v7.app.AlertDialog.Builder builder;
                        builder = new android.support.v7.app.AlertDialog.Builder(SignUpActivity.this,R.style.CustomDialogTheme);
                        View mview=getLayoutInflater().inflate(R.layout.gender_picker_dialog,null);
                        final RadioGroup RG=(RadioGroup)mview.findViewById(R.id.RG_gender);
                        builder.setView(mview);
                        builder.setMessage("Select your gender")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id=RG.getCheckedRadioButtonId();
                                        if(id==R.id.RB_male)
                                        {
                                            gender.setText("Male");
                                        }
                                        if(id==R.id.RB_female)
                                        {
                                            gender.setText("Female");
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                    }
                }
        );






       //Listener for signup button
        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(networkChecker.isConnected())
                        {
                            signup.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            signingin.setVisibility(View.VISIBLE);

                            if(name.getText().toString().trim().equals(""))
                            {
                                name.setError("Please set a username");
                                signup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                signingin.setVisibility(View.GONE);
                            }
                            if(phone.getText().toString().trim().equals(""))
                            {
                                phone.setError("Please enter your phone number");
                                signup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                signingin.setVisibility(View.GONE);
                            }
                            if(password.getText().toString().trim().equals(""))
                            {
                                password.setError("Please set a password");
                                signup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                signingin.setVisibility(View.GONE);
                            }
                            if(DOB.getText().toString().trim().equals(""))
                            {
                                DOB.setError("Please set your birth date");
                                signup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                signingin.setVisibility(View.GONE);
                            }
                            if(gender.getText().toString().trim().equals(""))
                            {
                                gender.setError("Please select your gender");
                                signup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                signingin.setVisibility(View.GONE);
                            }
                            if(!name.getText().toString().trim().equals("")&&!phone.getText().toString().trim().equals("")&&!password.getText().toString().trim().equals("")&&!DOB.getText().toString().trim().equals("")&&!gender.getText().toString().trim().equals("")) {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("users");
                                final String user_name = name.getText().toString().toLowerCase().trim();
                                final String user_phone = phone.getText().toString().trim();
                                final String user_gender = gender.getText().toString();
                                final String user_password = password.getText().toString().trim();
                                boolean status = false;
                                ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild(user_name))
                                                {
                                                    name.setError("Username already exist");
                                                    Toast.makeText(SignUpActivity.this, "Username already exist", Toast.LENGTH_SHORT).show();
                                                    //name.setTextColor(Color.parseColor("#C80000"));
                                                    signup.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.GONE);
                                                    signingin.setVisibility(View.GONE);
                                                }
                                                else
                                                {
                                                    Toast.makeText(SignUpActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                                                    Map<String, String> userData = new HashMap<String, String>();
                                                    userData.put("phone",user_phone);
                                                    userData.put("password", user_password);
                                                    userData.put("bday", userBDay);
                                                    userData.put("bmonth", userBMonth);
                                                    userData.put("byear", userByear);
                                                    userData.put("gender", user_gender);
                                                    DatabaseReference userRef = database.getReference("users").child(user_name);
                                                    userRef.setValue(userData);
                                                    Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        }
                                );
                            }
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

    }









    //Select date and show it to textview
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c=Calendar.getInstance();
        c.set(c.YEAR,year);
        c.set(c.MONTH,month);
        c.set(c.DAY_OF_MONTH,dayOfMonth);
        DOB=(TextView)findViewById(R.id.edit_text_userDOB_sign_up_screen);
        userBDay=String.valueOf(dayOfMonth);
        userBMonth=String.valueOf(month+1);
        userByear=String.valueOf(year);
        DOB.setText(dayOfMonth+" : "+(month+1)+" : "+year);
    }
}
