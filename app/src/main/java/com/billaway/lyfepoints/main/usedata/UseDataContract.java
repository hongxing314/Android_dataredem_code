package com.billaway.lyfepoints.main.usedata;


import com.billaway.lyfepoints.BasePresenter;
import com.billaway.lyfepoints.BaseView;

public class UseDataContract {
    interface Presenter extends BasePresenter {

        void startUseData(int dataInMb);

        void confirmUseData(int dataInMb);

        void onUseDataSuccess();
    }

    interface View extends BaseView<Presenter> {

        void initializeRedeemedCredits(Integer redeemedCredits);

        void showConfirmUseDataDialog(int dataInMb);

        void showUseDataProgress();

        void showUseDataSuccess();

        void showUseDataFailed();
    }

    public static class SuccessEvent{

    }
}
