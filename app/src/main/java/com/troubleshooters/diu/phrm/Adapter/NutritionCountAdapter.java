package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.R;

import java.text.DecimalFormat;

import io.paperdb.Paper;

/**
 * Created by Arif on 08-12-17.
 */

public class NutritionCountAdapter extends BaseAdapter {
    String text[];
    String gained_nutrition[];
    String necessary_nutrition[];
    String units[];
    Context context;
    LayoutInflater inflater;

    public NutritionCountAdapter(String text[], String gained_nutrition[], String necessary_Nutrition[], String units[], Context context) {
        this.text = text;
        this.gained_nutrition = gained_nutrition;
        this.necessary_nutrition = necessary_Nutrition;
        this.units = units;
        this.context = context;
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int position) {
        return text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View gridView = view;
        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.list_view_nutrition, null);
            TextView heading = (TextView) gridView.findViewById(R.id.nutrition_heading);
            TextView gained = (TextView) gridView.findViewById(R.id.gained_nutrition);
            TextView necessary = (TextView) gridView.findViewById(R.id.necessary_nutrition);
            TextView unit = (TextView) gridView.findViewById(R.id.nutrition_unit);

            SharedPreferences sharedPreferences=context.getSharedPreferences("nutrition",Context.MODE_PRIVATE);
            String necessary_calorie=sharedPreferences.getString("necessary_calorie","");
            String necessary_carbohydrate=sharedPreferences.getString("necessary_carbohydrate","");
            String necessary_fat=sharedPreferences.getString("necessary_fat","");
            String necessary_protein=sharedPreferences.getString("necessary_protein","");


            String gained_calorie=new DecimalFormat("##.#").format(sharedPreferences.getFloat("gained_calorie",0.0f));
            String gained_fat=new DecimalFormat("##.#").format(sharedPreferences.getFloat("gained_fat",0.0f));
            String gained_carbohydrate=new DecimalFormat("##.#").format(sharedPreferences.getFloat("gained_carbohydrate",0.0f));
            String gained_protein=new DecimalFormat("##.#").format(sharedPreferences.getFloat("gained_protein",0.0f));




            if(position==0&&!necessary_calorie.equals(""))
            {
                necessary.setText(necessary_calorie);
            }
            else if(position==1&&!necessary_carbohydrate.equals(""))
            {
                necessary.setText(necessary_carbohydrate);
            }
            else if(position==2&&!necessary_fat.equals(""))
            {
                necessary.setText(necessary_fat);
            }
            else if(position==3&&!necessary_protein.equals(""))
            {
                necessary.setText(necessary_protein);
            }
            else
            {
                necessary.setText(necessary_nutrition[position]);

            }

            if(position==0)
            {
                gridView.setBackgroundResource(R.drawable.list_view_calorie_background);
            }
            else {
                gridView.setBackgroundResource(R.drawable.list_view_cal_car_pro_background);
            }




            if(position==0&&!gained_calorie.equals(""))
            {
                gained.setText(gained_calorie);
            }
            else if(position==1&&!gained_carbohydrate.equals(""))
            {
                gained.setText(gained_carbohydrate);
            }
            else if(position==2&&!gained_fat.equals(""))
            {
                gained.setText(gained_fat);
            }
            else if(position==3&&!gained_protein.equals(""))
            {
                gained.setText(gained_protein);
            }
            else
            {
                gained.setText("0");
            }

            if(position==0)
            {
                gridView.setBackgroundResource(R.drawable.list_view_calorie_background);
            }
            else {
                gridView.setBackgroundResource(R.drawable.list_view_cal_car_pro_background);
            }

            unit.setText(units[position]);
            heading.setText(text[position]);



        }

        return gridView;
    }
}
