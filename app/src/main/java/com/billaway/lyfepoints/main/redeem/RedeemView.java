package com.billaway.lyfepoints.main.redeem;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;

import com.billaway.lyfepoints.LyfePointsApplication;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.databinding.LayoutCouponCodeBinding;
import com.billaway.lyfepoints.databinding.LayoutHomeBinding;

public class RedeemView extends NestedScrollView implements RedeemContract.View {
    private LayoutCouponCodeBinding binding;
    private LayoutHomeBinding homeBinding;
    private final RedeemContract.Presenter presenter;
    private ProgressDialog redeemProgress;

    public RedeemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            presenter = null;
            return;
        }

        this.presenter = new RedeemPresenter(LyfePointsApplication.component(context), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) return;
        this.binding = DataBindingUtil.bind(this);
        this.binding.redeem.setOnClickListener(view -> presenter.startRedeem(homeBinding.dataArc.getProgress()));
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.stop();
    }


    @Override
    public void initializeAvailableCredits(Integer availableCredits) {
        //WHY WON'T IT LET ME SET THE MAX THIS IS FUCKING SHITTY MANG.
        // Try going through the View & changing the Max value via the XML.
        //homeBinding.dataArc.setMax(availableCredits); //setMax(availableCredits);
        //homeBinding.dataArc.setMax
        //R.integer.availableCredits = availableCredits;

        if (availableCredits < 100) {
            homeBinding.dataArc.setEnabled(false);
            homeBinding.dataArc.setProgress(availableCredits);
            homeBinding.txtMB.setText(0 + " MB");
            //binding.tvRemainingLabel.setText("Redeem not available");
            homeBinding.btnApply.setEnabled(false);
        } else {
            homeBinding.btnApply.setEnabled(true);
            //binding.tvRemainingLabel.setText(R.string.will_be_remaining);

            homeBinding.dataArc.setOnSeekArcChangeListener(new com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener() {
                @Override
                public void onProgressChanged(com.triggertrap.seekarc.SeekArc seekArc, int progress, boolean b) {
                    if (progress < 100) {
                        progress = 100;
                        seekArc.setProgress(progress);
                    }
                    //CHANGE THIS TO SHOWCASE HOW MUCH WILL BE REDEEMED IN THE SEEKARC THUMB BUTTON I HAVE TO MAKE SOMEHOW BECAUSE FUCK ME THAT'S WHY
                    //binding.etRedeemingAmount.setText(progress + " MB");
                    homeBinding.txtMB.setText((availableCredits - progress) + " MB");
                }

                @Override
                public void onStartTrackingTouch(com.triggertrap.seekarc.SeekArc seekArc) {

                }

                @Override
                public void onStopTrackingTouch(com.triggertrap.seekarc.SeekArc seekArc) {

                }
            });

            homeBinding.dataArc.setProgress(100);
        }
    }

    @Override
    public void showConfirmRedeemDialog(int dataInMb) {
        new AlertDialog.Builder(getContext(), R.style.SDXAlertDialogStyle)
                .setTitle(R.string.redeem)
                .setMessage(String.format(getContext().getString(R.string.confirm_redeem_message), dataInMb))
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> presenter.confirmRedeem(dataInMb))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void showRedeemProgress() {
        redeemProgress = new ProgressDialog(getContext(), R.style.SDXAlertDialogStyle);
        redeemProgress.setTitle(R.string.redeem);
        redeemProgress.setMessage(getContext().getString(R.string.please_wait_while_process_completes));
        redeemProgress.show();
    }

    @Override
    public void showRedeemSuccess() {
        if (redeemProgress != null && redeemProgress.isShowing())
            redeemProgress.cancel();
        new AlertDialog.Builder(getContext(), R.style.SDXAlertDialogStyle)
                .setTitle(R.string.redeem)
                .setMessage(R.string.redeem_successful)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .setOnDismissListener(dialogInterface -> presenter.onRedeemSuccessful())
                .show();
    }

    @Override
    public void showRedeemFailed() {
        if (redeemProgress != null && redeemProgress.isShowing())
            redeemProgress.cancel();
        Snackbar.make(binding.getRoot(),R.string.failed_to_redeem_data,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, view -> {
//                        IGNORED
                }).show();
    }

    @Override
    public void setPresenter(RedeemContract.Presenter presenter) {

    }
}
