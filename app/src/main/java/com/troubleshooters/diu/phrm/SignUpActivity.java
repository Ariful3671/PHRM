package com.troubleshooters.diu.phrm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button createProfile;
    EditText userNameField, phoneField, passwordField;
    TextView dobField, genderField;
    DatePickerDialog.OnDateSetListener userDOBSetter;
    RequestQueue requestQueue;

    String createProfileInsertlink = "http://phrmweb.ml/phrm_api/insert_userData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        userNameField = (EditText)findViewById(R.id.edit_text_userName_sign_up_screen);
        phoneField = (EditText)findViewById(R.id.edit_text_userPhone_sign_up_screen);
        passwordField = (EditText)findViewById(R.id.edit_text_password_sign_up_screen);
        dobField = (TextView) findViewById(R.id.edit_text_userDOB_sign_up_screen);
        genderField = (TextView)findViewById(R.id.edit_text_userGender_sign_up_screen);

        createProfile=(Button)findViewById(R.id.button_create_profile_sign_up_screen);

        requestQueue = Volley.newRequestQueue(this);

        genderField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder genderPickerDialog = new AlertDialog.Builder(SignUpActivity.this);
                final View genderPickerDialogView = getLayoutInflater().inflate(R.layout.gender_picker_dialog, null);
                TextView genderMale = (TextView) genderPickerDialogView.findViewById(R.id.maleGenderText);
                TextView genderFemale = (TextView) genderPickerDialogView.findViewById(R.id.femaleGenderText);
                genderPickerDialog.setCancelable(false);

                genderPickerDialog.setView(genderPickerDialogView);
                final AlertDialog genderDialog = genderPickerDialog.create();
                Window genderDialogWindow = genderDialog.getWindow();
                genderDialogWindow.setGravity(Gravity.BOTTOM);
                genderDialog.show();

                genderMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genderField.setText("Male");
                        genderDialog.cancel();

                    }
                });

                genderFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genderField.setText("Female");
                        genderDialog.cancel();
                    }
                });

            }
        });

        dobField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog DPDialog = new DatePickerDialog(SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar, userDOBSetter, year, month, day);
                DPDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window DPDialogWindow =DPDialog.getWindow();
                DPDialogWindow.setGravity(Gravity.BOTTOM);
                DPDialog.show();
            }
        });

        userDOBSetter = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date = year + "-" + month + "-" + dayOfMonth;
                dobField.setText(date);
            }
        };

        createProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 StringRequest createProfileInsertRequest = new StringRequest(Request.Method.POST, createProfileInsertlink, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         Toast toast = Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT);
                         toast.show();
                         Intent goToLogin = new Intent(SignUpActivity.this, LogInActivity.class);
                         startActivity(goToLogin);
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Toast toast = Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT);
                         toast.show();
                     }
                 }){
                     @Override
                     protected Map<String, String> getParams()
                     {
                         Map<String, String> insertParameters = new HashMap<String, String>();
                         insertParameters.put("userName", userNameField.getText().toString());
                         insertParameters.put("phone", phoneField.getText().toString());
                         insertParameters.put("password", passwordField.getText().toString());
                         insertParameters.put("dob", dobField.getText().toString());
                         insertParameters.put("gender", genderField.getText().toString());
                         return insertParameters;
                     }
                 };
                 requestQueue.add(createProfileInsertRequest);

             }
         }
        );
    }
}
