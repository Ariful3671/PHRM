package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.troubleshooters.diu.phrm.R;

/**
 * Created by Arif on 02-12-17.
 */

public class MedicationPlanGridAdapter extends BaseAdapter{


    Context context;


    public MedicationPlanGridAdapter(Context context) {

        this.context=context;

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View gridView=view;
        if(view==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView=inflater.inflate(R.layout.medication_deatils,null);
        }
        return gridView;
    }
}
