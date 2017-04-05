package com.billaway.lyfepoints.data.remote;


import com.billaway.lyfepoints.data.models.Brand;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.data.models.PhoneNumberSubmitResponse;
import com.billaway.lyfepoints.data.models.Status;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.Query;
import retrofit2.http.Url;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class MockApi implements Api {
    private final BehaviorDelegate<Api> delegate;

    public MockApi(Retrofit retrofit) {
        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setFailurePercent(2);
        behavior.setDelay(1, TimeUnit.SECONDS);
        behavior.setVariancePercent(40);
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();

        delegate = mockRetrofit.create(Api.class);
    }

    @Override
    public Single<Brand> getBrand(String brandId) {
        return delegate.returning(Calls.response(new Brand("Sample Title " + brandId, "Icon Url")))
                .getBrand(brandId);
    }

    @Override
    public Single<List<Coupon>> getAllCouponCodes(@Url String url, @Query("token") String token) {
        return null;
    }

    @Override
    public Single<PhoneNumberSubmitResponse> submitPhoneNumber(@Field("PhoneNumber") String phoneNumber,
                                                               @Field("CouponCode") String couponCode) {
        return delegate.returning(Calls.response(new PhoneNumberSubmitResponse())).submitPhoneNumber(phoneNumber, couponCode);
    }

    @Override
    public Single<Status> validateCouponCode(@Url String url, @Query("token") String token, @Query("PIN") String pin) {
        return null;
    }

}
