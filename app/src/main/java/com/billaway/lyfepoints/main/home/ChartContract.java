package com.billaway.lyfepoints.main.home;


import android.util.Pair;

import com.billaway.lyfepoints.BasePresenter;
import com.billaway.lyfepoints.BaseView;

public class ChartContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showAvailableCredits(Integer availableCredits);

        void showRedeemedUsedCredits(Pair<Integer, Integer> redeemedUsed);
    }


}
