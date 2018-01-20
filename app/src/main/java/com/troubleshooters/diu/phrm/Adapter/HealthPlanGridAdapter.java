package com.troubleshooters.diu.phrm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.troubleshooters.diu.phrm.R;

/**
 * Created by Arif on 13-11-17.
 */

public class HealthPlanGridAdapter extends BaseAdapter{

    String text[];
    String button_status[];
    int  icon[];
    Context context;
    LayoutInflater inflater;

    public HealthPlanGridAdapter(String text[],int icon[],String button_status[],Context context)
    {
        this.text=text;
        this.icon=icon;
        this.button_status=button_status;
        this.context=context;
        //this.drawable=drawable;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View gridView=view;
        if(view==null)
        {
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView=inflater.inflate(R.layout.custom_grid_view,null);


            ImageView imageView=(ImageView)gridView.findViewById(R.id.image_view_custom_grid);
            TextView lebel=(TextView)gridView.findViewById(R.id.text_view_custom_Grid);
            TextView status=(TextView) gridView.findViewById(R.id.button_custom_grid);
            if(position==0)
            {
                gridView.setBackgroundResource(R.drawable.grid_item_round_corner_nutrition);
            }
            if(position==1)
            {
                gridView.setBackgroundResource(R.drawable.grid_item_round_corner_exercise);
            }
            if(position==2)
            {
                gridView.setBackgroundResource(R.drawable.grid_item_round_corner_medication);
            }
            imageView.setImageResource(icon[position]);
            lebel.setText(text[position]);
            status.setText(button_status[0]);

        }
        return gridView;
    }
}
