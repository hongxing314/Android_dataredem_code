package com.billaway.lyfepoints.phonenumber;


import android.support.annotation.NonNull;
import android.util.Log;

import com.billaway.lyfepoints.AppComponent;
import com.billaway.lyfepoints.data.models.Coupon;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

class PhoneNumberPresenter implements PhoneNumberContract.Presenter {

    private final AppComponent component;
    private final PhoneNumberContract.View view;
    private final Coupon coupon;
    private final DeviceInfoProvider deviceInfoProvider;
    private final CompositeDisposable disposable;

    PhoneNumberPresenter(@NonNull AppComponent component,
                         @NonNull PhoneNumberContract.View view,
                         @NonNull Coupon coupon,
                         @NonNull DeviceInfoProvider deviceInfoProvider) {
        this.component = component;
        this.view = view;
        this.coupon = coupon;
        this.deviceInfoProvider = deviceInfoProvider;
        this.disposable = new CompositeDisposable();
        Log.d("PhoneNumberPresenter","PhoneNumberPresenter");
    }


    @Override
    public void start() {
        Log.d("PhoneNumberPresenter","start");
        view.showCouponLoading();
        disposable.add(Completable.complete()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showCoupon(coupon), throwable -> view.showCouponLoadingError()));
    }

    @Override
    public void stop() {
        Log.d("PhoneNumberPresenter","stop");
        disposable.clear();
    }

    @Override
    public void onPhoneNumberSubmit() {
        Log.d("PhoneNumberPresenter","onPhoneNumberSubmit");
        view.showPhoneNumberSubmitProgress();
        disposable.add(component.data().submitPhoneNumber(coupon, "PhoneNumber", "CouponCode")
                .subscribe(response -> view.showPhoneNumberSubmitSuccess(coupon),
                        throwable -> view.showPhoneNumberSubmitError()));
    }

    @Override
    public void onShare() {
        Log.d("PhoneNumberPresenter","onShare");
        component.bus().post(new PhoneNumberContract.ShareRequestEvent(coupon));
    }

    @Override
    public void goHome() {
        Log.d("PhoneNumberPresenter","goHome");
        component.bus().post(new PhoneNumberContract.HomeScreenRequest());
    }

    @Override
    public void onCreateSponsorship() {
        component.bus().post(new PhoneNumberContract.CreateSponsorshipEvent(coupon));
    }
}
