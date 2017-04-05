package com.billaway.lyfepoints.main.mybrands;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.billaway.lyfepoints.data.models.Brand;
import com.billaway.lyfepoints.databinding.LayoutMyBrandsBinding;

import java.util.ArrayList;
import java.util.List;

public class MyBrandsView extends FrameLayout {
    LayoutMyBrandsBinding binding;

    public MyBrandsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) return;
        this.binding = DataBindingUtil.bind(this);
        this.binding.rvMyBrands.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvMyBrands.setAdapter(new BrandsAdapter(getBrands()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        this.binding.rvMyBrands.addItemDecoration(dividerItemDecoration);
    }


    List<BrandListItemViewModel> getBrands() {
        List<BrandListItemViewModel> brands = new ArrayList<>();
        brands.add(new BrandListItemViewModel(new Brand("Walgreens", "Walgreens"), 7));
        brands.add(new BrandListItemViewModel(new Brand("Target", "Target"), 15));
        brands.add(new BrandListItemViewModel(new Brand("Toys and babies", "Toys and babies"), 16));
        brands.add(new BrandListItemViewModel(new Brand("Whole foods market", "Whole foods market"), 12));
        brands.add(new BrandListItemViewModel(new Brand("Blue bottle coffee co.", "Blue bottle coffee co."), 7));
        brands.add(new BrandListItemViewModel(new Brand("REI", "REI"), 7));
        return brands;
    }
}
