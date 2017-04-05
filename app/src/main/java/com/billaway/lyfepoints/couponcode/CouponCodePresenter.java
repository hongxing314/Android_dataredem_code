package com.billaway.lyfepoints.couponcode;

/**
 * Created by ABTBenjamins on 1/29/17.
 */

import com.billaway.lyfepoints.AppComponent;
import com.billaway.lyfepoints.data.models.Coupon;
import android.util.Log;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.billaway.lyfepoints.utils.Commons.isEmpty;

public class CouponCodePresenter implements CouponCodeContract.Presenter{
    private final AppComponent component;
    private final CouponCodeContract.View view;
    private final CompositeDisposable disposable;
    private List<Coupon> coupons;

    public CouponCodePresenter(AppComponent component, CouponCodeContract.View view) {
        Log.d("CouponCodePresenter", "CouponCodePresenter");
        this.component = component;
        this.view = view;
        this.disposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        Log.d("CouponCodePresenter", "start");
        //This is where we're grabbing the coupon codes

        disposable.add(component.data().allCouponCodes()
        .subscribe(coupons -> {
            this.coupons = coupons;
            view.onCouponAvailable(coupons);
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }));

    }

    @Override
    public void stop(){
        Log.d("CouponCodePresenter", "stop");
        disposable.clear();
    }

    @Override
    public void onCouponCodeSubmit(String couponCode) {
        Log.d("CouponCodePresenter", "onCouponCodeSubmit");
        Coupon coupon = getCouponFor(couponCode);
        if(coupon == null){
            view.showCouponCodeNotValid();
        } else {
            view.showCouponCodeSubmitProgress();
            disposable.add(component.data().validateCouponCode(couponCode)
            .subscribe(status -> {
                if(status.isSuccess()){
                    view.onCouponCodeSubmitSuccess(couponCode);
                    component.bus().post(new CouponCodeContract.CouponCodeSuccessEvent(coupon));
                } else {
                    view.showCouponCodeNotValid();
                }
            }, throwable -> view.onCouponCodeSubmitError()));
        }
    }

    private Coupon getCouponFor(String couponCode){
        Log.d("CouponCodePresenter", "Coupon");
        if(!isEmpty(coupons))
            for(Coupon coupon: coupons){
                if(couponCode.equalsIgnoreCase(coupon.getBaCouponId()))
                    return coupon;
            }
        return null;
    }

}
