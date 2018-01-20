package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.troubleshooters.diu.phrm.Adapter.TestRecordGridAdapter;

import org.w3c.dom.Text;

public class LogInActivity extends AppCompatActivity {


    Button buttonLogIn,buttoSignUp;
    EditText userid,password;
    ProgressBar progressBar;
    TextView logginngin,forgetPassword;
    CheckBox c;
    String user,pass;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    NetworkChecker networkChecker;//check box for save password dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();
        buttoSignUp=(Button)findViewById(R.id.button_sign_up);
        buttonLogIn=(Button)findViewById(R.id.button_log_in);
        userid=(EditText)findViewById(R.id.edit_text_User_Id_log_in_screen);
        password=(EditText)findViewById(R.id.edit_text_password_log_in_screen);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar_log_in);
        logginngin=(TextView)findViewById(R.id.text_view_logging_in);
        forgetPassword=(TextView)findViewById(R.id.textview_forget_password);
        c=(CheckBox)findViewById(R.id.checkbox_save_password);//check box for save password dialog
        networkChecker=new NetworkChecker(this);//For checking network connection







        final SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();







        //Checking password is saved or not
        if(sharedPreferences.getString("save password status","").equals("yes"))
        {
            userid.setText(sharedPreferences.getString("userid",""));
            password.setText(sharedPreferences.getString("password",""));
        }






        //Go to signup activity
        buttoSignUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent signUpIntent=new Intent(LogInActivity.this,SignUpActivity.class);
                        startActivity(signUpIntent);
                    }
                }

        );






        //Log in on click listener
        buttonLogIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {


                        //if network connection is on
                        if(networkChecker.isConnected())
                        {
                            buttonLogIn.setVisibility(View.GONE);
                            forgetPassword.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);//loading bar
                            logginngin.setVisibility(View.VISIBLE);

                            user = userid.getText().toString().trim().toLowerCase();
                            pass = password.getText().toString();

                            if (user.equals("") || pass.equals("")) {
                                Toast.makeText(LogInActivity.this, "Please insert your username and password", Toast.LENGTH_SHORT).show();
                                logginngin.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                buttonLogIn.setVisibility(View.VISIBLE);
                                forgetPassword.setVisibility(View.VISIBLE);
                            } else {

                                if(sharedPreferences.getString("save password status","").equals("yes"))
                                {
                                    String preference_userid=sharedPreferences.getString("userid","");
                                    String preference_password=sharedPreferences.getString("password","");
                                    if(!preference_userid.equals(user))
                                    {
                                        editor.putString("save password status","no");
                                        editor.putString("save password dialog status","yes");
                                    }
                                }
                                if(sharedPreferences.getString("save password status","").equals("no"))
                                {
                                    if(sharedPreferences.getString("save password dialog status","").equals("no"))
                                    {
                                        String preference_userid=sharedPreferences.getString("userid","");
                                        String preference_password=sharedPreferences.getString("password","");
                                        if(!preference_userid.equals(user))
                                        {
                                            editor.putString("save password status","no");
                                            editor.putString("save password dialog status","yes");
                                        }
                                    }

                                }




                                //Checking username and password validation
                                DatabaseReference ref = database.getReference("users");
                                ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(user)) {
                                                    DatabaseReference ref = database.getReference("users");
                                                    DatabaseReference ref2 = ref.child(user);

                                                    ref2.addListenerForSingleValueEvent(
                                                            new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    String actual_password = dataSnapshot.child("password").getValue().toString();

                                                                    if (actual_password.equals(pass)) {
                                                                        editor.putString("userid",user);
                                                                        editor.putString("password",actual_password);
                                                                        editor.commit();
                                                                        if(sharedPreferences.getString("save password dialog status","").equals("")||sharedPreferences.getString("save password dialog status","").equals("yes")) {

                                                                            android.support.v7.app.AlertDialog.Builder builder;
                                                                            builder = new android.support.v7.app.AlertDialog.Builder(LogInActivity.this, R.style.CustomDialogTheme);
                                                                            View mview = getLayoutInflater().inflate(R.layout.save_password_dialog, null);
                                                                            builder.setView(mview);
                                                                            builder.setMessage("Save username and password")
                                                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            editor.putString("save password status", "yes");
                                                                                            editor.putString("save password dialog status", "no");
                                                                                            editor.putString("logout status", "no");
                                                                                            editor.commit();
                                                                                            //Toast.makeText(LogInActivity.this, sharedPreferences.getString("userid","").toString()+"  "+sharedPreferences.getString("password","").toString() , Toast.LENGTH_SHORT).show();
                                                                                            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                                                                            startActivity(intent);
                                                                                            finish();

                                                                                        }
                                                                                    })
                                                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            editor.putString("logout status", "no");
                                                                                            editor.putString("save password status", "no");
                                                                                            editor.commit();
                                                                                            //Toast.makeText(LogInActivity.this, sharedPreferences.getString("userid","").toString()+"  "+sharedPreferences.getString("password","").toString() , Toast.LENGTH_SHORT).show();
                                                                                            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                                                                            startActivity(intent);
                                                                                            finish();

                                                                                        }
                                                                                    }).show();
                                                                        }
                                                                        else{
                                                                            editor.putString("logout status", "no");
                                                                            editor.commit();
                                                                            //Toast.makeText(LogInActivity.this, sharedPreferences.getString("userid","").toString()+"  "+sharedPreferences.getString("password","").toString() , Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }

                                                                    } else {
                                                                        Toast.makeText(LogInActivity.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                                                                        logginngin.setVisibility(View.GONE);
                                                                        progressBar.setVisibility(View.GONE);
                                                                        buttonLogIn.setVisibility(View.VISIBLE);
                                                                        forgetPassword.setVisibility(View.VISIBLE);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            }
                                                    );
                                                } else {
                                                    Toast.makeText(LogInActivity.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                                                    logginngin.setVisibility(View.GONE);
                                                    progressBar.setVisibility(View.GONE);
                                                    buttonLogIn.setVisibility(View.VISIBLE);
                                                    forgetPassword.setVisibility(View.VISIBLE);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }

                                );

                            }
                        }
                        else{
                            Toast.makeText(LogInActivity.this, "Please check your network connection!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


        );
    }








    //Function for checkbox
    public void getStatus(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

                if(((CheckBox)v).isChecked())
                {
                    editor.putString("save password dialog status", "no");
                    editor.commit();
                }
                else
                {
                    editor.putString("save password dialog status", "yes");
                    editor.commit();
                }

    }
}
