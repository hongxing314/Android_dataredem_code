package com.billaway.lyfepoints.main.home;


import android.util.Pair;

import com.billaway.lyfepoints.AppComponent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import android.util.Log;

public class ChartPresenter implements ChartContract.Presenter {
    private final AppComponent component;
    private final ChartContract.View view;
    private final CompositeDisposable disposable;

    public ChartPresenter(AppComponent component, ChartContract.View view) {
        this.component = component;
        this.view = view;
        disposable = new CompositeDisposable();
        Log.d("ChartPresenter", "ChartPresenter");
    }

    @Override
    public void start() {
        Log.d("ChartPresenter", "start");
        disposable.add(component.data().availableCredits().subscribe(view::showAvailableCredits));

        disposable.add(component.data().redeemedCredits()
                .zipWith(component.data().availableCredits(), Pair::new)
                .subscribe(view::showRedeemedUsedCredits));
    }

    @Override
    public void stop() {
        Log.d("ChartPresenter", "stop");
        disposable.clear();
    }
}
