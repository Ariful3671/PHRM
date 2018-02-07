package com.troubleshooters.diu.phrm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.EditMedication;
import com.troubleshooters.diu.phrm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arif on 08-01-18.
 */

public class MedicationReminder extends RecyclerView.Adapter<MedicationReminder.MedicationReminderViewHolder>{

    Context context;
    List<Model_medicin_details> medicines;

    public MedicationReminder(Context context, List<Model_medicin_details> medicines) {
        //Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
        this.context = context;
        this.medicines = medicines;

    }

    @Override
    public MedicationReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Toast.makeText(context, "ok2", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.medication_recycler_item,null);
        MedicationReminderViewHolder medicationReminderViewHolder=new MedicationReminderViewHolder(view,context,medicines);
        return medicationReminderViewHolder;
    }

    @Override
    public void onBindViewHolder(MedicationReminderViewHolder holder, int position) {

        Model_medicin_details model_medicin_details=medicines.get(position);
        holder.medicineDetails.setText(model_medicin_details.getMedicines());
        //Toast.makeText(context, "ok3", Toast.LENGTH_SHORT).show();
        int value=Integer.parseInt(model_medicin_details.getHour());
        if(Integer.parseInt(model_medicin_details.getHour())>12)
        {
            value=Integer.parseInt(model_medicin_details.getHour())-12;

        }
        if(Integer.parseInt(model_medicin_details.getMinute())<10)
        {
            holder.time.setText("Time:"+String.valueOf(value)+":0"+model_medicin_details.getMinute()+model_medicin_details.getAMorPM());
        }
        else {
            holder.time.setText("Time:"+String.valueOf(value)+":0"+model_medicin_details.getMinute()+model_medicin_details.getAMorPM());
        }
        holder.time.setText("Time:"+String.valueOf(value)+":"+model_medicin_details.getMinute()+model_medicin_details.getAMorPM());
        holder.repeat.setText("Repeat: "+model_medicin_details.getDays());
        holder.startTime.setText("Start date: "+model_medicin_details.getDate()+":"+model_medicin_details.getMonth()+":"+model_medicin_details.getYear());

    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    class MedicationReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView medicineDetails,time,repeat,startTime;
        Context context;
        List<Model_medicin_details> list=new ArrayList<>();
        public MedicationReminderViewHolder(View itemView, Context context, List<Model_medicin_details> list ) {

            super(itemView);
            this.context=context;
            this.list=list;
            itemView.setOnClickListener(this);
            //Toast.makeText(context, "ok1", Toast.LENGTH_SHORT).show();
            medicineDetails=(TextView) itemView.findViewById(R.id.card_view_medicine_details);
            time=(TextView)itemView.findViewById(R.id.card_view_time_details);
            repeat=(TextView)itemView.findViewById(R.id.card_view_repeat);
            startTime=(TextView)itemView.findViewById(R.id.card_view_start_time);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Model_medicin_details object=this.list.get(position);
            Intent in=new Intent(this.context, EditMedication.class);
            in.putExtra("medInfo",object.getMedicines());
            in.putExtra("NID",object.getNID());
            in.putExtra("numberofalarm",object.getNumberofalarm());
            this.context.startActivity(in);
        }
    }
}
