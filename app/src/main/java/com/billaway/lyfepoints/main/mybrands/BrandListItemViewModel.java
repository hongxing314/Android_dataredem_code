package com.billaway.lyfepoints.main.mybrands;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.billaway.lyfepoints.data.models.Brand;

public class BrandListItemViewModel extends BaseObservable {
    private final Brand brand;
    private final int redeemCount;

    BrandListItemViewModel(Brand brand, int redeemCount) {
        this.brand = brand;
        this.redeemCount = redeemCount;
    }

    @Bindable
    public String getTitle() {
        return brand.getTitle();
    }

    @Bindable
    public String getIcon() {
        return brand.getIcon();
    }

    @Bindable
    public String getRedeems() {
        return redeemCount + " redeems";
    }
}
