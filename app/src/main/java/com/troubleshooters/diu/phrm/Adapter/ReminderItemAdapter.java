package com.troubleshooters.diu.phrm.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.troubleshooters.diu.phrm.EditMedication;
import com.troubleshooters.diu.phrm.MedicationActivity;
import com.troubleshooters.diu.phrm.MedicationAlarm;
import com.troubleshooters.diu.phrm.NotificationReceiver;
import com.troubleshooters.diu.phrm.NutritionActivity;
import com.troubleshooters.diu.phrm.R;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Arif on 07-02-18.
 */

public class ReminderItemAdapter extends RecyclerView.Adapter<ReminderItemAdapter.ReminderItemViewHolder> {


    Context context;
    List<ModelReminder> reminders;

    public ReminderItemAdapter(Context context, List<ModelReminder> reminders) {
        //Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
        this.context = context;
        this.reminders = reminders;
    }


    @Override
    public ReminderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.reminder_recycler_item,null);
        //Toast.makeText(context, "ok2", Toast.LENGTH_SHORT).show();
        ReminderItemViewHolder holder=new ReminderItemViewHolder(view,context,reminders);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReminderItemViewHolder holder, int position) {
        ModelReminder modelReminder=reminders.get(position);
        //Toast.makeText(context, modelReminder.getDay().toString(), Toast.LENGTH_SHORT).show();
        holder.reminderText.setText("Reminder Details:\n\n"+modelReminder.getRemindertext());
        holder.date.setText("Date: "+modelReminder.getDay()+"."+modelReminder.getMonth()+"."+modelReminder.getYear());
        holder.time.setText("Time: "+modelReminder.getHour()+":"+modelReminder.getMinute());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class ReminderItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        TextView reminderText,time,date;
        Context context;
        List<ModelReminder> reminders;
        public ReminderItemViewHolder(View itemView,Context context,List<ModelReminder> reminders) {
            super(itemView);
            //Toast.makeText(context, "ok3", Toast.LENGTH_SHORT).show();
            this.context=context;
            this.reminders=reminders;
            itemView.setOnLongClickListener(this);
            reminderText=(TextView) itemView.findViewById(R.id.reminder_details);
            time=(TextView) itemView.findViewById(R.id.time_reminder_recycler);
            date=(TextView) itemView.findViewById(R.id.date_reminder_recycler);
        }

        @Override
        public boolean onLongClick(View v) {
            final int position=getAdapterPosition();
            final ModelReminder object=this.reminders.get(position);
            android.support.v7.app.AlertDialog.Builder builder;
            builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.CustomDialogTheme);
            builder.setMessage("Do you really want to cancel your reminder!")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final SharedPreferences sharedPreferences=context.getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
                            final DatabaseReference ref=database.getReference("reminder").child(sharedPreferences.getString("userid",""));

                            ref.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                            {
                                                if(snapshot.child("rid").getValue().equals(object.getRid()))
                                                {
                                                    ref.child(snapshot.getKey()).removeValue();
                                                    Intent intent=new Intent(getApplicationContext(),MedicationAlarm.class);
                                                    PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),Integer.valueOf(object.getRid()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                    AlarmManager alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);
                                                    alarmManager.cancel(pendingIntent);
                                                    reminders.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, reminders.size());

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();


            return true;
        }
    }
}
