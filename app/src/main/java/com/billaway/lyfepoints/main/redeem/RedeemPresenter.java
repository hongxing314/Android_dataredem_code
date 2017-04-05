package com.billaway.lyfepoints.main.redeem;


import com.billaway.lyfepoints.AppComponent;

public class RedeemPresenter implements RedeemContract.Presenter {

    private final AppComponent component;
    private final RedeemContract.View view;

    public RedeemPresenter(AppComponent component, RedeemContract.View view) {
        this.component = component;
        this.view = view;
    }


    @Override
    public void start() {
        component.data().availableCredits().subscribe(view::initializeAvailableCredits);
    }

    @Override
    public void stop() {

    }

    @Override
    public void startRedeem(int dataInMb) {
        view.showConfirmRedeemDialog(dataInMb);
    }

    @Override
    public void confirmRedeem(int dataInMb) {
        view.showRedeemProgress();
        component.data().redeemCredits(dataInMb)
                .subscribe(view::showRedeemSuccess, error -> view.showRedeemFailed());

    }

    @Override
    public void onRedeemSuccessful() {
        component.bus().post(new RedeemContract.Events.RedeemSuccess());
    }
}
