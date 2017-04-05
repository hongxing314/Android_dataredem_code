package com.billaway.lyfepoints.couponcode;

/**
 * Created by ABTBenjamins on 1/29/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.databinding.LayoutCouponCodeBinding;

import android.util.Log;

import java.util.List;

import static com.billaway.lyfepoints.utils.Commons.hideKeypad;

public class CouponCodeView extends LinearLayout implements CouponCodeContract.View {
    private LayoutCouponCodeBinding binding;
    private CouponCodeContract.Presenter presenter;
    private ProgressDialog couponCodeSubmitProgress;

    public CouponCodeView(Context context, AttributeSet attrs){
        super(context,attrs);
        if(isInEditMode()) return;
    }

    @Override
    protected void onFinishInflate() {
        Log.d("RedeemCodeView", "onFinishInflate");
        super.onFinishInflate();
        if (isInEditMode()) return;
        this.binding = DataBindingUtil.bind(this);
        this.binding.redeem.setAlpha(0.28f);
        this.binding.redeem.setOnClickListener(view -> {
            Snackbar.make(this, R.string.coupon_code_not_available, Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok, view1 -> {
//                    Ignored
                    }).show();
        });
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d("RedeemCodeView", "onAttachedToWindow");
        super.onAttachedToWindow();
        presenter.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d("RedeemCodeView", "onDeatchedFromWindow");
        this.presenter.stop();
        super.onDetachedFromWindow();
    }

    @Override
    public void onCouponCodeSubmitSuccess(String brandId) {
        Log.d("RedeemCodeView", "onCouponCodeSubmitSuccess");
        if (couponCodeSubmitProgress != null && couponCodeSubmitProgress.isShowing())
            couponCodeSubmitProgress.dismiss();
    }

    @Override
    public void showCouponCodeNotValid() {
        Log.d("RedeemCodeView", "showCouponCodeNotValid");
        if (couponCodeSubmitProgress != null && couponCodeSubmitProgress.isShowing())
            couponCodeSubmitProgress.dismiss();
        Snackbar.make(this, R.string.invalid_coupon_code_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, view -> {
//                    Ignored
                }).show();
    }

    @Override
    public void onCouponCodeSubmitError() {
        Log.d("RedeemCodeView", "onCouponCodeSubmitError");
        if (couponCodeSubmitProgress != null && couponCodeSubmitProgress.isShowing())
            couponCodeSubmitProgress.dismiss();
        Snackbar.make(this, R.string.error_submitting_coupon_code_generic_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, view -> {
//                    Ignored
                }).show();
    }

    @Override
    public void showCouponCodeSubmitProgress() {
        Log.d("RedeemCodeView", "showCouponCodeSubmitProgress");
        couponCodeSubmitProgress = new ProgressDialog(getContext(), R.style.SDXAlertDialogStyle);
        couponCodeSubmitProgress.setTitle(R.string.coupon_code_submit_progress_header);
        couponCodeSubmitProgress.setMessage(getContext().getString(R.string.coupon_code_submit_progress_message));
        couponCodeSubmitProgress.show();
    }

    @Override
    public void onCouponAvailable(List<Coupon> coupons) {
        Log.d("RedeemCodeView", "onCouponAvailable");
        this.binding.redeem.setAlpha(1);
        this.binding.redeem.setOnClickListener(view -> {
            hideKeypad(binding.redeem);
            presenter.onCouponCodeSubmit(binding.couponCode.getText().toString().trim());
        });
    }

    @Override
    public void setPresenter(CouponCodeContract.Presenter presenter) {
        Log.d("RedeemCodeView", "setPresenter");
        this.presenter = presenter;
    }
}
