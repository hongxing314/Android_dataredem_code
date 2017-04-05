package com.billaway.lyfepoints.transaction;


import com.billaway.lyfepoints.AppComponent;

public class TransactionPresenter implements TransactionContract.Presenter {
    private final AppComponent appComponent;
    private final TransactionContract.View view;

    public TransactionPresenter(AppComponent appComponent, TransactionContract.View view) {
        this.appComponent = appComponent;
        this.view = view;
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
