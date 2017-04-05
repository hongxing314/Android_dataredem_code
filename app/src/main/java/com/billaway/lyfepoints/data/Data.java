package com.billaway.lyfepoints.data;

import com.billaway.lyfepoints.data.local.LocalRepo;
import com.billaway.lyfepoints.data.models.Brand;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.data.models.PhoneNumberSubmitResponse;
import com.billaway.lyfepoints.data.models.Status;
import com.billaway.lyfepoints.data.remote.RemoteRepo;
import com.billaway.lyfepoints.phonenumber.DeviceInfoProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class Data {

    private final LocalRepo localRepo;
    private final RemoteRepo remoteRepo;

    @Inject
    public Data(LocalRepo localRepo, RemoteRepo remoteRepo) {
        this.remoteRepo = remoteRepo;
        this.localRepo = localRepo;
    }

    public Single<Status> validateCouponCode(String couponCode) {
        return remoteRepo.validateCouponCode(couponCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Brand> getBrand(String brandId, DeviceInfoProvider infoProvider) {
        return remoteRepo.getBrand(brandId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PhoneNumberSubmitResponse> submitPhoneNumber(Coupon coupon, String phoneNumber, String couponCode) {
        return remoteRepo.submitPhoneNumber(phoneNumber, couponCode)
                .doOnSuccess(response -> localRepo.addData((int) coupon.getCreditAwarded()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean isExplanationShown() {
//        return localRepo.isExplanationShown();TODO uncomment when ready
        return true;
    }

    public void setExplanationShown(boolean explanationShown) {
        localRepo.setExplanationShown(explanationShown);
    }

    public Single<Integer> availableCredits() {
        return localRepo.getAvailableCredits()
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable redeemCredits(int dataInMb) {
        return localRepo.redeemCredits(dataInMb)
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> redeemedCredits() {
        return localRepo.getRedeemedCredits()
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> usedCredits() {
        return localRepo.getUsedCredits();
    }

    public Completable useCredits(int dataInMb) {
        return localRepo.useCredits(dataInMb);
    }

    public Single<List<Coupon>> allCouponCodes() {
        return remoteRepo.getAllCouponCodes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
