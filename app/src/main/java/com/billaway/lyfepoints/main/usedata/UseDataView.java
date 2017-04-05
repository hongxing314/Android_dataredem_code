package com.billaway.lyfepoints.main.usedata;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.billaway.lyfepoints.LyfePointsApplication;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.databinding.LayoutDataUsageBinding;//.LayoutUseDataBinding;

public class UseDataView extends NestedScrollView implements UseDataContract.View {
    private final UseDataContract.Presenter presenter;
    private LayoutDataUsageBinding binding;
    private ProgressDialog useDataProgress;

    public UseDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            presenter = null;
            return;
        }

        this.presenter = new UseDataPresenter(LyfePointsApplication.component(context), this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) return;

        this.binding = DataBindingUtil.bind(this);
        //this.binding.btnUseData.setOnClickListener(view -> presenter.startUseData(binding.seekBar.getProgress()));
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
    public void setPresenter(UseDataContract.Presenter presenter) {

    }

    @Override
    public void initializeRedeemedCredits(Integer redeemedCredits) {
        /*
        binding.seekBar.setMax(redeemedCredits);
        if (redeemedCredits < 10) {
            binding.seekBar.setEnabled(false);
            binding.seekBar.setProgress(redeemedCredits);
            binding.tvRemainingData.setText(0 + " MB");
            binding.tvRemainingLabel.setText("Data cannot be used");
            binding.btnUseData.setEnabled(false);
        } else {
            binding.btnUseData.setEnabled(true);
            binding.tvRemainingLabel.setText(R.string.will_be_remaining);
            binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    if (progress < 10) {
                        progress = 10;
                        seekBar.setProgress(progress);
                    }
                    binding.etRedeemingAmount.setText(progress + " MB");
                    binding.tvRemainingData.setText((redeemedCredits - progress) + " MB");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            binding.seekBar.setProgress(10);
        }*/
    }

    @Override
    public void showConfirmUseDataDialog(int dataInMb) {
        new AlertDialog.Builder(getContext(), R.style.SDXAlertDialogStyle)
                .setTitle(R.string.use_data)
                .setMessage(String.format(getContext().getString(R.string.confirm_use_data), dataInMb))
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> presenter.confirmUseData(dataInMb))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void showUseDataProgress() {
        useDataProgress = new ProgressDialog(getContext(), R.style.SDXAlertDialogStyle);
        useDataProgress.setTitle(R.string.use_data);
        useDataProgress.setMessage(getContext().getString(R.string.please_wait_while_process_completes));
        useDataProgress.show();
    }

    @Override
    public void showUseDataSuccess() {
        if (useDataProgress != null && useDataProgress.isShowing())
            useDataProgress.cancel();
        new AlertDialog.Builder(getContext(), R.style.SDXAlertDialogStyle)
                .setTitle(R.string.use_data)
                .setMessage(R.string.add_data_successful)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .setOnDismissListener(dialogInterface -> presenter.onUseDataSuccess())
                .show();
    }

    @Override
    public void showUseDataFailed() {
        if (useDataProgress != null && useDataProgress.isShowing())
            useDataProgress.cancel();
        Snackbar.make(binding.getRoot(), R.string.failed_to_use_data, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, view -> {
//                        IGNORED
                }).show();
    }
}
