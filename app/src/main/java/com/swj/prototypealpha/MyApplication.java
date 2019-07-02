package com.swj.prototypealpha;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.swj.prototypealpha.swj.util.Utils;

public class MyApplication extends Application
{
    private static MyApplication appContext;

    public static MyApplication getInstance()
    {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Utils.init(this);
        if(BuildConfig.DEBUG)
        {
            Logger.addLogAdapter(new AndroidLogAdapter());
        }
    }
}
