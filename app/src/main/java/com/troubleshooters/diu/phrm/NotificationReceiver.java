package com.troubleshooters.diu.phrm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Arif on 06-01-18.
 */

public class NotificationReceiver extends BroadcastReceiver{


    @Override
        public void onReceive(Context context, Intent intent) {


            int NID=intent.getIntExtra("NID",0);
            String text=intent.getStringExtra("text");

            NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
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
