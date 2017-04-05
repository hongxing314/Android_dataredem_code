package com.billaway.lyfepoints.views;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billaway.lyfepoints.R;

public class ContentStateView extends RelativeLayout {
    private final TextView textView;
    private final ImageView imageView;
    private final ProgressBar progressBar;
    private View content;

    public ContentStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.content_state_items, this, true);
        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    public void showProgress() {
        setVisibility(VISIBLE);
        textView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        imageView.setVisibility(INVISIBLE);
        content.setVisibility(GONE);
    }

    public void setContent(View content) {
        this.content = content;
    }

    public void showContent() {
        if (content == null)
            throw new NullPointerException("content view == null, please set before show content ");
        setVisibility(GONE);
        content.setVisibility(VISIBLE);
    }

    public void showError(String message) {
        setVisibility(VISIBLE);
        content.setVisibility(GONE);
        textView.setText(message);
        textView.setVisibility(!TextUtils.isEmpty(message) ? VISIBLE : GONE);
        progressBar.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
    }


}
