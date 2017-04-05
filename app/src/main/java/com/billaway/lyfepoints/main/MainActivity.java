package com.billaway.lyfepoints.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Base64;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import com.billaway.lyfepoints.BaseActivity;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.adapter.SponsorshipDataAdapter;
import com.billaway.lyfepoints.couponcode.CouponCodeActivity;
import com.billaway.lyfepoints.databinding.ActivityMainBinding;
import com.billaway.lyfepoints.databinding.LayoutChartBinding;
import com.billaway.lyfepoints.R.integer;
import com.billaway.lyfepoints.sponsorship.PostTokenRequest;
import com.click2sdx.controlsdk.android.utils.Utils;
import com.triggertrap.seekarc.SeekArc;
import com.billaway.lyfepoints.main.redeem.RedeemContract;
import com.billaway.lyfepoints.main.usedata.UseDataContract;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.support.annotation.LayoutRes;
import android.databinding.DataBindingUtil;
import android.view.View.OnLayoutChangeListener;
import android.widget.RelativeLayout;
import android.widget.Button;

import com.click2sdx.controlsdk.android.api.SDX;
import com.click2sdx.controlsdk.android.api.listeners.GetDataCallback;
import com.click2sdx.controlsdk.android.api.listeners.ResultCallback;
import com.click2sdx.controlsdk.android.api.listeners.SDXStatusChangeListener;
import com.click2sdx.controlsdk.android.api.listeners.SponsorshipsUpdateListener;
import com.click2sdx.controlsdk.android.api.listeners.VPNForceStoppedListener;
import com.click2sdx.controlsdk.android.model.entity.SponsorshipData;
import com.click2sdx.controlsdk.android.model.enums.Result;
import com.click2sdx.controlsdk.android.model.enums.SDXStatus;
import com.click2sdx.controlsdk.android.ui.DividerItemDecoration;
import android.content.Intent;
import android.util.Log;

import com.billaway.lyfepoints.AppComponent;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Single;

//Think I figured it out - ^Explore Coupon more to retrieve data regarding.. well, data.

//Extrapolate from SponsorshipsActivity.java
//Here's what we'll do. We'll allow the user to activate data for a sponsorship, so we'll iterate over
//a list of the sponsorship data's, by expiration date, and allow them to apply data by sponsorship
//rather than by 100MB. <--nearly finished

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private LayoutChartBinding layoutBinding;
    private SponsorshipDataAdapter adapter;
    private AppComponent component;

    public static final String TAG = MainActivity.class.getSimpleName();


    public int redeeming = 0;
    //public long totalCredits = 0;
    public int totalCredits;
    public long queuedCredits = 0;
    View btnApply;
    View btnEarn;

    //MMP Authorization
    private String username = "billaway";
    private String password = "g5t9YRFFqR%#Ev6j";


    //junk height to hide the fact that I'm a washed up loser.
    int height = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        showHome();
        subscribeToEvents();
        Log.d("MainActivity", "onCreate");

        btnApply = (View) findViewById(R.id.btnApply);
        btnApply.setVisibility(View.INVISIBLE);

        btnEarn = (View) findViewById(R.id.earn);
        btnEarn.setVisibility(View.VISIBLE);

        //reset redeeming onCreate to avoid carryover values
        redeeming = 0;

        //fix the height of the up/down buttons because design work is haaard
        View viewAlign = (View) findViewById(R.id.viewAlign);
        viewAlign.getLayoutParams().height = height;

        //Override onTouch for the Arc to prevent user from fingerbanging it.
        SeekArc seekArc = (SeekArc)findViewById(R.id.dataArc);

        seekArc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //Fix alignment of up/down buttons here
        View myView = findViewById(R.id.dataDial);

        myView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                // its possible that the layout is not complete in which case
                // we will get all zero values for the positions, so ignore the event
                if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                    return;
                }
                // use width for height since view is a square
                height = myView.getWidth();
                View viewAlign = (View) findViewById(R.id.viewAlign);
                viewAlign.getLayoutParams().height = height;

            }
        });

        seekArc.bringToFront();
        seekArc.invalidate();
        //SponsorshipDataAdapter - for displaying current data usage information
        adapter = new SponsorshipDataAdapter(this);

        retrieveSponsorships();

        updateTotalCredits(adapter);
        //updateTotalCredits();

        //Log.d("MainActivity", "Credits: " + component.data().availableCredits());//data.availableCredits());

        //whatdu
        SDX.registerSDXStatusChangeListener(this, new SDXStatusChangeListener() {
            @Override
            public void onStatusChange(SDXStatus newStatus, SDXStatus oldStatus) {
                adapter.notifyDataSetChanged();
            }
        });

        SDX.registerSponsorshipsUpdateListener(this, new SponsorshipsUpdateListener() {
            @Override
            public void onSponsorshipsUpdated() {
                Log.d(TAG, "onSponsorshipsUpdated");
                refreshAdapter();
            }
        });

        //Restarts the SDX, potentially useful but not as it exists, but maybe
        SDX.registerVPNForceStoppedListener(this, new VPNForceStoppedListener() {
            @Override
            public void onVPNForceStopped() {
                mapSDKRunningToUI();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "| onResume");

        refreshAdapter();

    }

    //needed in order to get the sponsorships
    private void retrieveSponsorships() {
        SDX.retrieveSponsorships(new GetDataCallback<SponsorshipData>() {
            @Override
            public void onResult(Result Result, List<SponsorshipData> data) {
                SDX.toastResult(getApplicationContext(), Result);
                refreshAdapter();

            }
        });
    }

    private void mapSDKRunningToUI() {
        if (SDX.isSDKRunning()) {
            //IGNORE
        } else {
            //RESTART SDX
            SDX.startSDK();
            mapSDKRunningToUI();
        }
    }

    private void sortOptions() {
        //rather than display alert & ask for user sort options, sort by expiration


    }
        /*

        From SponsorshipsActivity/showActionsDialog, we want to steal the activateSponsorship(sponsorshipData)
        And apply it to a button press. We'll need to iterate through our sorted list of sponsorships to
        activate the proper number of selected sponsorships. We'll use btnApply in the OnClick below to
        accomplish this.

         */

    private void refreshAdapter () {
        List<SponsorshipData> sponsorships = SDX.retrieveSponsorships();
        List<SponsorshipData> result = new ArrayList<>();

        if(Utils.isNotEmpty(sponsorships)){
            for(SponsorshipData sponsorship : sponsorships){
                String state = sponsorship.getState();
                if(state == "INACTIVE"){
                   result.add(sponsorship);
                }
            }
        }
        result = sponsorships;

        sort(result);

        adapter.setItems(result);
        adapter.notifyDataSetChanged();
    }

    //Sort the sponsorships in order of expiration date
    private void sort(final List<SponsorshipData> sponsorships) {
        Collections.sort(sponsorships, new Comparator<SponsorshipData>() {
            @Override
            public int compare(SponsorshipData left, SponsorshipData right) {
                //We will not use switch, only sort type expire time.
                Long leftExpirationTime = left.getExpirationTime();
                Long rightExpirationTime = right.getExpirationTime();
                int result = leftExpirationTime.compareTo(rightExpirationTime);

                return result;
            }
        });
    }

    private void activateSponsorship(SponsorshipData sponsorshipData) {

        SDX.activateSponsorship(sponsorshipData.getId(), new ResultCallback() {
            @Override
            public void onResult(Result result) {
                SDX.toastResult(getApplicationContext(), result);
            }
        });
    }

    //useful for future purposes, but not necessarily needed right now.
    private void deactivateSponsorship(SponsorshipData sponsorshipData) {

        SDX.deactivateSponsorship(sponsorshipData.getId(), new ResultCallback() {
            @Override
            public void onResult(Result result) {
                SDX.toastResult(getApplicationContext(), result);
            }
        });

    }


    //END NEW SPONSORSHIP CODE









    public void updateTotalCredits(SponsorshipDataAdapter a){

        SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);

        totalCredits = sharedPreferences.getInt("ACCOUNT_CREDITS", 0);

        if(totalCredits>0) {

            TextView txtMB = (TextView) findViewById(R.id.txtMB);
            txtMB.setText(String.format(getApplicationContext().getString(R.string.data_format), totalCredits));

        } else {
            TextView txtMB = (TextView) findViewById(R.id.txtMB);
            txtMB.setText(String.format(getApplicationContext().getString(R.string.data_format), 0));
        }

        //totalCredits = integer.availableCredits;

            //totalCredits = data.availableCredits().intValue();//Integer.parseInt(data.availableCredits().toString());//data.availableCredits().parse


        Log.d("MainActivity", "totalCredits: " + totalCredits);
        //totalCredits = 0;
        /* Let it conduct business as I intended for Sponsorships, but we're going to go back to the old method of totalCredits */
        //refreshAdapter();

/*
        //List<SponsorshipData> items = new ArrayList<>();
        //items = a.getItems();




        if(!items.isEmpty()) {

            long totalBytes = 0;

            for (SponsorshipData i : items) {
                totalBytes += i.getTotalBytes();
            }

            String totalString = Utils.getByteCountString(totalBytes, true);

            TextView txtMB = (TextView) findViewById(R.id.txtMB);
            txtMB.setText(totalString);

            //We're not updating here, but above the info line
            //totalCredits = totalBytes;

            sort(items);
            adapter.setItems(items);


        } else {
            TextView txtMB = (TextView) findViewById(R.id.txtMB);
            txtMB.setText(String.format(getApplicationContext().getString(R.string.data_format), 0));
        }*/

    }





























    public void updateQueuedCredits(List<SponsorshipData> items){
        long totalBytes = 0;
        int i = 0;
        Button apply = (Button) findViewById(R.id.btnApply);

        for (SponsorshipData s : items) {
            if(i<redeeming) {
                totalBytes += s.getTotalBytes();
                i++;
            } else{
                apply.setVisibility(View.INVISIBLE);
                //TextView redeeming = (TextView) findViewById(R.id.redeeming_amount);
                //redeeming.setText("");
                return;
            }
        }

        //
        String totalString = Utils.getByteCountString(totalBytes, true);
        //TextView redeeming = (TextView) findViewById(R.id.redeeming_amount);
        apply = (Button) findViewById(R.id.btnApply);
        apply.setText(String.format(getApplicationContext().getString(R.string.redeeming_amount), totalString));
    }

    public void OnClick(View view){
        Log.d("MainActivity", "OnClick");

        switch(view.getId()){
            case R.id.moreHandler:
                Log.d("OnClick ", "More Handler");
                Button moreData = (Button) findViewById(R.id.moreData);
                moreData.performClick();
                moreData.setPressed(true);
                moreData.invalidate();

                moreData.postDelayed(new Runnable(){
                    public void run() {
                        moreData.setPressed(false);
                        moreData.invalidate();
                    }
                }, 400);
                //moreData.setPressed(false);
                //moreData.invalidate();


                break;

            case R.id.lessHandler:
                Log.d("OnClick ", "Less Handler");
                Button lessData = (Button) findViewById(R.id.lessData);
                lessData.performClick();
                lessData.setPressed(true);
                lessData.invalidate();

                lessData.postDelayed(new Runnable(){
                    public void run() {
                        lessData.setPressed(false);
                        lessData.invalidate();
                    }
                }, 400);


                break;

            case R.id.btnApply:
                Log.d("MainActivity", "OnClick/btnApply");
                if(totalCredits !=0) {

                    String credentials = username + ":" + password;
                    String credits = "";
                    if (redeeming == 0) {
                        credits = "0";
                    } else {
                        credits = String.valueOf(redeeming);
                    }

                    Log.d("String Value", "credentials " + credentials);

                    SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);

                    String phone = sharedPreferences.getString("ACCOUNT_KEY_PHONE", "");

                    int redeemed = sharedPreferences.getInt("ACCOUNT_KEY_REDEEMED", 0);

                    redeemed += redeeming;
                    totalCredits -= redeeming;

                    new PostTokenRequest(MainActivity.this).execute(credentials, credits, phone);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("ACCOUNT_KEY_REDEEMED", redeemed);
                    editor.putInt("ACCOUNT_CREDITS", totalCredits);
                    editor.apply();

                    SeekArc seekArc = (SeekArc) findViewById(R.id.dataArc);
                    int progress = 0;
                    redeeming = 0;
                    seekArc.setProgress(progress);
                    btnApply.setVisibility(View.INVISIBLE);
                    btnEarn.setVisibility(View.VISIBLE);

                    Log.d("totalCredits ", "" + totalCredits);
                    updateTotalCredits(adapter);
                }

                break;

            case R.id.moreData:
                Log.d("OnClick ", "More Data");
                int diff = totalCredits - redeeming;
                if(totalCredits >=100 && diff >=100) {
                    Log.d("totalCredits ", "" + totalCredits);
                    SeekArc seekArc = (SeekArc) findViewById(R.id.dataArc);
                    redeeming += 100;
                    int progress = (redeeming * 100) / totalCredits;
                    seekArc.setProgress(progress);

                    Button applyText = (Button)btnApply.findViewById(R.id.btnApply);
                    applyText.setText("Apply " + redeeming + " MB");

                    btnApply.setVisibility(View.VISIBLE);
                    btnEarn.setVisibility(View.INVISIBLE);

                }

                break;

            case R.id.lessData:
                if(redeeming != 0) {

                    SeekArc seekArc = (SeekArc) findViewById(R.id.dataArc);
                    if(redeeming > 100) {
                        redeeming -= 100;
                    } else {
                        redeeming = 0;
                    }
                    int progress = (redeeming * 100) / totalCredits;
                    seekArc.setProgress(progress);

                    btnApply.setVisibility(View.VISIBLE);
                    btnEarn.setVisibility(View.INVISIBLE);

                    Button applyText = (Button)btnApply.findViewById(R.id.btnApply);
                    applyText.setText("Apply " + redeeming + " MB");

                    if(redeeming<100) {
                        btnApply.setVisibility(View.INVISIBLE);
                        btnEarn.setVisibility(View.VISIBLE);
                    }

                } else {
                    btnApply.setVisibility(View.INVISIBLE);
                    btnEarn.setVisibility(View.VISIBLE);
                }

                break;

            //Updated
            case R.id.earn:
                showRedeemCode();
                break;

            case R.id.menu:
                showMenu();
                break;

            case R.id.data:
                showDataUsage();
                break;

            case R.id.return_home:
                showHome();
                break;

            case R.id.usage_back:
                showHome();
                break;
        }
    }

    @Override
    public void onEvent(Object event){
        Log.d("MainActivity", "onEvent");
        if(event instanceof RedeemContract.Events.RedeemSuccess
                || event instanceof UseDataContract.SuccessEvent){
            showHome();
        }
    }


    @Override
    public void onBackPressed() {
        Log.d("MainActivity", "onBackPressed");
        setView(R.layout.layout_home);
    }

    private void showMenu(){
        Log.d("MainActivity", "showMenu");
        setView(R.layout.layout_menu);
    }

    private void showDataUsage(){
        Log.d("MainActivity", "showDataUsage");

        TextView txtMB = (TextView) findViewById(R.id.txtMB);
        txtMB.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtMB.getText().toString().length() == 0){

                }
            }
        });

        setView(R.layout.layout_data_usage);
    }

    private void showRedeemCode(){
        Log.d("MainActivity", "showRedeemCode");
        startActivity(new Intent(this, CouponCodeActivity.class));
    }

    private void showHome(){
        Log.d("MainActivity", "showHome");
        setView(R.layout.layout_chart);
        View viewAlign = (View) findViewById(R.id.viewAlign);
        viewAlign.getLayoutParams().height = height;
        btnApply = (View) findViewById(R.id.btnApply);
        btnApply.setVisibility(View.INVISIBLE);
        btnEarn = (View) findViewById(R.id.earn);
        btnEarn.setVisibility(View.VISIBLE);
        updateTotalCredits(adapter);
        //updateTotalCredits();
    }


    private void setView(@LayoutRes int layout){
        Log.d("MainActivity", "setView");
        this.binding.contentFrame.removeAllViews();
        this.binding.contentFrame.addView(getLayoutInflater().inflate(layout, binding.contentFrame, false));
    }


}
