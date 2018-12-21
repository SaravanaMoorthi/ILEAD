package com.tvscs.ilead.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tvscs.ilead.utils.TypefaceUtil;

public class LeadApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Poppins-Regular.ttf");
        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/Poppins-Regular.ttf");
    }
}
