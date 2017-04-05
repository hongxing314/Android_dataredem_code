package com.billaway.lyfepoints.sponsorship;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by ABTBenjamins on 2/17/17.
 */

public class PostProductQM extends AsyncTask<String, String, Boolean> {

    protected Boolean doInBackground(String... params) {

        int credits = Integer.valueOf(params[0]);
        String access_token = params[1];
        String phone = params[2];
        String productId = params[3];

        Log.d("PostProductQM", " | phone: " + phone);

        int units = credits/100;

        Log.d("PostProductQM", " | units " + units);


        //Post Request to Procure Api
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String orderId = UUID.randomUUID().toString();
        RequestBody body = RequestBody.create(mediaType, "{\n    orderId : \""+orderId+"\",\n    productId : \""+productId+"\",\n    merchantId: \"\",\n    msisdn : \""+phone+"\",\n    unit: "+Integer.toString(units)+",\n    currency: \"USD\",\n    vendorParameters:{\n    \t\"sponsorship_name\":\"Billaway Rewards Sponsorship\",\n    \t\"sponsorship_description\":\"Describe what sponsorship is for\",\n  \t\t\"sponsorship_type\": \"USER_REWARD\",\n  \t\t\"recipient_sdx_app_id\": \"43\",\n\t\t\"app_name\": \"Billaway Rewards Program\",\n        \"app_description\": \"Billaway Rewards Program\",\n        \"app_filename\":\"*\",\n        \"presentation_data\":\"\"\n    }\n}\n\n");


        Request request = new Request.Builder()
                .url("https://mmptata.com/tata/api/topup/procure")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", access_token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("PostProductQM", "response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success){

        if(success) {
            Log.d("PostProductQM", " Successful Post to MMP. Sponsorship Created");
        }



    }
}
