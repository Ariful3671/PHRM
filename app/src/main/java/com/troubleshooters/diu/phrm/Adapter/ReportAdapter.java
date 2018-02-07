package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arif on 31-01-18.
 */

public class ReportAdapter extends BaseAdapter{


    List<ModelReport> reportList;
    Context context;


    public ReportAdapter(Context context,List<ModelReport> reportList) {

        this.reportList=reportList;
        this.context=context;

    }

    @Override
    public int getCount() {
        return reportList.size();
    }

    @Override
    public Object getItem(int position) {
        return reportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View listView=view;
        if(view==null) {

        LayoutInflater inflater;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView=inflater.inflate(R.layout.report_list_view,null);

        TextView hospital=(TextView)listView.findViewById(R.id.hospitalName);
        TextView received=(TextView)listView.findViewById(R.id.receivedDate);
        TextView appointment=(TextView)listView.findViewById(R.id.appointmentDate);

        hospital.setText("Hospital: "+reportList.get(position).getHospital());
        received.setText("Received: "+reportList.get(position).getReceived());
        appointment.setText("Appointment: "+reportList.get(position).getAppointment());


        }
        return listView;
    }
}
