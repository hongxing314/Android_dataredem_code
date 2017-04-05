package com.billaway.lyfepoints.transaction;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.billaway.lyfepoints.BaseActivity;
import com.billaway.lyfepoints.LyfePointsApplication;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.data.models.BrandTransaction;
import com.billaway.lyfepoints.databinding.ActivityTransactionBinding;
import com.billaway.lyfepoints.utils.DataBindingAdapters;

import java.util.Arrays;

public class TransactionActivity extends BaseActivity implements TransactionContract.View {
    private ActivityTransactionBinding binding;
    private TransactionContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction);
        setSupportActionBar(binding.toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.presenter = new TransactionPresenter(LyfePointsApplication.component(this), this);
        this.binding.tvTransactionsCount.setText("13 Transactions");
        DataBindingAdapters.loadImage(binding.brandLogo, Uri.encode("https://placeholdit.imgix.net/~text?txtsize=30&txt=REI&w=240&h=240"));
        this.binding.rvTransaction.setAdapter(new TransactionsAdapter(Arrays.asList(new BrandTransaction(), new BrandTransaction(), new BrandTransaction())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stop();
    }

    @Override
    public void setPresenter(TransactionContract.Presenter presenter) {

    }
}
