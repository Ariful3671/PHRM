package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.troubleshooters.diu.phrm.R;

import java.util.zip.Inflater;

/**
 * Created by Arif on 08-12-17.
 */

public class NutritionCountAdapter extends BaseAdapter {
    String text[];
    String gained_nutrition[];
    String necessary_nutrition[];
    Context context;
    LayoutInflater inflater;

    public NutritionCountAdapter(String text[], String gained_nutrition[], String necessary_Nutrition[], Context context)
    {
        this.text=text;
        this.gained_nutrition=gained_nutrition;
        this.necessary_nutrition=necessary_Nutrition;
        this.context=context;
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
            gridView = inflater.inflate(R.layout.custom_grid_view, null);
            TextView gain = (TextView) gridView.findViewById(R.id.text_view1_custom_grid);
            TextView necessary = (TextView) gridView.findViewById(R.id.text_view2_custom_grid);
            TextView name = (TextView) gridView.findViewById(R.id.text_view3_custom_grid);
            SharedPreferences sharedPreferences=context.getSharedPreferences("Nutrition",Context.MODE_PRIVATE);
            String necessary_carbohydrate=sharedPreferences.getString("necessary_carbohydrate","");
            String necessary_fat=sharedPreferences.getString("necessary_fat","");
            String necessary_protein=sharedPreferences.getString("necessary_protein","");
            String gained_calorie=sharedPreferences.getString("gained_calorie","");
            String gained_fat=sharedPreferences.getString("gained_fat","");
            String gained_carbohydrate=sharedPreferences.getString("gained_carbohydrate","");
            String gained_protein=sharedPreferences.getString("gained_protein","");
            if(position==0&&!necessary_carbohydrate.equals(""))
            {
                necessary.setText("of "+necessary_carbohydrate+"gm");
            }
            else if(position==1&&!necessary_fat.equals(""))
            {
                necessary.setText("of "+necessary_fat+"gm");
            }
            else if(position==2&&!necessary_protein.equals(""))
            {
                necessary.setText("of "+necessary_protein+"gm");
            }
            else
            {
                necessary.setText("of "+necessary_nutrition[position]+"gm");
            }



            if(position==0&&!gained_carbohydrate.equals(""))
            {
                gain.setText(gained_carbohydrate);
            }
            else if(position==1&&!gained_fat.equals(""))
            {
                gain.setText(gained_fat);
            }
            else if(position==2&&!gained_protein.equals(""))
            {
                gain.setText(gained_protein);
            }
            else
            {
                gain.setText(gained_nutrition[position]);
            }
            gridView.setBackgroundResource(R.drawable.edittext_rectangle_round_corner);

            name.setText(text[position]);
        }
        return gridView;
    }

}
