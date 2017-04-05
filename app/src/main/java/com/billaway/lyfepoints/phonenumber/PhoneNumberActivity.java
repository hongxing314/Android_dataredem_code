package com.billaway.lyfepoints.phonenumber;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.SharedPreferences;

import com.billaway.lyfepoints.BaseActivity;
import com.billaway.lyfepoints.adapter.SponsorshipDataAdapter;
import com.billaway.lyfepoints.main.MainActivity;
import com.billaway.lyfepoints.LyfePointsApplication;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.data.models.DeviceInfo;
import com.billaway.lyfepoints.utils.Commons;
import com.cocosw.bottomsheet.BottomSheetHelper;

import com.click2sdx.controlsdk.android.model.entity.SponsorshipData;

public class PhoneNumberActivity extends BaseActivity implements DeviceInfoProvider {
    public static final String KEY_COUPON = "_coupon";
    private PhoneNumberPresenter presenter;
    private SponsorshipDataAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PhoneNumberActivity","onCreate");
        setContentView(R.layout.activity_phone_number);
        Coupon coupon = (Coupon) getIntent().getSerializableExtra(KEY_COUPON);
        if (coupon == null)
            throw new IllegalArgumentException("coupon == null");

        PhoneNumberView phoneNumberView = (PhoneNumberView) findViewById(R.id.phoneNumberView);
        presenter = new PhoneNumberPresenter(LyfePointsApplication.component(this),
                phoneNumberView, coupon, () -> null);
        phoneNumberView.setPresenter(presenter);


        adapter = new SponsorshipDataAdapter(this);



        subscribeToEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PhoneNumberActivity","onResume");
        presenter.start();
    }

    //Occurs when an event happens. Checks for whether or not we're requesting the home screen or the share event. probably wont share event
    @Override
    public void onEvent(Object event) {
        Log.d("PhoneNumberActivity","onEvent");
        if (event instanceof PhoneNumberContract.HomeScreenRequest) {
            navigateToHomeScreen((PhoneNumberContract.HomeScreenRequest) event);
        } else if (event instanceof PhoneNumberContract.ShareRequestEvent) {
            showShare((PhoneNumberContract.ShareRequestEvent) event);
        } else if (event instanceof PhoneNumberContract.CreateSponsorshipEvent){
            createNewSponsorship((PhoneNumberContract.CreateSponsorshipEvent) event);
        }
    }


    private void showShare(PhoneNumberContract.ShareRequestEvent event) {
        Log.d("PhoneNumberActivity","showShare");
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String shareText = "Received " + event.coupon.getCreditAwarded() + "MB of date credits from " +
                event.coupon.getBrandName() + "\n";




        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        BottomSheetHelper.shareAction(this, shareIntent).title("Share")
                .build().show();
    }

    private void navigateToHomeScreen(PhoneNumberContract.HomeScreenRequest event) {
        Log.d("PhoneNumberActivity","navigateToHomeScreen");
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public DeviceInfo getDeviceInfo() {
        Log.d("PhoneNumberActivity","DeviceInfo");
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setAllInfo(Commons.getInfosAboutDevice(this));
        String phoneNumber = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        deviceInfo.setPhoneNumber(phoneNumber);
        return null;
    }

    //Create new Sponsorship
    public void createNewSponsorship(PhoneNumberContract.CreateSponsorshipEvent event) {

        SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int totalCredits = sharedPreferences.getInt("ACCOUNT_CREDITS", 0);

        totalCredits += event.coupon.getCreditAwarded();

        editor.putInt("ACCOUNT_CREDITS", totalCredits);
        editor.apply();

        //SponsorshipData sponsorship = new SponsorshipData();
        //sponsorship.set
        Log.d("PhoneNumberActivity", "/credits Awarded: " + totalCredits);

    }


}
