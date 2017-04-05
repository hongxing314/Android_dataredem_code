package com.billaway.lyfepoints.utils;


import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DataBindingAdapters {

    @BindingAdapter("roundedCornerImage")
    public static void setRoundedCorner(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .transform(new RoundedTransformation(10, 8))
                .into(imageView);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
