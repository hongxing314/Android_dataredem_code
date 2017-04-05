package com.billaway.lyfepoints.couponcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.billaway.lyfepoints.BaseActivity;
import com.billaway.lyfepoints.LyfePointsApplication;
import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.main.MainActivity;
import com.billaway.lyfepoints.phonenumber.PhoneNumberActivity;

import io.reactivex.disposables.Disposable;

import static com.billaway.lyfepoints.utils.Commons.dispose;

public class CouponCodeActivity extends BaseActivity {
    private CouponCodeContract.Presenter redeemCodePresenter;
    private String couponCode;

    private Disposable closeDisposable;
    boolean closeVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("CouponCodeActivity", "onCreate");
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_coupon_code);

        this.redeemCodePresenter = new CouponCodePresenter(LyfePointsApplication.component(this),
                (CouponCodeContract.View) findViewById(R.id.viewRedeemCode));
        subscribeToEvents();



    }
    //Old redemption implementation


    @Override
    public void onEvent(Object event) {
        Log.d("CouponCodeActivity", "onEvent");
        if(event instanceof CouponCodeContract.CouponCodeSuccessEvent){
            //What to do when the coupon code is redeemed successfully
            startPhoneNumberScreen((CouponCodeContract.CouponCodeSuccessEvent) event);
        }
    }

    @Override
    protected void onResume(
            
    ) {
        Log.d("CouponCodeActivity", "onResume");
        super.onResume();
        closeDisposable = component.data().availableCredits()
                .subscribe(credits -> {
                    if (credits> 0){
                        closeVisible = true;
                        invalidateOptionsMenu();
                        supportInvalidateOptionsMenu();
                    }
                });
    }

    @Override
    protected void onPause() {
        Log.d("CouponCodeActivity", "onPause");
        super.onPause();
        dispose(closeDisposable);
    }

    private void startPhoneNumberScreen(CouponCodeContract.CouponCodeSuccessEvent event){
        Log.d("CouponCodeActivity", "queueNewData");
        //change to MainActivity & implement PhoneNumberActivity.class into MainActivity
        //end of ye line, needs more work here.
        Intent intent = new Intent(this, PhoneNumberActivity.class);
        intent.putExtra(PhoneNumberActivity.KEY_COUPON, event.coupon);
        startActivity(intent);
    }

    //New Redemption Implementation
    public void OnClickRedeem(View view){
        Log.d("CouponCodeActivity", "onClickRedeem");
        switch(view.getId()){
            case R.id.redeem:
                final TextView c = (TextView) findViewById(R.id.couponCode);
                couponCode = c.getText().toString();

                if(couponCode.toCharArray().length < 5){
                    AlertDialog alertDialog = new AlertDialog.Builder(CouponCodeActivity.this).create();
                    alertDialog.setTitle("Invalid Code");
                    alertDialog.setMessage("We have encountered an issue with your redemption. Please try again or contact us.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    //Need to cross check the couponCode with all valid sponsorship codes before flagging alert
                    if(couponCode.equals("00000")){

                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CouponCodeActivity.this).create();
                        alertDialog.setTitle("Invalid Code");
                        alertDialog.setMessage("There seems to be an issue with your coupon code.  Please try again.  If you require assistance, please Contact Us.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }

                break;
            case R.id.redeem_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
        }
    }

}
