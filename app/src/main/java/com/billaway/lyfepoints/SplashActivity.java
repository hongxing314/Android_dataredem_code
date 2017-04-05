package com.billaway.lyfepoints;


import android.content.Intent;
import android.os.Handler;

import com.click2sdx.controlsdk.android.api.SDX;
import com.click2sdx.controlsdk.android.api.listeners.ResultCallback;
import com.click2sdx.controlsdk.android.model.enums.Result;
import android.content.SharedPreferences;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DELAY = 2000;
    private Handler autoCloseHandler;
    private Runnable autoCloseRunnable;

    @Override
    protected void onResume() {
        this.autoCloseHandler = new Handler();
        this.autoCloseRunnable = this::onClose;
        this.autoCloseHandler.postDelayed(autoCloseRunnable, SPLASH_DELAY);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (autoCloseHandler != null)
            autoCloseHandler.removeCallbacks(autoCloseRunnable);
    }

    private void onClose(){
        if(SDX.isInitialized()) {
            startReturnActivity();
            SDX.startSDK();
        } else {
            TataSdkManager.getInstance(getApplicationContext()).initWithSavedParams(new ResultCallback() {
                @Override
                public void onResult(Result result) {
                    if (result.isSuccess()) {
                        startReturnActivity();
                    } else {
                        startCreateAccountActivity();
                    }
                }
            });
        }
    }

    private void startReturnActivity() {
        startActivity(new Intent(this, com.billaway.lyfepoints.views.ReturnActivity.class));
        finish();
    }

    private void startCreateAccountActivity() {
        startActivity(new Intent(this, CreateAccountActivity.class));
        finish();
    }
}
