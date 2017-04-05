package com.billaway.lyfepoints;


import android.content.Context;

import com.click2sdx.controlsdk.android.api.SDX;
import com.click2sdx.controlsdk.android.api.SDXApplication;
import com.click2sdx.controlsdk.android.api.listeners.ResultCallback;
import com.click2sdx.controlsdk.android.model.SDXParams;
import com.click2sdx.controlsdk.android.model.enums.Result;
import com.click2sdx.controlsdk.android.model.enums.SubscriberType;

import com.squareup.picasso.Picasso;

public class LyfePointsApplication extends SDXApplication {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        Picasso.with(this).setLoggingEnabled(true);
    }


    public static AppComponent component(Context context) {
        return ((LyfePointsApplication) context.getApplicationContext()).appComponent;
    }
}
