package com.billaway.lyfepoints.utils;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class Bus {

    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    public void post(Object o) {
        bus.accept(o);
    }

    public Disposable subscribe(Consumer<Object> consumer) {
        return this.bus.subscribe(consumer);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}