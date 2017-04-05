package com.billaway.lyfepoints.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.billaway.lyfepoints.main.MainActivity;
import com.billaway.lyfepoints.R;

public class ReturnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_return);

        //SpanText PIN recovery
        TextView recoverPin = (TextView) findViewById(R.id.recoverPin);

        SpannableString rP = new SpannableString(recoverPin.getText().toString());

        ClickableSpan s1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //Display PIN Recovery
                Log.d("PIN Recovery", "");
            }
        };

        rP.setSpan(s1, 17, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        recoverPin.setText(rP);
        recoverPin.setMovementMethod(LinkMovementMethod.getInstance());


        //SpanText Account Reset
        TextView accountReset = (TextView) findViewById(R.id.accountReset);

        SpannableString aR = new SpannableString(accountReset.getText().toString());


        ClickableSpan s2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //Display Account Reset
                Log.d("Account Recovery", "");
            }
        };

        aR.setSpan(s2, 26, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        accountReset.setText(aR);
        accountReset.setMovementMethod(LinkMovementMethod.getInstance());




        final TextView phone = (TextView) findViewById(R.id.returnPhone);

        String accountPhone = getPhoneNumber();

        String str0 = "";
        String str1 = "";
        String str2 = "";
        String str3 = "";

        for(int i=0; i<accountPhone.toCharArray().length; i++) {
            if(i==0){
                str0+= accountPhone.toCharArray()[i];
            } else if (i>0 && i<4) {
                str1+= accountPhone.toCharArray()[i];
            } else if(i>=4 && i<7){
                str2+= accountPhone.toCharArray()[i];
            } else if(i>=7){
                str3+= accountPhone.toCharArray()[i];
            }

        }

        phone.setText(str0 + "-(" + str1 + ")-" + str2 + "-" + str3);


    }

    public String getPhoneNumber(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);
        Log.d("phone", sharedPreferences.getString("ACCOUNT_KEY_PHONE", "no phone number saved"));
        String accountPhone = sharedPreferences.getString("ACCOUNT_KEY_PHONE", "0000000000");
        return accountPhone;
    }

    public void enterOnClick(View view){
        final com.billaway.lyfepoints.views.PinEntryEditText p = (com.billaway.lyfepoints.views.PinEntryEditText) findViewById(R.id.returnPin);
        SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);
        String pinInput = p.getText().toString();

        String pinAccount = sharedPreferences.getString("ACCOUNT_KEY_PIN", "0000");

        Log.d(pinInput, pinAccount);

        if(pinInput.equals(pinAccount)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(ReturnActivity.this).create();
            alertDialog.setTitle("Incorrect PIN");
            alertDialog.setMessage("Please enter your account PIN or use the PIN Recovery link.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }
}
