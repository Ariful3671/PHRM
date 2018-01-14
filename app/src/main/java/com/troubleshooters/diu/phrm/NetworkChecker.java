package com.troubleshooters.diu.phrm;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Arif on 14-01-18.
 */

public class NetworkChecker {

    Context context;

    public NetworkChecker(Context context) {
        this.context = context;
    }

    public boolean isConnected()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if(info!=null)
            {
                if(info.getState()==NetworkInfo.State.CONNECTED)
                {
                    return true;
                }

            }
        }
        return false;
    }
}
