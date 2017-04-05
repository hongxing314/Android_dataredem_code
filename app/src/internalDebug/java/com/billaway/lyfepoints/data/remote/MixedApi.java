package com.billaway.lyfepoints.data.remote;


import com.billaway.lyfepoints.data.models.Brand;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.data.models.PhoneNumberSubmitResponse;
import com.billaway.lyfepoints.data.models.Status;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MixedApi implements Api {

    private final Api realApi;
    private final MockApi mockApi;

    public MixedApi(Retrofit retrofit) {
        this.mockApi = new MockApi(retrofit);
        this.realApi = retrofit.create(Api.class);
    }

    @Override
    public Single<Brand> getBrand(String brandId) {
        return mockApi.getBrand(brandId);
    }

    @Override
    public Single<List<Coupon>> getAllCouponCodes(@Url String url, @Query("token") String token) {
        return realApi.getAllCouponCodes(url, token);
    }

    @Override
    public Single<PhoneNumberSubmitResponse> submitPhoneNumber(@Field("PhoneNumber") String phoneNumber, @Field("CouponCode") String couponCode) {
        return mockApi.submitPhoneNumber(phoneNumber, couponCode);
    }

    @Override
    public Single<Status> validateCouponCode(@Url String url, @Query("token") String token, @Query("PIN") String pin) {
        return realApi.validateCouponCode(url, token, pin);
    }
}
