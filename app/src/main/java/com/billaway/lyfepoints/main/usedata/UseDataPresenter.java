package com.billaway.lyfepoints.main.usedata;


import com.billaway.lyfepoints.AppComponent;

import io.reactivex.disposables.CompositeDisposable;

public class UseDataPresenter implements UseDataContract.Presenter {
    private final AppComponent component;
    private final UseDataContract.View view;
    private final CompositeDisposable disposable;

    public UseDataPresenter(AppComponent component, UseDataContract.View view) {
        this.component = component;
        this.view = view;
        this.disposable = new CompositeDisposable();
    }


    @Override
    public void start() {
        disposable.add(
                component.data().redeemedCredits().subscribe(view::initializeRedeemedCredits)
        );
    }

    @Override
    public void stop() {
        disposable.clear();
    }

    @Override
    public void startUseData(int dataInMb) {
        view.showConfirmUseDataDialog(dataInMb);
    }

    @Override
    public void confirmUseData(int dataInMb) {
        view.showUseDataProgress();
        component.data().useCredits(dataInMb)
                .subscribe(view::showUseDataSuccess, error -> view.showUseDataFailed());
    }

    @Override
    public void onUseDataSuccess() {
        component.bus().post(new UseDataContract.SuccessEvent());
    }
}
