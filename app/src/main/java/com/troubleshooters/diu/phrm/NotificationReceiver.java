package com.troubleshooters.diu.phrm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Arif on 06-01-18.
 */

public class NotificationReceiver extends BroadcastReceiver{
    @Override
        public void onReceive(Context context, Intent intent) {


            NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent reaptingIntent=new Intent(context,HomeActivity.class);
            reaptingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,104,reaptingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"101")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.breakfast_icon)
                    .setContentTitle("Nutritionist")
                    .setContentText("Time for lunch!!!")
                    .setAutoCancel(true);
            notificationManager.notify(104,builder.build());
    }
}
