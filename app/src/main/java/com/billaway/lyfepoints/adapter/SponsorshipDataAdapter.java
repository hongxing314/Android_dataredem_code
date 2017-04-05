package com.billaway.lyfepoints.adapter;

/**
 * Created by ABTBenjamins on 1/31/17.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import com.click2sdx.controlsdk.android.api.SDX;
import com.click2sdx.controlsdk.android.model.entity.SponsorshipData;
import com.click2sdx.controlsdk.android.model.entity.Threshold;
import com.click2sdx.controlsdk.android.model.enums.SDXStatus;
//import com.click2sdx.controlsdk.android.rest.response.AppInfo;
//import com.click2sdx.controlsdk.android.model.response.AppInfo;
//import com.click2sdx.controlsdk.android.R;
import com.billaway.lyfepoints.R;
import com.click2sdx.controlsdk.android.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//HERE'S WHAT WE'RE GOING TO DO! WE'RE GOING TO REDESIGN THIS CODE TO PROVIDE US WITH THE INFORMATION
//WE NEED ABOUT THE DATA THAT IS AVAILABLE TO US RIGHT NOW. WE'LL WORK FROM THERE.

public class SponsorshipDataAdapter extends RecyclerView.Adapter<SponsorshipDataAdapter.ViewHolder> {

    private List<SponsorshipData> items = new ArrayList<>();

    private ItemClickListener itemClickListener;

    private Context context;

    public interface ItemClickListener {
        void onItemClick(SponsorshipData sponsorshipData);
    }

    public interface TresholdClickListener {
        void onTresholdClick(SponsorshipData sponsorshipData);
    }

    public SponsorshipDataAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<SponsorshipData> items) {
        this.items = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, subtitleView, statusTextView, infoTextView, tresholdsView, timeTextView, validForVPNView;
        public ImageView iconView;
        public ProgressBar progressBar;
        public LinearLayout iconsView;

        public ViewHolder(View view) {
            super(view);
            Log.d("SponsorshipDataAdapter", "ViewHolder()");
            //title not needed
            //titleView = (TextView) view.findViewById(R.id.title);

            //icon not needed
            //iconsView = (LinearLayout) view.findViewById(R.id.icons);

            //Relates to expiration of data. needed
            timeTextView = (TextView) view.findViewById(R.id.timeText);

            //Thresholds, not particularly needed
            //tresholdsView = (TextView) view.findViewById(R.id.thresholds);

            //Lets us know if we're currently using data
            //statusTextView = (TextView) view.findViewById(R.id.statusText);

            //Not needed
            //subtitleView = (TextView) view.findViewById(R.id.subtitle);

            //Not needed
            //iconView = (ImageView) view.findViewById(R.id.icon);

            //Not needed
            //infoTextView = (TextView) view.findViewById(R.id.infoText);

            //Not entirely sure if needed, Likely not.
            //validForVPNView = (TextView) view.findViewById(R.id.validForVPN);
            progressBar = (ProgressBar) view.findViewById(R.id.usageBar);
        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_data_usage, parent, false);

        Log.d("SponsorshipDataAdapter", "onCreateViewHolder()");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d("SponsorshipDataAdapter", "onBindViewHolder()");
        final SponsorshipData item = items.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(item);
                }
            }
        });


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

        if (Utils.isNotEmpty(expirationTimeFormatted) && Utils.isNotEmpty(startTimeFormatted)) {
            holder.timeTextView.setText(context.getString(R.string.valid_from_to, startTimeFormatted, expirationTimeFormatted));
        } else if (Utils.isNotEmpty(expirationTimeFormatted)) {
            holder.timeTextView.setText(context.getString(R.string.valid_to, expirationTimeFormatted));
        } else if (Utils.isNotEmpty(startTimeFormatted)) {
            holder.timeTextView.setText(context.getString(R.string.valid_from, startTimeFormatted));
        } else {
            holder.timeTextView.setText(context.getString(R.string.valid_unlimited));
        }


        long totalBytes = item.getTotalBytes();
        long usageBytes = item.getUsageBytes();

        String usageString = Utils.getByteCountString(usageBytes, true);
        String totalString = Utils.getByteCountString(totalBytes, true);

        int percentUsed = (int) ((usageBytes * 100.0) / (double) totalBytes);

        //holder.subtitleView.setText("Usage info: " + usageString + " of " + totalString + " consumed");

        String state = item.getState();

        //rather than setText(state), check state & set text to R.string.sponsorship_activated || R.string.sponsorship_unactivated
        holder.statusTextView.setText(state);

        //Is this what I want to do? Layout design suggested to display data usage over progress bar.
            //Possibly update bar in 25% increments to leave room to display data usage as intended?
        holder.progressBar.setProgress(percentUsed);


        /* Don't know how to handle this yet, or if it will even be necessary.
        if (SDX.getStatus() == SDXStatus.SPONSORED && item.isValidForVPN()) {
            holder.validForVPNView.setText(R.string.valid_for_vpn);
            holder.validForVPNView.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.validForVPNView.setText(R.string.not_valid_for_vpn);
            holder.validForVPNView.setTextColor(context.getResources().getColor(R.color.secondary_text_color));
        } */

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        Log.d("SponsorshipDataAdapter", "setItemClickListener()");
        this.itemClickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        Log.d("SponsorshipDataAdapter", "getItemCount()");
        return items.size();
    }


    public List<SponsorshipData> getItems() {
        Log.d("SponsorshipDataAdapter", "getItems()");
        return items;
    }

}
