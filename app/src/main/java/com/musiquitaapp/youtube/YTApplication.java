package com.musiquitaapp.youtube;

import android.app.Application;
import android.content.Context;
;import com.google.api.services.youtube.model.Activity;

public class YTApplication extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }

}