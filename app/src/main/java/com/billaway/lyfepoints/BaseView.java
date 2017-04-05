package com.billaway.lyfepoints;


public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
