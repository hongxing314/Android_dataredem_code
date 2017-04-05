package com.billaway.lyfepoints.main.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.LinearLayout;
import com.billaway.lyfepoints.LyfePointsApplication;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.adapter.SponsorshipDataAdapter;
import com.billaway.lyfepoints.databinding.LayoutDataUsageBinding;
import com.click2sdx.controlsdk.android.api.SDX;
import com.click2sdx.controlsdk.android.model.entity.SponsorshipData;
import com.click2sdx.controlsdk.android.utils.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChartView extends LinearLayout implements ChartContract.View {
    private LayoutDataUsageBinding binding;
    private final ChartContract.Presenter presenter;

    private SponsorshipDataAdapter adapter;
    private List<SponsorshipData> items = new ArrayList<>();

    TextView timeTextView;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("ChartView", "ChartView");
        if (isInEditMode()) {
            presenter = null;
            return;
        }
        presenter = new ChartPresenter(LyfePointsApplication.component(context), this);
        adapter = new SponsorshipDataAdapter(context);
        timeTextView = (TextView) findViewById(R.id.timeText);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("ChartView", "onFinishInflate");
        if (isInEditMode()) return;
        this.binding = LayoutDataUsageBinding.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("ChartView", "onAttachedToWindow");
        if (isInEditMode()) return;
        presenter.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("ChartView", "onDetachedFromWindow");
        presenter.start();
    }


    @Override
    public void showAvailableCredits(Integer availableCredits) {
        //binding.tvAvailableCredits.setText(String.format(getContext().getString(R.string.data_format), availableCredits));
        //binding.tvRemainingDaysLabel.setVisibility(VISIBLE);

        Log.d("ChartView", "showAvailableCredits");
        //binding.txtMB.setText(String.format(getContext().getString(R.string.data_format), availableCredits));
    }

    @Override
    public void showRedeemedUsedCredits(Pair<Integer, Integer> redeemedUsed) {
        Log.d("ChartView", "showRedeemedUsedCredits");
        //Filter Activated sponsorships, then sort them by expiration.
        List<SponsorshipData> sponsorships = SDX.retrieveSponsorships();
        List<SponsorshipData> result = new ArrayList<>();

        if(Utils.isNotEmpty(sponsorships)){
            for(SponsorshipData sponsorship : sponsorships){
                String state = sponsorship.getState();
                Log.d("ChartView", "/showRedeemedUsedCredits: state - " + state + " | sponsorship - " + sponsorship);
                if(state == "ACTIVATED"){

                    result.add(sponsorship);
                }
            }
        }
        result = sponsorships;

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

        adapter.setItems(result);
        adapter.notifyDataSetChanged();

        items = adapter.getItems();

        boolean test = false;

        if(!items.isEmpty() || test){
            final SponsorshipData item = items.get(0);

            Log.d("ChartView", "/showRedeemedUsedCredits: state - " + item.getState());

            String state = item.getState();
            if(state.equals("ACTIVATED") || test) {
                if(!test) {
                    Log.d("ChartView", "/showRedeemedUsedCredits: Display expire date/status message/data usage");
                    //Display the earliest expiring data date
                    TextView timeTextView = (TextView) findViewById(R.id.timeText);
                    String expirationTimeFormatted = null;
                    String startTimeFormatted = null;

                    long expirationTime = item.getExpirationTime();
                    if (expirationTime > 0) {
                        expirationTimeFormatted = Utils.DATE_FORMAT.format(new Date(expirationTime));
                    }

                    long startTime = item.getStartTime();
                    if (startTime > 0) {
                        startTimeFormatted = Utils.DATE_FORMAT.format(new Date(startTime));
                    }

                    //binding.timeText.setText("ACTIVATED");

                /*if (Utils.isNotEmpty(expirationTimeFormatted) && Utils.isNotEmpty(startTimeFormatted)) {
                    binding.timeText.setText(getContext().getString(R.string.valid_from_to, startTimeFormatted, expirationTimeFormatted));
                } else */

                    //We only want to showcase expiration date or if there is no expiration on the data
                    if (Utils.isNotEmpty(expirationTimeFormatted)) {
                        //Careful with this, may not be completely correct
                        binding.timeText.setText(getContext().getString(R.string.valid_to, expirationTimeFormatted));
                /*} else if (Utils.isNotEmpty(startTimeFormatted)) {
                    binding.timeText.setText(getContext().getString(R.string.valid_from, startTimeFormatted));
                */
                    } else {
                        binding.timeText.setVisibility(INVISIBLE);
                        binding.validTo.setVisibility(INVISIBLE);
                        //binding.timeText.setText(getContext().getString(R.string.valid_unlimited));
                    }

                    Log.d("Validity Check: ", "" + binding.timeText.getText());


                    //Let us know if we're currently using data
                    TextView statusTextView1 = (TextView) findViewById(R.id.statusText1);
                    TextView statusTextView2 = (TextView) findViewById(R.id.statusText2);
                    statusTextView1.setText(R.string.sponsorship_activated_1);
                    statusTextView2.setText(R.string.sponsorship_activated_2);


                    //display used data / remaining data
                    long totalBytes = 0;
                    long usageBytes = 0;

                    for (SponsorshipData i : items) {
                        totalBytes += i.getTotalBytes();
                        usageBytes += i.getUsageBytes();
                    }

                    binding.usageBar.setVisibility(VISIBLE);
                    String usageString = Utils.getByteCountString(usageBytes, true);
                    String totalString = Utils.getByteCountString(totalBytes, true);

                    int percentUsed = (int) ((usageBytes * 100.0) / (double) totalBytes);

                    binding.dataUsed.setText(usageString);
                    binding.dataTotal.setText(totalString);

                } else {

                    Log.d("ChartView", "/showRedeemedUsedCredits: Display expire date/status message/data usage");
                    //Display the earliest expiring data date
                    TextView timeTextView = (TextView) findViewById(R.id.timeText);
                    String expirationTimeFormatted = null;
                    String startTimeFormatted = null;

                    long expirationTime = item.getExpirationTime();
                    if (expirationTime > 0) {
                        expirationTimeFormatted = Utils.DATE_FORMAT.format(new Date(expirationTime));
                    }

                    long startTime = item.getStartTime();
                    if (startTime > 0) {
                        startTimeFormatted = Utils.DATE_FORMAT.format(new Date(startTime));
                    }

                    //binding.timeText.setText("ACTIVATED");

                /*if (Utils.isNotEmpty(expirationTimeFormatted) && Utils.isNotEmpty(startTimeFormatted)) {
                    binding.timeText.setText(getContext().getString(R.string.valid_from_to, startTimeFormatted, expirationTimeFormatted));
                } else */

                    //We only want to showcase expiration date or if there is no expiration on the data
                    if (Utils.isNotEmpty(expirationTimeFormatted)) {
                        //Careful with this, may not be completely correct
                        binding.timeText.setText(getContext().getString(R.string.valid_to, expirationTimeFormatted));
                /*} else if (Utils.isNotEmpty(startTimeFormatted)) {
                    binding.timeText.setText(getContext().getString(R.string.valid_from, startTimeFormatted));
                */
                    } else {
                        binding.timeText.setVisibility(INVISIBLE);
                        binding.validTo.setVisibility(INVISIBLE);
                        //binding.timeText.setText(getContext().getString(R.string.valid_unlimited));
                    }

                    Log.d("Validity Check: ", "" + binding.timeText.getText());


                    //Let us know if we're currently using data
                    TextView statusTextView1 = (TextView) findViewById(R.id.statusText1);
                    TextView statusTextView2 = (TextView) findViewById(R.id.statusText2);
                    statusTextView1.setText(R.string.sponsorship_activated_1);
                    statusTextView2.setText(R.string.sponsorship_activated_2);


                    //display used data / remaining data
                    /*
                    long totalBytes = 0;
                    long usageBytes = 0;

                    for (SponsorshipData i : items) {
                        totalBytes += i.getTotalBytes();
                        usageBytes += i.getUsageBytes();
                    }
                    */

                    binding.usageBar.setVisibility(VISIBLE);
                    //String usageString = Utils.getByteCountString(usageBytes, true);
                    //String totalString = Utils.getByteCountString(totalBytes, true);

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);



                    int totalCredits = sharedPreferences.getInt("ACCOUNT_CREDITS", 0);
                    int redeemedCredits = sharedPreferences.getInt("ACCOUNT_KEY_REDEEMED", 0);

                    String usageString = "20 MB";
                    String totalString = String.valueOf(redeemedCredits) + " MB";


                    binding.dataUsed.setText(usageString);
                    binding.dataTotal.setText(totalString);
                }
        }

        } else {
            Log.d("ChartView", "No Sponsorships being used");
            //if results is empty (means there is no active data
            TextView statusTextView1 = (TextView) findViewById(R.id.statusText1);
            TextView statusTextView2 = (TextView) findViewById(R.id.statusText2);
            statusTextView1.setText(R.string.sponsorship_unactivated_1);
            statusTextView2.setText(R.string.sponsorship_unactivated_2);
        }

    }

    @Override
    public void setPresenter(ChartContract.Presenter presenter) {
        Log.d("ChartView", "setPresenter");

    }
}

