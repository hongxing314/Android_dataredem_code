package com.billaway.lyfepoints;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.click2sdx.controlsdk.android.api.SDX;
import com.click2sdx.controlsdk.android.api.listeners.ResultCallback;
import com.click2sdx.controlsdk.android.model.SDXParams;
import com.click2sdx.controlsdk.android.model.SDXTestParams;
import com.click2sdx.controlsdk.android.model.enums.LogLevel;
import com.click2sdx.controlsdk.android.model.enums.Result;
import com.click2sdx.controlsdk.android.model.enums.SubscriberType;
import com.click2sdx.controlsdk.android.utils.Utils;


public class TataSdkManager {
    private Context context;
    public static TataSdkManager instance;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 1001;

    public TataSdkManager(Context context) {
        this.context = context;
    }

    public static TataSdkManager getInstance(Context context) {
        if(instance == null){
            instance = new TataSdkManager(context);
        }
        return instance;
    }

    public void initWithSavedParams(final ResultCallback callback){
        SDXParams sdxParams = getParams();
        SDX.initialize(context, sdxParams, new ResultCallback() {
            @Override
            public void onResult(Result result) {
                callback.onResult(result);
            }
        });
    }

    private SDXParams getParams() {
        SDXParams sdxParams = new SDXParams();

        sdxParams.setSubscriberType(SubscriberType.RESTRICTED_MSISDN);

        SharedPreferences sharedPreferences = context.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);
        Log.d("phone", sharedPreferences.getString("ACCOUNT_KEY_PHONE", "no phone number saved"));
        String mPhoneNumber = sharedPreferences.getString("ACCOUNT_KEY_PHONE", "0000000000");

        sdxParams.setMsisdn(mPhoneNumber);

        //ID of end user, temporarily use phone number
        sdxParams.setUserId(mPhoneNumber);

        //Optional Parameter
        //sdxParams.setAppAuthCode("authCode");

        //Forces the SDK to accept device regardless of network source or wifi access
        sdxParams.setForceEligible(true);

        //Specifically related to this application & the developer. Unique to application & developer.
        sdxParams.setControlAppId("43");
        sdxParams.setDevKey3rdPartyApp("cb61b797-f57b-4aea-822f-73047451f028");

        //Allows Billaway capture of information.
        sdxParams.setLogLevel(LogLevel.DEBUG);

        return sdxParams;

    }

    private SDXTestParams getTestParams() {
        SDXTestParams sdxTestParams = new SDXTestParams();
        return sdxTestParams;
    }

    public void init(String name, String phone, String email, final ResultCallback callback){
        SDXParams sdxParams = getParams();

        SDX.initialize(context, sdxParams, new ResultCallback() {
            @Override
            public void onResult(Result result) {
                callback.onResult(result);
            }
        });
    }

}
