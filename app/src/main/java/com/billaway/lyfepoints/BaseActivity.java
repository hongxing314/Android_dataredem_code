package com.billaway.lyfepoints;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.billaway.lyfepoints.data.Data;
import com.billaway.lyfepoints.utils.Bus;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    protected Data data;
    @Inject
    protected Bus bus;

    protected AppComponent component;
    private Disposable busDisposable;
    private boolean subscribeToEvents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.component = LyfePointsApplication.component(this);
        this.component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (subscribeToEvents)
            busDisposable = bus.subscribe(this::onEvent);
    }

    @Override
    protected void onPause() {
        if (subscribeToEvents)
            busDisposable.dispose();
        super.onPause();
    }

    public void subscribeToEvents() {
        this.subscribeToEvents = true;
    }

    public void onEvent(Object event) {
//        Ignored
    }
}
