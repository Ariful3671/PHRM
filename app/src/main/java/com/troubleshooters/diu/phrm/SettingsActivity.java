package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    ListView list_settings;
    String setting_items[]={"Change password"};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        list_settings=(ListView)findViewById(R.id.list_sittings);
        adapter=new ArrayAdapter<String>(SettingsActivity.this,android.R.layout.simple_list_item_1,setting_items);
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

                        }

                    }
                }
        );


    }
}
