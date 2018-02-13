package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.troubleshooters.diu.phrm.Helper.LocaleHelper;

import java.util.ArrayList;
import java.util.Arrays;

import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity {

    ListView list_settings;


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings_title));

        //code by Tanin
        Paper.init(this);

        updateView((String)Paper.book().read("language"));

        list_settings=(ListView)findViewById(R.id.list_sittings);
        String[] settingItems = getResources().getStringArray(R.array.settings_item);
        ArrayAdapter<String> adapter;
        adapter=new ArrayAdapter<String>(SettingsActivity.this,android.R.layout.simple_list_item_1,settingItems);
        list_settings.setAdapter(adapter);



        list_settings.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (position)
                    {
                        case 0:
                            SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            final DatabaseReference ref=database.getReference("users").child(sharedPreferences.getString("userid","")).child("password");
                            AlertDialog.Builder builder;
                            View mview;
                            final TextView text_view_custom_dialog;
                            builder = new AlertDialog.Builder(SettingsActivity.this,R.style.CustomDialogTheme);
                            mview=getLayoutInflater().inflate(R.layout.custom_dialog,null);
                            text_view_custom_dialog=(TextView)mview.findViewById(R.id.text_view_custom_dialog);
                            text_view_custom_dialog.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_action_lock, 0, 0, 0);
                            text_view_custom_dialog.setHint("");
                            builder.setView(mview);
                            builder.setMessage("Enter your new password")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!text_view_custom_dialog.getText().toString().equals(""))
                                        {
                                            ref.setValue(text_view_custom_dialog.getText().toString());
                                            Toast.makeText(SettingsActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
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
                            android.support.v7.app.AlertDialog.Builder languageDialogBuilder;
                            languageDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this,R.style.CustomDialogTheme);
                            View lview=getLayoutInflater().inflate(R.layout.language_picker_dialog,null);
                            final RadioGroup RG=lview.findViewById(R.id.RG_language_picker);
                            languageDialogBuilder.setView(lview);
                            languageDialogBuilder.setMessage("Select Language")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    int id=RG.getCheckedRadioButtonId();
                                    if(id==R.id.RB_English)
                                    {
                                        Paper.book().write("language", "en");
                                        updateView((String)Paper.book().read("language"));
                                        finish();
                                        overridePendingTransition( 0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition( 0, 0);
                                        HomeActivity.home.finish();
                                    }
                                    if(id==R.id.RB_Bangla)
                                    {
                                        Paper.book().write("language", "bn");
                                        updateView((String)Paper.book().read("language"));
                                        finish();
                                        overridePendingTransition( 0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition( 0, 0);
                                        HomeActivity.home.finish();
                                    }
                                    //Toast.makeText(SettingsActivity.this, "Please restart the app!", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SettingsActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();

    }
}
