package com.troubleshooters.diu.phrm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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

    }
}
