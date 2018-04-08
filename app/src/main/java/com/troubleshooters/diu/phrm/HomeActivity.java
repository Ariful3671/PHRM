package com.troubleshooters.diu.phrm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.troubleshooters.diu.phrm.Adapter.HealthPlanGridAdapter;
import com.troubleshooters.diu.phrm.Adapter.MedicationReminder;
import com.troubleshooters.diu.phrm.Adapter.Model_medicin_details;
import com.troubleshooters.diu.phrm.Adapter.TestRecordGridAdapter;
import com.troubleshooters.diu.phrm.Helper.LocaleHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity {

    ViewFlipper flipperWeb, flipperNoweb;
    GridView grid_daily_routin;
    GridView grid_test_records;
    Animation fade_in,fade_out;
    BottomNavigationView bottomNavigationView;
    public static Activity home;

    LinearLayout summaryLayout, bpSummaryLayout, glucoseSummaryLayout;
    TextView showHideSummary;
    private LineChart bpChart, bgChart;
    RelativeLayout tipsAndNewsLayout;
    Button viewTipsNews;

    WebView tips, news;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home=this;

        //Initialization for Test record and health plan adapter
        String grid_daily_routin_text[]=getResources().getStringArray(R.array.grid_daily_routine);
        String grid_test_record_text[]=getResources().getStringArray(R.array.grid_test_record);
        int grid_test_record_icon[]={R.drawable.blood_pressure_icon,R.drawable.glucose_test_icon,R.drawable.add};
        int grid_daily_routin_icon[]={R.drawable.nutrition_icon,R.drawable.exercise_icon,R.drawable.medication_icon};
        String grid_daily_routin_button_status[]=getResources().getStringArray(R.array.grid_daily_routine_button);
        String grid_record_test_button_status[]=getResources().getStringArray(R.array.grid_test_record_button);

        //for bp & glucose symary
        summaryLayout = (LinearLayout)findViewById(R.id.bpGlucoseChartLayout);
        bpSummaryLayout = (LinearLayout)findViewById(R.id.bpSummary);
        glucoseSummaryLayout = (LinearLayout)findViewById(R.id.bgSummary);
        showHideSummary = (TextView)findViewById(R.id.hide_summary);
        bpChart = (LineChart)findViewById(R.id.bp_chart);
        bpChart.setDragEnabled(true);
        bpChart.setScaleEnabled(false);

        bgChart = (LineChart)findViewById(R.id.bg_chart);
        bgChart.setDragEnabled(true);
        bgChart.setScaleEnabled(true);

        summaryLayout.setVisibility(View.GONE);
        bpSummaryLayout.setVisibility(View.GONE);
        glucoseSummaryLayout.setVisibility(View.GONE);

        //Setting language
        setLanguage();

        setTitle(getString(R.string.home_title));//Changing activity name.
        tipsAndNewsLayout = (RelativeLayout)findViewById(R.id.tips_and_news_layout);
        viewTipsNews = (Button)findViewById(R.id.view_tips_news);
        flipperWeb = (ViewFlipper)findViewById(R.id.flipper_webview);
        flipperNoweb=(ViewFlipper)findViewById(R.id.flipper_noweb);
        tips=(WebView)findViewById(R.id.tips);
        news=(WebView)findViewById(R.id.news);

        grid_daily_routin=(GridView)findViewById(R.id.grid_daily_routin);
        grid_test_records=(GridView)findViewById(R.id.grid_test_record);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Animating view flipper (Internet)
            flipperNoweb.setVisibility(View.GONE);
            flipperWeb.setVisibility(View.VISIBLE);
            tips.setWebViewClient(new WebViewClient());
            tips.loadUrl("https://sites.google.com/diu.edu.bd/phrmtipsnews/tips?authuser=1");
            news.setWebViewClient(new WebViewClient());
            news.loadUrl("https://sites.google.com/diu.edu.bd/phrmtipsnews/news?authuser=1");
            fade_in= AnimationUtils.loadAnimation(this,R.anim.fade_in);
            fade_out= AnimationUtils.loadAnimation(this,R.anim.fade_out);
            flipperWeb.setAutoStart(true);
            flipperWeb.setInAnimation(fade_in);
            flipperWeb.setOutAnimation(fade_out);
            flipperWeb.setFlipInterval(15000);
            flipperWeb.startFlipping();
        }
        else{
            //Animating view flipper (No internet)
            flipperWeb.setVisibility(View.GONE);
            viewTipsNews.setVisibility(View.GONE);
            flipperNoweb.setVisibility(View.VISIBLE);
            fade_in= AnimationUtils.loadAnimation(this,R.anim.fade_in);
            fade_out= AnimationUtils.loadAnimation(this,R.anim.fade_out);
            flipperNoweb.setAutoStart(true);
            flipperNoweb.setInAnimation(fade_in);
            flipperNoweb.setOutAnimation(fade_out);
            flipperNoweb.setFlipInterval(20000);
            flipperNoweb.startFlipping();
        }


        //Creating firebase and sharedpreference for updating profileinfo sharedpreference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        String user=sharedPreferences.getString("userid","");
        DatabaseReference ref = database.getReference("users").child(user);

        //bp summary implementation
        bpSummary();

        //bg summary implementation
        bgSummary();



        //Getting profile data from fire base to sharedpreference
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name,phone,birthday,height,weight,gender,activity;

                        if(dataSnapshot.hasChild("name"))
                        {
                            name=dataSnapshot.child("name").getValue().toString();
                            editor.putString("name",name);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "name:"+name, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            name="";
                            editor.putString("name",name);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "name:", Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild("phone"))
                        {
                            phone=dataSnapshot.child("phone").getValue().toString();
                            editor.putString("phone",phone);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "phone:"+phone, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            phone="";
                            editor.putString("phone",phone);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "phone:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("byear"))
                        {
                            String bYear=dataSnapshot.child("byear").getValue().toString();
                            String bMonth=dataSnapshot.child("bmonth").getValue().toString();
                            String bDay=dataSnapshot.child("bday").getValue().toString();
                            editor.putString("birthday",bDay+":"+bMonth+":"+bYear);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "birthday: exist", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            birthday="";
                            editor.putString("birthday",birthday);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "birthday:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("byear"))
                        {
                            int currentYear= Calendar.getInstance().get(Calendar.YEAR);
                            int currentMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
                            int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                            int bYear=Integer.parseInt(dataSnapshot.child("byear").getValue().toString());
                            int bMonth=Integer.parseInt(dataSnapshot.child("bmonth").getValue().toString());
                            int bDay=Integer.parseInt(dataSnapshot.child("bday").getValue().toString());

                            int age=currentYear-bYear;
                            if(currentMonth-bMonth<0)
                            {
                                age--;
                            }
                            if(currentMonth-bMonth==0)
                            {
                                if(currentDay-bDay<0)
                                {
                                    age--;
                                }
                            }
                            editor.putString("age",String.valueOf(age));
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "age:"+age, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String age="";
                            editor.putString("age",age);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "age:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("height"))
                        {
                            height=dataSnapshot.child("height").getValue().toString();
                            editor.putString("height",height);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "height:"+height, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            height="";
                            editor.putString("height",height);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "height:", Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild("weight"))
                        {
                            weight=dataSnapshot.child("weight").getValue().toString();
                            editor.putString("weight",weight);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "weight:"+weight, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            weight="";
                            editor.putString("weight",weight);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "weight:", Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild("gender"))
                        {
                            gender=dataSnapshot.child("gender").getValue().toString();
                            editor.putString("gender",gender);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "gender:"+gender, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            gender="";
                            editor.putString("gender",gender);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "gender:", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.hasChild("activity"))
                        {
                            activity=dataSnapshot.child("activity").getValue().toString();
                            editor.putString("activity",activity);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "activity:"+activity, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            activity="";
                            editor.putString("activity",activity);
                            editor.commit();
                            //Toast.makeText(HomeActivity.this, "activity:", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );






        //Setting activity for bottom navigation menu
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_navigation_home:
                                break;
                                case R.id.bottom_navigation_notification:
                                    Intent intent=new Intent(HomeActivity.this,NotificationActivity.class);
                                    startActivity(intent);
                                    break;

                            case R.id.bottom_navigation_report:
                                Intent intent1;
                                intent1=new Intent(HomeActivity.this,ReportActivity.class);
                                intent1.putExtra("loadingTime",2);
                                startActivity(intent1);
                                break;
                        }
                        return false;
                    }

                }
        );




        //Setting adapter for grid views
        HealthPlanGridAdapter adapter=new HealthPlanGridAdapter(grid_daily_routin_text,grid_daily_routin_icon,grid_daily_routin_button_status,HomeActivity.this);
        grid_daily_routin.setAdapter(adapter);
        TestRecordGridAdapter adapter_test_record=new TestRecordGridAdapter(grid_test_record_text,grid_test_record_icon,grid_record_test_button_status,HomeActivity.this);
        grid_test_records.setAdapter(adapter_test_record);




        //setting activity for grid view health plan
        grid_daily_routin.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent intent;
                        if(position==0)
                        {
                            intent=new Intent(HomeActivity.this,NutritionActivity.class);
                            startActivity(intent);
                        }
                        if(position==1)
                        {
                            intent=new Intent(HomeActivity.this,ExerciseActivity.class);
                            startActivity(intent);
                        }
                        if(position==2)
                        {
                            intent=new Intent(HomeActivity.this,MedicationActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );


        //setting activity for grid view test record
        grid_test_records.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent intent;
                        if(position==0)
                        {
                            intent=new Intent(HomeActivity.this,BloodPressureActivity.class);
                            startActivity(intent);
                        }
                        if(position==1)
                        {
                            intent=new Intent(HomeActivity.this,BloodGlucoseActivity.class);
                            startActivity(intent);
                        }
                        if(position==2)
                        {
                            intent=new Intent(HomeActivity.this,TestAnalysisActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );

        //Show or Hide BP and Glucose Summary
        showHideSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showHideSummary.getText().equals("Hide Summary")) {
                    showHideSummary.setText("Show Summary");
                    bpSummaryLayout.setVisibility(View.GONE);
                    glucoseSummaryLayout.setVisibility(View.GONE);

                }
                else{
                    showHideSummary.setText("Hide Summary");
                    bpSummary();
                    bgSummary();
                }
            }
        });

        //Opening Tips & News in another activity
        viewTipsNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flipperWeb.getCurrentView()==tips){
                    Intent intent = new Intent(HomeActivity.this, TipsAndNews.class);
                    intent.putExtra("label", "Tips");
                    intent.putExtra("url", "https://sites.google.com/diu.edu.bd/phrmtipsnews/tips?authuser=1");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(HomeActivity.this, TipsAndNews.class);
                    intent.putExtra("label", "News");
                    intent.putExtra("url", "https://sites.google.com/diu.edu.bd/phrmtipsnews/news?authuser=1");
                    startActivity(intent);
                }
            }
        });

    }

    private void setLanguage() {
        Paper.init(this);
        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");
        updateView((String)Paper.book().read("language"));
    }

    private void bgSummary() {
        SharedPreferences bloodGlucoseData = getSharedPreferences("BloodGlucoseData", Context.MODE_PRIVATE);
        String bgDataString = bloodGlucoseData.getString("bgString", "");
        if(bgDataString!=""){
            summaryLayout.setVisibility(View.VISIBLE);
            glucoseSummaryLayout.setVisibility(View.VISIBLE);
            setBgData();
        }
    }

    private void bpSummary() {
        SharedPreferences bloodPressureData = getSharedPreferences("BloodPressureData", Context.MODE_PRIVATE);
        String bpDataString = bloodPressureData.getString("bpString", "");
        if(bpDataString!=""){
            summaryLayout.setVisibility(View.VISIBLE);
            bpSummaryLayout.setVisibility(View.VISIBLE);
            setBpData();
        }
    }


    //Setting BP summary chart data
    private void setBpData() {
        String[] bpDates = new String[15];
        ArrayList<Entry> systolicBpVals = new ArrayList<>();
        ArrayList<Entry> diastolicBpVals = new ArrayList<>();
        SharedPreferences bloodPressureData = getSharedPreferences("BloodPressureData", Context.MODE_PRIVATE);
        String bpDataString = bloodPressureData.getString("bpString", "");
        int strLength = bpDataString.length();
        int i=0;
        int j=0;

        while(i<strLength){

            bpDates[j] = bpDataString.substring(i, 6+i);
            int sysVal = Integer.parseInt(bpDataString.substring(6+i, 9+i));
            systolicBpVals.add(new Entry(j, sysVal));
            int diasVal = Integer.parseInt(bpDataString.substring(9+i, 12+i));
            diastolicBpVals.add(new Entry(j, diasVal));
            j++;
            i+=12;
        }

        LineDataSet sysData, diasData;
        sysData = new LineDataSet(systolicBpVals, "Systolic");
        sysData.setColor(Color.MAGENTA);
        sysData.setLineWidth(2f);
        sysData.setValueTextColor(Color.BLUE);
        diasData = new LineDataSet(diastolicBpVals, "Diastolic");
        diasData.setColor(Color.DKGRAY);
        diasData.setLineWidth(2f);
        diasData.setValueTextColor(Color.BLUE);
        LineData bpChartData = new LineData(sysData,diasData);
        bpChart.setData(bpChartData);
        bpChart.getAxisRight().setEnabled(false);
        bpChart.getAxisLeft().setTextSize(8f);
        bpChart.getXAxis().setTextSize(8f);
        YAxis bpYxxis = bpChart.getAxisLeft();
        bpYxxis.setAxisMaximum(180f);
        bpYxxis.setAxisMinimum(40f);
        XAxis bpXaxis = bpChart.getXAxis();
        bpXaxis.setValueFormatter(new xAxisValueFormatter(bpDates));
        LimitLine upperLimit = new LimitLine(140f, "");
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setLineColor(Color.GREEN);
        upperLimit.setLineWidth(2f);
        LimitLine lowerLimit = new LimitLine(60f, "");
        lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLimit.setLineColor(Color.GREEN);
        lowerLimit.setLineWidth(2f);
        bpYxxis.removeAllLimitLines();
        bpYxxis.addLimitLine(upperLimit);
        bpYxxis.addLimitLine(lowerLimit);
        bpChart.animateX(2000);

    }

    //Setting bg summary chart data
    private void setBgData() {
        String[] bgDates = new String[15];
        ArrayList<Entry> fastingBgVals = new ArrayList<>();
        ArrayList<Entry> afterMealBgVals = new ArrayList<>();
        SharedPreferences bloodGlucoseData = getSharedPreferences("BloodGlucoseData", Context.MODE_PRIVATE);
        String bgDataString = bloodGlucoseData.getString("bgString", "");
        int strLength = bgDataString.length();
        int i=0;
        int j=0;

        while(i<strLength){

            bgDates[j] = bgDataString.substring(i, 6+i);
            float fastingVal = Float.parseFloat(bgDataString.substring(6+i, 10+i));
            fastingBgVals.add(new Entry(j, fastingVal));
            float afterMealVal = Float.parseFloat(bgDataString.substring(10+i, 14+i));
            afterMealBgVals.add(new Entry(j, afterMealVal));
            j++;
            i+=14;
        }



        LineDataSet fastingData, afterMealData;
        fastingData = new LineDataSet(fastingBgVals, "Fasting");
        fastingData.setColor(Color.MAGENTA);
        fastingData.setLineWidth(2f);
        fastingData.setValueTextColor(Color.BLUE);
        afterMealData = new LineDataSet(afterMealBgVals, "2Hrs after meal");
        afterMealData.setColor(Color.DKGRAY);
        afterMealData.setLineWidth(2f);
        afterMealData.setValueTextColor(Color.BLUE);

        LineData bgChartData = new LineData(fastingData,afterMealData);
        bgChart.setData(bgChartData);
        bgChart.getAxisRight().setEnabled(false);
        bgChart.getAxisLeft().setTextSize(8f);
        bgChart.getXAxis().setTextSize(8f);
        YAxis bgYxxis = bgChart.getAxisLeft();
        bgYxxis.setAxisMaximum(25f);
        bgYxxis.setAxisMinimum(0f);
        XAxis bgXaxis = bgChart.getXAxis();
        bgXaxis.setValueFormatter(new xAxisValueFormatter(bgDates));
        LimitLine upperLimit = new LimitLine(8.5f, "");
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setLineColor(Color.GREEN);
        upperLimit.setLineWidth(2f);
        LimitLine lowerLimit = new LimitLine(4f, "");
        lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLimit.setLineColor(Color.GREEN);
        lowerLimit.setLineWidth(2f);
        bgYxxis.removeAllLimitLines();
        bgYxxis.addLimitLine(upperLimit);
        bgYxxis.addLimitLine(lowerLimit);
        bgChart.animateX(2000);
    }

    //xAxis value formatter for graphs
    public class  xAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;
        public xAxisValueFormatter(String[] values){
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

    //Updating language change
    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();

    }


    //Actionbar menu setting
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.item_setting)
        {
            Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.item_profile)
        {
            Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.item_log_out)
        {

            SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_nutrition=getSharedPreferences("nutrition", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_time=getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_RID=getSharedPreferences("reminder", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_time_exercise=getSharedPreferences("timeExercise", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences_medicine=getSharedPreferences("medication",Context.MODE_PRIVATE);
            SharedPreferences sharedPreferencesExercise=getSharedPreferences("exercise", Context.MODE_PRIVATE);
            SharedPreferences bloodPressureData = getSharedPreferences("BloodPressureData", Context.MODE_PRIVATE);
            SharedPreferences bloodGlucoseData = getSharedPreferences("BloodGlucoseData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();




            //Save password and dont show dialog
            if(sharedPreferences.getString("save password status","").equals("yes"))
            {

                //Canceling meal reminder
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);



                //Canceling alarm of medication activity
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));




                ref.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {

                                    int NID=Integer.parseInt(snapshot.child("nid").getValue().toString());
                                    int numberofalarm=Integer.parseInt(snapshot.child("numberofalarm").getValue().toString());
                                    if(numberofalarm==1)
                                    {
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                    }
                                    if(numberofalarm>1)
                                    {
                                        for(int i=0;i<numberofalarm;i++)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            NID=NID+1;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );



                String userid=sharedPreferences.getString("userid","");
                String password=sharedPreferences.getString("password","");
                sharedPreferences.edit().clear().commit();
                sharedPreferences_nutrition.edit().clear().commit();
                sharedPreferences_time.edit().clear().commit();
                sharedPreferences_time_exercise.edit().clear().commit();
                sharedPreferences_medicine.edit().clear().commit();
                sharedPreferencesExercise.edit().clear().commit();
                sharedPreferences_RID.edit().clear().commit();
                bloodPressureData.edit().clear().commit();
                bloodGlucoseData.edit().clear().commit();
                editor.putString("userid",userid);
                editor.putString("password",password);
                editor.putString("save password status", "yes");
                editor.putString("save password dialog status", "no");
                editor.putString("logout status", "yes");
                editor.commit();
            }



            //Dont save password and dont show dialog
            else if(sharedPreferences.getString("save password status","").equals("no")&&sharedPreferences.getString("save password dialog status","").equals("no"))
            {
                //Canceling meal reminder
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);





                //Canceling alarm of medication activity
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));




                ref.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {

                                    int NID=Integer.parseInt(snapshot.child("nid").getValue().toString());
                                    int numberofalarm=Integer.parseInt(snapshot.child("numberofalarm").getValue().toString());
                                    if(numberofalarm==1)
                                    {
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                    }
                                    if(numberofalarm>1)
                                    {
                                        for(int i=0;i<numberofalarm;i++)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            NID=NID+1;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );







                String userid=sharedPreferences.getString("userid","");
                String password=sharedPreferences.getString("password","");
                sharedPreferences.edit().clear().commit();
                sharedPreferences_nutrition.edit().clear().commit();
                sharedPreferences_time.edit().clear().commit();
                sharedPreferences_time_exercise.edit().clear().commit();
                sharedPreferences_medicine.edit().clear().commit();
                sharedPreferences_RID.edit().clear().commit();
                sharedPreferencesExercise.edit().clear().commit();
                bloodPressureData.edit().clear().commit();
                bloodGlucoseData.edit().clear().commit();
                editor.putString("userid",userid);
                editor.putString("password",password);
                editor.putString("save password status", "no");
                editor.putString("save password dialog status", "no");
                editor.putString("logout status", "yes");
                editor.commit();
            }









            //dont save password and show dialog
            else{
                //Canceling meal reminder
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),102,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),103,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),104,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),105,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),106,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                intent=new Intent(getApplicationContext(),NotificationReceiver.class);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),107,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);





                //Canceling alarm of medication activity
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("medication reminder").child(sharedPreferences.getString("userid",""));




                ref.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {

                                    int NID=Integer.parseInt(snapshot.child("nid").getValue().toString());
                                    int numberofalarm=Integer.parseInt(snapshot.child("numberofalarm").getValue().toString());
                                    if(numberofalarm==1)
                                    {
                                        Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                    }
                                    if(numberofalarm>1)
                                    {
                                        for(int i=0;i<numberofalarm;i++)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(pendingIntent);
                                            NID=NID+1;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );






                String userid=sharedPreferences.getString("userid","");
                String password=sharedPreferences.getString("password","");
                sharedPreferences.edit().clear().commit();
                sharedPreferences_nutrition.edit().clear().commit();
                sharedPreferences_time.edit().clear().commit();
                sharedPreferences_time_exercise.edit().clear().commit();
                sharedPreferences_medicine.edit().clear().commit();
                sharedPreferences_RID.edit().clear().commit();
                sharedPreferencesExercise.edit().clear().commit();
                bloodPressureData.edit().clear().commit();
                bloodGlucoseData.edit().clear().commit();
                editor.putString("userid",userid);
                editor.putString("password",password);
                editor.putString("save password status", "no");
                editor.putString("save password dialog status", "yes");
                editor.putString("logout status", "yes");
                editor.commit();
            }
            Intent intent=new Intent(HomeActivity.this,LogInActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setLanguage();
        bpSummary();
        bgSummary();
    }
}

