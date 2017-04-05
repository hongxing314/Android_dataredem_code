package com.billaway.lyfepoints.phonenumber;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.data.models.Coupon;
import com.billaway.lyfepoints.databinding.LayoutPhoneNumberBinding;

public class PhoneNumberView extends FrameLayout implements PhoneNumberContract.View {
    private LayoutPhoneNumberBinding binding;
    private PhoneNumberContract.Presenter presenter;
    private ProgressDialog phoneNumberDialog;

    public PhoneNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("PhoneNumberView","PhoneNumberView");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("PhoneNumberView","onFinishInflate");
        if (isInEditMode()) return;
        this.binding = DataBindingUtil.bind(this);
        this.binding.btnNext.setOnClickListener(view -> presenter.onPhoneNumberSubmit());
        this.binding.contentState.setContent(this.binding.content);
    }


    @Override
    public void showCouponLoading() {
        Log.d("PhoneNumberView","showCouponLoading");
        this.binding.contentState.showProgress();
    }

    @Override
    public void showCoupon(Coupon coupon) {
        Log.d("PhoneNumberView","showCoupon");
        this.binding.setCoupon(coupon);
        presenter.onPhoneNumberSubmit();
        //this.binding.contentState.showContent();
    }

    @Override
    public void showCouponLoadingError() {
        Log.d("PhoneNumberView","showCouponLoadingError");
        this.binding.contentState.showError(null);
    }

    @Override
    public void showPhoneNumberSubmitProgress() {
        Log.d("PhoneNumberView","showPhoneNumberSubmitProgress");
        phoneNumberDialog = new ProgressDialog(getContext(), R.style.SDXAlertDialogStyle);
        phoneNumberDialog.setTitle(R.string.submiting_details);
        phoneNumberDialog.setMessage(getContext().getString(R.string.please_wait_while_process_completes));
        phoneNumberDialog.show();
    }

    @Override
    public void showPhoneNumberSubmitError() {
        Log.d("PhoneNumberView","showPhoneNumberSubmitError");
        if (phoneNumberDialog != null && phoneNumberDialog.isShowing())
            phoneNumberDialog.dismiss();
        Snackbar.make(this, R.string.phone_number_submit_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, view -> presenter.onPhoneNumberSubmit()).show();
    }

    @Override
    public void showPhoneNumberSubmitSuccess(Coupon coupon) {
        Log.d("PhoneNumberView","showPhoneNumberSubmitSuccess");
        if (phoneNumberDialog != null && phoneNumberDialog.isShowing())
            phoneNumberDialog.dismiss();
        showCongratulationsDialog(coupon);
        showCreateSponsorship(coupon);
    }

    private void showCongratulationsDialog(Coupon coupon) {
        Log.d("PhoneNumberView","showCongratulationsDialog");
        new AlertDialog.Builder(getContext(), R.style.SDXAlertDialogStyle)
                .setTitle(R.string.congratulations)
                .setMessage("You have received " + coupon.getCreditAwarded() + "MBs from " + coupon.getBrandName() +
                        ". Redeem to start using mobile data.")
                .setPositiveButton(R.string.share, (dialogInterface, i) -> presenter.onShare())
                .setNegativeButton(R.string.ok, (dialogInterface, i) -> presenter.goHome())
                .show();
    }

    public void showCreateSponsorship(Coupon coupon) {
        presenter.onCreateSponsorship();

    }

    @Override
    public void setPresenter(PhoneNumberContract.Presenter presenter) {
        Log.d("PhoneNumberView","setPresenter");
        this.presenter = presenter;
    }
}
