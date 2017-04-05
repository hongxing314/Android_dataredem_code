package com.billaway.lyfepoints.main.redeem;


import com.billaway.lyfepoints.BasePresenter;
import com.billaway.lyfepoints.BaseView;

public class RedeemContract {
    interface Presenter extends BasePresenter {

        void startRedeem(int dataInMb);

        void confirmRedeem(int dataInMb);

        void onRedeemSuccessful();
    }

    interface View extends BaseView<Presenter> {

        void initializeAvailableCredits(Integer availableCredits);

        void showConfirmRedeemDialog(int dataInMb);

        void showRedeemProgress();

        void showRedeemSuccess();

        void showRedeemFailed();
    }

    public static class Events{
        public static class RedeemSuccess{}
    }
}
