package com.billaway.lyfepoints.data.local;

import com.billaway.lyfepoints.data.local.sharedpreference.LyfePointsPrefs;
import com.billaway.lyfepoints.data.local.sqlite.LyfepointsDb;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

@Singleton
public class LocalRepo {
    private final LyfePointsPrefs prefs;
    private final LyfepointsDb db;

    @Inject
    public LocalRepo(LyfePointsPrefs prefs, LyfepointsDb db) {
        this.prefs = prefs;
        this.db = db;
    }

    public void addData(int dataInMb) {
        prefs.addCredits(dataInMb);
    }


    public boolean isExplanationShown() {
        return prefs.isExplanationShown();
    }

    public void setExplanationShown(boolean explanationShown) {
        prefs.setExplanationShown(explanationShown);
    }


    public Single<Integer> getAvailableCredits() {
        return Single.just(prefs.getAvailableCredits());
    }

    public Completable redeemCredits(int dataInMb) {
        return Completable.fromAction(() -> prefs.redeemCredits(dataInMb));
    }

    public Single<Integer> getRedeemedCredits() {
        return Single.just(prefs.getRedeemedCredits());
    }

    public Completable useCredits(int dataInMb) {
        return Completable.fromAction(() -> prefs.useCredits(dataInMb));
    }

    public Single<Integer> getUsedCredits(){
        return Single.just(prefs.getUsedCredits());
    }
}
