package com.troubleshooters.diu.phrm;

import android.app.Application;
import android.content.Context;

import com.troubleshooters.diu.phrm.Helper.LocaleHelper;

/**
 * Created by Oahid on 1/20/2018.
 */

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
