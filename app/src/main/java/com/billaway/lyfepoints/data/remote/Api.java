package com.billaway.lyfepoints.data.remote;


import com.billaway.lyfepoints.data.models.Brand;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.data.models.PhoneNumberSubmitResponse;
import com.billaway.lyfepoints.data.models.Status;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    @GET("/brand")
    Single<Brand> getBrand(String brandId);

    @GET
    Single<List<Coupon>> getAllCouponCodes(@Url String url, @Query("token") String token);

    @POST
    @FormUrlEncoded
    Single<PhoneNumberSubmitResponse> submitPhoneNumber(@Field("PhoneNumber") String phoneNumber,
                                                        @Field("CouponCode") String couponCode);

    @GET
    Single<Status> validateCouponCode(@Url String url, @Query("token") String token, @Query("PIN") String pin);
}
