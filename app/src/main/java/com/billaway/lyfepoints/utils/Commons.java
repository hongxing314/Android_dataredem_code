package com.billaway.lyfepoints.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import io.reactivex.disposables.Disposable;

public class Commons {
    public static void dispose(Disposable... disposables) {
        if (disposables == null || disposables.length <= 0) return;
        for (Disposable d : disposables) {
            if (d != null && !d.isDisposed())
                d.dispose();
        }
    }

    public static void hideKeypad(@NonNull View view) {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * Returns phone number. May return null if phone number is not stored on SIM card.
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        String result = null;
        if (isPermissionGranted(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            result = telephonyManager.getLine1Number();
        }
        return result;
    }

    /**
     * Checks if the app permission is granted.
     *
     * @param context
     * @param permission android.Manifest.permission
     * @return
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public static Pair<String, String> readableFileSize(long size) {
        if (size <= 0) return new Pair<>("0", "B");
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new Pair<>(new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)), units[digitGroups]);
    }

    public static String getInfosAboutDevice(Activity a) {
        String s = "";
        try {
            PackageInfo pInfo = a.getPackageManager().getPackageInfo(
                    a.getPackageName(), PackageManager.GET_META_DATA);
            s += "\n APP Package Name: " + a.getPackageName();
            s += "\n APP Version Name: " + pInfo.versionName;
            s += "\n APP Version Code: " + pInfo.versionCode;
            s += "\n";
        } catch (PackageManager.NameNotFoundException e) {
        }
        s += "\n OS Version: " + System.getProperty("os.version") + " ("
                + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " ("
                + android.os.Build.PRODUCT + ")";
        // TODO add application version!

        // more from
        // http://developer.android.com/reference/android/os/Build.html :
        s += "\n Manufacturer: " + android.os.Build.MANUFACTURER;
        s += "\n Other TAGS: " + android.os.Build.TAGS;

        s += "\n screenWidth: "
                + a.getWindow().getWindowManager().getDefaultDisplay()
                .getWidth();
        s += "\n screenHeigth: "
                + a.getWindow().getWindowManager().getDefaultDisplay()
                .getHeight();
        s += "\n Keyboard available: "
                + (a.getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS);

        s += "\n Trackball available: "
                + (a.getResources().getConfiguration().navigation == Configuration.NAVIGATION_TRACKBALL);
        s += "\n SD Card state: " + Environment.getExternalStorageState();
        Properties p = System.getProperties();
        Enumeration keys = p.keys();
        String key = "";
        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            s += "\n > " + key + " = " + (String) p.get(key);
        }
        return s;
    }
}
