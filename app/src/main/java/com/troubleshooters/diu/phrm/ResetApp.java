package com.troubleshooters.diu.phrm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Arif on 11-01-18.
 */

public class ResetApp extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences=context.getSharedPreferences("nutrition",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("gained_calorie","");
        editor.putString("gained_fat","");
        editor.putString("gained_protein","");
        editor.putString("gained_carbohydrate","");
        editor.commit();


        /*NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent reaptingIntent=new Intent(context,HomeActivity.class);
        reaptingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,reaptingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.breakfast_icon)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setContentTitle("PHRM")
                .setContentText("Time for your meal!!!")
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());*/

    }
}