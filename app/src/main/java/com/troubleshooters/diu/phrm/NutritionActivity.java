package com.troubleshooters.diu.phrm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    Switch switch_reminder;
    LinearLayout linearLayout;
    HorizontalBarChart bar_chart_record,bar_chart_suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        setTitle("Nutrition plan");
        switch_reminder=(Switch)findViewById(R.id.switch_reminder);
        linearLayout=(LinearLayout)findViewById(R.id.reminder_item_linear_layout);
        linearLayout.setVisibility(View.GONE);
        switch_reminder.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(switch_reminder.isChecked())
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            //Toast.makeText(getApplicationContext(), "Visible", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            linearLayout.setVisibility(View.GONE);
                            //Toast.makeText(getApplicationContext(), "Gone", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

        bar_chart_record=(HorizontalBarChart)findViewById(R.id.bar_chart_record);
        bar_chart_suggestion=(HorizontalBarChart)findViewById(R.id.bar_chart_suggestion);
        setData();

    }

    public void setData()
    {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry (1, 5));
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry (2, 2));
        ArrayList<BarEntry> entries3 = new ArrayList<>();
        entries3.add(new BarEntry (3, 6));
        ArrayList<BarEntry> entries4 = new ArrayList<>();
        entries4.add(new BarEntry (4, 7));
        List<IBarDataSet> bars = new ArrayList<IBarDataSet>();
        BarDataSet dataset1 = new BarDataSet(entries, "Calories");
        dataset1.setColor(Color.RED);
        bars.add(dataset1);
        BarDataSet dataset2 = new BarDataSet(entries2, "Carbohydrate");
        dataset2.setColor(Color.BLUE);
        bars.add(dataset2);
        BarDataSet dataset3 = new BarDataSet(entries3, "Fat");
        dataset3.setColor(Color.GREEN);
        bars.add(dataset3);
        BarDataSet dataset4 = new BarDataSet(entries4, "Protein");
        dataset4.setColor(Color.GRAY);
        dataset4.setDrawValues(true);
        bars.add(dataset4);
        BarData data = new BarData(bars);
        data.setBarWidth(1);


        /*XAxis xAxis = bar_chart.getXAxis();
        xAxis.setDrawLabels(false);
        YAxis yAxis =bar_chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis = bar_chart.getAxisRight();
        yAxis.setDrawGridLines(false);

        YAxis yAxisRight = bar_chart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = bar_chart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        xAxis = bar_chart.getXAxis();
        //bar_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bar_chart.getXAxis().setDrawGridLines(false);*/
        XAxis xAxis = bar_chart_record.getXAxis();
        xAxis.setEnabled(false);
        YAxis yAxisRight = bar_chart_record.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = bar_chart_record.getAxisLeft();
        yAxisLeft.setEnabled(false);
        bar_chart_record.setData(data);


        xAxis = bar_chart_suggestion.getXAxis();
        xAxis.setEnabled(false);
        yAxisRight = bar_chart_suggestion.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisLeft = bar_chart_suggestion.getAxisLeft();
        yAxisLeft.setEnabled(false);
        bar_chart_suggestion.setData(data);
    }


}


