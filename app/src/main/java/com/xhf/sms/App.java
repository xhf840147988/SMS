package com.xhf.sms;

import android.app.Application;

import com.xhf.sms.api.AppContext;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.attachApp(this);
    }
}
