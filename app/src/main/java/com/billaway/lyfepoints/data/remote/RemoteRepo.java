package com.billaway.lyfepoints.data.remote;

import com.billaway.lyfepoints.data.models.Brand;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.data.models.PhoneNumberSubmitResponse;
import com.billaway.lyfepoints.data.models.Status;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class RemoteRepo {
    private final Api api;

    @Inject
    public RemoteRepo(Api api) {
        this.api = api;
    }

    public Single<Brand> getBrand(String brandId) {
        return api.getBrand(brandId);
    }

    public Single<PhoneNumberSubmitResponse> submitPhoneNumber(String phoneNumber, String couponCode) {
        return api.submitPhoneNumber(phoneNumber, couponCode);
    }

    public Single<Status> validateCouponCode(String pin) {
        return api.validateCouponCode("https://api.test.billaway.com/API/Coupon/validateCoupon",
                "xmjjlp71vuqgdu4ygbjkps8931gdl6zq", pin);
    }

    public Single<List<Coupon>> getAllCouponCodes() {
        return api.getAllCouponCodes("https://api.test.billaway.com/API/Coupon/getAllCoupons/",
                "xmjjlp71vuqgdu4ygbjkps8931gdl6zq");
    }
}
