package com.billaway.lyfepoints.transaction;


import com.billaway.lyfepoints.BasePresenter;
import com.billaway.lyfepoints.BaseView;

public interface TransactionContract {
    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

    }
}
