package com.billaway.lyfepoints;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.billaway.lyfepoints.main.MainActivity;
import com.billaway.lyfepoints.views.ReturnActivity;
import com.click2sdx.controlsdk.android.api.listeners.ResultCallback;
import com.click2sdx.controlsdk.android.model.enums.Result;


public class CreateAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final int VPN_SERVICE_REQUEST_CODE = 1000;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 1001;

    Context context;

    private String name;
    private String email;
    private String zip;
    private Spinner country;
    private String phone;
    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_account);

        //Choosing Next means that you have agreed to the terms of the Privacy Statement and Additional Data Agreement.
        TextView agreement = (TextView) findViewById(R.id.textAgreement);

        SpannableString s = new SpannableString(agreement.getText().toString());

        ClickableSpan s1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //Display Privacy Policy
            }
        };

        ClickableSpan s2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //Display Additional Data Agreement
            }
        };

        s.setSpan(s1, 61, 78, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(s2, 83, 108, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        agreement.setText(s);
        agreement.setMovementMethod(LinkMovementMethod.getInstance());

        createCountrySpinner();
        createCarrierSpinner();

        //Temp Fix
        TextView phoneView = (TextView) findViewById(R.id.phone);
        phoneView.setEnabled(true);
        TelephonyManager tMgr = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        String inputPhoneNumber = tMgr.getLine1Number();
        if(inputPhoneNumber.isEmpty()) phoneView.setText("Please input phone number");
        else phoneView.setText(tMgr.getLine1Number());

        /*
        phone = Utils.getPhoneNumber(getApplicationContext());

        if(Utils.isEmpty(phone) && !Utils.isPermissionGranted(this, Manifest.permission.READ_PHONE_STATE)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
        }

        TelephonyManager tMgr = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        TextView phoneView = (TextView) findViewById(R.id.phone);
        //Log.d("phone number: ", "" + tMgr.getLine1Number());

        //phone = "0000000000";

        try {
            phone = tMgr.getLine1Number();
            //phone = tMgr.getSubscriberId();
        } catch(NullPointerException ex) {
        }

        //if(phone.equals("") || phone == null){
        if(phone != null){
            phone = tMgr.getSubscriberId();
            phoneView.setEnabled(true);
        } else {
            phoneView.setEnabled(true);
        }


        //TextView phoneView = (TextView) findViewById(R.id.phone);
        phoneView.setText(phone);*/

        //Log.d("phone number ", "" + phone);
    }
    
    private void createCountrySpinner() {
        //Country Spinner
        country = (Spinner) findViewById(R.id.country);
        country.setOnItemSelectedListener(this);

        String[] countries;

        countries=getResources().getStringArray(R.array.countries_array);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        country.setAdapter(dataAdapter);
    }
    
    private void createCarrierSpinner() {
        
    }

    private void startHomeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initialize() {
        Intent intent = VpnService.prepare(this);

        if(intent != null){
            startActivityForResult(intent, VPN_SERVICE_REQUEST_CODE);
        } else {
            TataSdkManager.getInstance(getApplicationContext()).init(name, phone, email, new ResultCallback() {
                @Override
                public void onResult(Result result) {
                    if(result.isSuccess()){
                        startHomeActivity();
                    } else if(result.isAuthorizationRequired()){
                        showAuthorizationRequiredDialog();

                    } else {
                        Toast.makeText(getApplicationContext(), "Registration fail " + result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VPN_SERVICE_REQUEST_CODE && resultCode == RESULT_OK) {
            initialize();
        }
    }

    private void showAuthorizationRequiredDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authorization required");
        builder.setMessage("SMS with authorization code will be sent to your phone....");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //Pre SDK Registration

    public void buttonOnClick(View view) {
        switch(view.getId()){
            case R.id.next:
                final EditText t = (EditText) findViewById(R.id.phone);

                String code = country.getSelectedItem().toString();
                code = code.replaceAll("[^\\d.]", "");

                phone = code + t.getText().toString();
                Log.d("CreateAccountActivity", "phone "+ phone);
                final com.billaway.lyfepoints.views.PinEntryEditText p = (com.billaway.lyfepoints.views.PinEntryEditText) findViewById(R.id.accountPin);
                pin = p.getText().toString();

                    //Will need to eventually confirm for different countries if app rolls out internationally.
                if(pin.toCharArray().length == 4){
                    boolean forcedEligible = true; //Remove in release candidate
                    String[] carriers;
                    carriers=getResources().getStringArray(R.array.carriers_array);
                    boolean eligibleOperator = false;

                    try {
                        TelephonyManager tMgr = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
                        String simOperatorName = tMgr.getSimOperatorName();
                        //boolean eligibleOperator = false;

                        for (String c : carriers) {
                            if (c.equals(simOperatorName)) {
                                eligibleOperator = true;
                                return;
                            }
                        }
                    } catch (NullPointerException ex){
                    }

                    if(forcedEligible || eligibleOperator) {
                        setContentView(R.layout.activity_create_account_confirm);
                    } else {
                        setContentView(R.layout.activity_create_account_unsupported);
                    }
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateAccountActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("To create an account, please enter a 4 digit security PIN.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                break;
            case R.id.create:

                final EditText t2 = (EditText) findViewById(R.id.name);
                name = t2.getText().toString();
                final EditText t3 = (EditText) findViewById(R.id.zip);
                zip = t3.getText().toString();

                final EditText t4 = (EditText) findViewById(R.id.email);
                email = t4.getText().toString();

                if(name.toCharArray().length > 0 && zip.toCharArray().length == 5){
                    SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ACCOUNT_KEY_PHONE", phone);
                    editor.putString("ACCOUNT_KEY_PIN", pin);
                    editor.putString("ACCOUNT_KEY_NAME", name);
                    editor.putString("ACCOUNT_KEY_ZIP", zip);
                    if(email.toCharArray().length >0)
                        editor.putString("ACCOUNT_KEY_EMAIL", email);

                    editor.putBoolean("ACCOUNT_KEY_STATUS", true);

                    editor.apply();

                    initialize();

                } else {
                    //User alert, improper input parameters
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateAccountActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please enter you name and zip code.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }



                break;
        }

    }


    public void getAccountStatus () {
        SharedPreferences sharedPreferences = this.getSharedPreferences("ACCOUNT_KEYS", Context.MODE_PRIVATE);
        boolean accountStatus = sharedPreferences.getBoolean("ACCOUNT_KEY_STATUS", false);


        if(accountStatus) {
            Intent intent = new Intent(this, ReturnActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        Toast.makeText(adapterView.getContext(), item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
