package com.billaway.lyfepoints.data.local.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LyfePointsPrefs {
    private final SharedPreferences prefs;
    private static final String KEY_EXPLANATION_SHOWN = "_explanation_shown";

    @Inject
    public LyfePointsPrefs(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public boolean isExplanationShown() {
        return prefs.getBoolean(KEY_EXPLANATION_SHOWN, false);
    }

    public void setExplanationShown(boolean explanationShown) {
        prefs.edit().putBoolean(KEY_EXPLANATION_SHOWN, explanationShown).apply();
    }


    public void addCredits(int dataInMb) {
        if (dataInMb < 0) throw new IllegalStateException("credits cannot be -ve");
        int currentCredit = prefs.getInt("totalCredits", 0);
        currentCredit += dataInMb;
        prefs.edit().putInt("totalCredits", currentCredit).apply();
    }


    public void redeemCredits(int dataInMb) {
        if (dataInMb < 0) throw new IllegalArgumentException("credits cannot be -ve");
        int currentCredit = prefs.getInt("totalCredits", 0);
        if (dataInMb > currentCredit)
            throw new IllegalArgumentException("redeem abount > than currentCredit");
        currentCredit -= dataInMb;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("totalCredits", currentCredit);
        editor.putInt("redeemedCredits", prefs.getInt("redeemedCredits", 0) + dataInMb);
        editor.apply();
    }

    public void useCredits(int dataInMb) {
        if (dataInMb < 0) throw new IllegalArgumentException("credits cannot be -ve");
        int redeemedCredits = prefs.getInt("redeemedCredits", 0);
        if (dataInMb > redeemedCredits)
            throw new IllegalArgumentException("use amount > than redeemed credits");
        redeemedCredits -= dataInMb;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("redeemedCredits", redeemedCredits);
        editor.putInt("usedCredits", prefs.getInt("usedCredits", 0) + dataInMb);
        editor.apply();
    }

    public Integer getAvailableCredits() {
        return prefs.getInt("totalCredits", 0);
    }

    public Integer getRedeemedCredits() {
        return prefs.getInt("redeemedCredits", 0);
    }


    public Integer getUsedCredits() {
        return prefs.getInt("usedCredits", 0);
    }
}
