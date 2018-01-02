package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.troubleshooters.diu.phrm.R;

import java.text.DecimalFormat;

/**
 * Created by Arif on 01-01-18.
 */

public class ProfileAdapter extends BaseAdapter {

    String option[];
    String value[];
    int icon[];
    int right_arrow[];
    Context context;
    LayoutInflater inflater;

    public ProfileAdapter(String option[], String value[], int icon[], int right_arrow[], Context context)
    {
        this.option=option;
        this.value=value;
        this.icon=icon;
        this.right_arrow=right_arrow;
        this.context=context;
    }
    @Override
    public int getCount() {
        return option.length;
    }

    @Override
    public Object getItem(int position) {
        return option[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View listView = view;
        if (view == null) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.custom_list_view, null);


            SharedPreferences sharedPreferences=context.getSharedPreferences("profileinfo",Context.MODE_PRIVATE);

            TextView text_view_option = (TextView) listView.findViewById(R.id.text_view_option);
            TextView text_view_value = (TextView) listView.findViewById(R.id.text_view_value);
            if(position==0&&!sharedPreferences.getString("name","").equals(""))
            {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("name","").toString());
            }
            else if(position==1&&!sharedPreferences.getString("phone","").equals(""))
            {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("phone","").toString());
            }
            else if(position==2&&!sharedPreferences.getString("birthday","").equals(""))
            {
                text_view_option.setText(option[position]);
                String birthday=sharedPreferences.getString("birthday","").toString();
                text_view_value.setText(birthday);
            }
            else if(position==3&&!sharedPreferences.getString("age","").equals(""))
            {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("age","").toString());
            }
            else if(position==4&&!sharedPreferences.getString("height","").equals(""))
            {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("height","").toString());
            }
            else if(position==5&&!sharedPreferences.getString("weight","").equals(""))
            {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("weight","").toString());
            }
            else if(position==6&&!sharedPreferences.getString("gender","").equals(""))
            {
                text_view_option.setText(option[6]);
                text_view_value.setText(sharedPreferences.getString("gender","").toString());
            }
            else if(position==7&&!sharedPreferences.getString("activity","").equals(""))
            {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("activity","").toString());
            }
            else {
                text_view_option.setText(option[position]);
                text_view_value.setText(value[position]);
            }

            text_view_option.setCompoundDrawablesWithIntrinsicBounds(icon[position],0,0,0);
            text_view_value.setCompoundDrawablesWithIntrinsicBounds(0,0,right_arrow[0],0);


        }

        return listView;
    }
}
