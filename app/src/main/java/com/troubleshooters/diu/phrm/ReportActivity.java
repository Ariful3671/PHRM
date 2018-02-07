package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.Adapter.ModelReport;
import com.troubleshooters.diu.phrm.Adapter.ReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {


    NetworkChecker networkChecker;//checking internet connection
    TextView loading,noReports;
    ProgressBar progressBar;
    ListView listView;
    List<ModelReport> reportList;
    ReportAdapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Reports");



        //Creating instance
        networkChecker=new NetworkChecker(ReportActivity.this);
        loading=(TextView)findViewById(R.id.loading_textview_report);
        noReports=(TextView)findViewById(R.id.textview_report);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar_report);
        listView=(ListView)findViewById(R.id.listview_report);
        reportList=new ArrayList<>();
        //





        //Checking internet connection
        if(networkChecker.isConnected())
        {

            BackgroundTask backgroundTask=new BackgroundTask(this);
            backgroundTask.execute();
        }
        else {
            Toast.makeText(ReportActivity.this,"NO or Bad internet connection", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences=getSharedPreferences("json", Context.MODE_PRIVATE);
        String json=sharedPreferences.getString("json","");
        if(json.equals(""))
        {
            loading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            noReports.setVisibility(View.VISIBLE);

        }
        else{
            loading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noReports.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            try {
                jsonObject=new JSONObject(json);
                jsonArray=jsonObject.getJSONArray("server_response");
                int count=0;
                String hospital,id,userName,appointment,comment,received,reportName;

                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    hospital=JO.getString("hospital");
                    id=JO.getString("id");
                    userName=JO.getString("username");
                    appointment=JO.getString("appointment");
                    comment=JO.getString("comment");
                    received=JO.getString("sendingdate");
                    reportName=JO.getString("reportname");
                    ModelReport report=new ModelReport(hospital,id,userName,appointment,comment,received,reportName);
                    reportList.add(report);
                    adapter=new ReportAdapter(this,reportList);
                    listView.setAdapter(adapter);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v7.app.AlertDialog.Builder builder;
                        builder = new android.support.v7.app.AlertDialog.Builder(ReportActivity.this, R.style.CustomDialogTheme);
                        builder.setMessage("Hospital: "+reportList.get(position).getHospital()+"\n\n"
                                          +"Received Date: "+reportList.get(position).getReceived()+"\n"
                                          +"Appointment Date: "+reportList.get(position).getAppointment()+"\n\n"
                                          +"Comment: "+reportList.get(position).getComment()+"\n\n"
                                          +"File: "+reportList.get(position).getReportName()+"\n")
                                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                })
                                .setNegativeButton("View", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {



                                            }
                                        }).show();
                    }
                }
        );



    }
}
