package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arif on 06-02-18.
 */

public class ReminderAdapter extends BaseAdapter{

    String option[];
    String value[];
    int arrow[];
    Context context;

    public ReminderAdapter(String option[],String value[],int arrow[],Context context) {
        this.option=option;
        this.value=value;
        this.arrow=arrow;
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
    public View getView(int position, View view, ViewGroup parent) {
        View listView = view;
        if (view == null) {

            LayoutInflater inflater;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.custom_list_view, null);
            TextView text_view_option = (TextView) listView.findViewById(R.id.text_view_option);
            TextView text_view_value = (TextView) listView.findViewById(R.id.text_view_value);
            SharedPreferences sharedPreferences_time=context.getSharedPreferences("remindertime",Context.MODE_PRIVATE);


            if(position==0)
            {
                if(sharedPreferences_time.getString("hour","").equals(""))
                {
                    text_view_option.setText(option[position]);
                    text_view_value.setText(value[position]);
                }
                else{
                    //Toast.makeText(context, sharedPreferences_time.getString("hour","")+"  "+sharedPreferences_time.getString("minute",""), Toast.LENGTH_SHORT).show();
                    String shour=sharedPreferences_time.getString("hour","");
                    String sminute=sharedPreferences_time.getString("minute","");
                    Integer hour= Integer.valueOf(shour);
                    Integer minute=Integer.valueOf(sminute);
                    String AMorPM="AM";


                    if(hour>12)
                    {
                        Integer value=hour-12;
                        hour=value;
                        AMorPM="PM";
                    }
                    if(minute<10)
                    {
                        text_view_option.setText(option[position]);
                        text_view_value.setText(String.valueOf(hour)+":0"+String.valueOf(minute)+AMorPM);
                    }
                    else{
                        text_view_option.setText(option[position]);
                        text_view_value.setText(String.valueOf(hour)+":"+String.valueOf(minute)+AMorPM);
                    }



                }
            }
            if(position==1)
            {
                if(sharedPreferences_time.getString("day","").equals(""))
                {
                    text_view_option.setText(option[position]);
                    text_view_value.setText(value[position]);
                }
                else{
                    String sday=sharedPreferences_time.getString("day","");
                    String smonth=sharedPreferences_time.getString("month","");
                    String syear=sharedPreferences_time.getString("year","");
                    Integer day= Integer.valueOf(sday);
                    Integer month=Integer.valueOf(smonth)+1;
                    Integer year=Integer.valueOf(syear);
                    text_view_option.setText(option[position]);
                    if(day<10&&month<10)
                    {
                        text_view_value.setText("0"+String.valueOf(day)+":"+"0"+String.valueOf(month)+":"+String.valueOf(year));

                    }
                    else if(day<10&&month>=10)
                    {
                        text_view_value.setText("0"+String.valueOf(day)+":"+String.valueOf(month)+":"+String.valueOf(year));

                    }
                    else if(day>=10&&month<10)
                    {
                        text_view_value.setText(String.valueOf(day)+":"+"0"+String.valueOf(month)+":"+String.valueOf(year));

                    }
                    else{
                        text_view_value.setText(String.valueOf(day)+":"+String.valueOf(month)+":"+String.valueOf(year));

                    }

                }


            }

            text_view_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrow[0], 0);
        }

        return listView;
    }
}
