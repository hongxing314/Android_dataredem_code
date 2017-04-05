package com.billaway.lyfepoints.couponcode;

/**
 * Created by ABTBenjamins on 1/29/17.
 */

import com.billaway.lyfepoints.BasePresenter;
import com.billaway.lyfepoints.BaseView;
import com.billaway.lyfepoints.data.models.Coupon;
import android.util.Log;

import java.util.List;

public class CouponCodeContract {
    interface Presenter extends BasePresenter{
        void onCouponCodeSubmit(String couponCode);
    }

    public interface View extends BaseView<Presenter>{
        void onCouponCodeSubmitSuccess(String brandId);

        void showCouponCodeNotValid();

        void onCouponCodeSubmitError();

        void showCouponCodeSubmitProgress();

        void onCouponAvailable(List<Coupon> coupons);
    }

    public static class CouponCodeSuccessEvent{
        public final Coupon coupon;

        public CouponCodeSuccessEvent(Coupon coupon) {
            Log.d("CouponCodeContract", "CouponCodeSuccessEvent");
            this.coupon = coupon;
        }
    }
}
