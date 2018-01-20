package com.troubleshooters.diu.phrm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Arif on 09-01-18.
 */

public class MedicationAlarm extends BroadcastReceiver {

    int NID;
    String text;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NID=intent.getIntExtra("NID",0);
        text=intent.getStringExtra("text");
        Intent reaptingIntent=new Intent(context,HomeActivity.class);
        reaptingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,NID,reaptingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.breakfast_icon)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setContentTitle("PHRM")
                .setContentText(text)
                .setAutoCancel(true);
        notificationManager.notify(NID,builder.build());
    }
}
