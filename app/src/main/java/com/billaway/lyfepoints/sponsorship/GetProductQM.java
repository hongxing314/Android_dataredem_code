package com.billaway.lyfepoints.sponsorship;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by ABTBenjamins on 2/17/17.
 */

public class GetProductQM extends AsyncTask<String, String, Boolean> {

    String credits;
    String access_token;
    String phone;
    String productId;


    protected Boolean doInBackground(String... params) {
        credits = params[0];
        access_token = "Bearer " + params[1];
        phone = params[2];

        Log.d("GetProductQM", " | phone: " + phone);


        //First get data products because we need to know what we can acquire
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://mmptata.com/tata/api/topup/dataproducts/" + phone)
                .get()
                .addHeader("authorization", access_token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            JSONArray JarrayResult = Jobject.getJSONArray("result");

            Log.d("GetProductQM", " | JarrayResult.length: " + JarrayResult.length());

            for(int i=0; i< JarrayResult.length(); i++){

                    JSONObject resultObject = JarrayResult.getJSONObject(i);

                    Log.d("GetProductQM", " | MBOBject: " + resultObject.getString("dataMB"));

                    if(resultObject.getString("dataMB").equals("100.0")){
                        productId = resultObject.getString("productId");
                    }

            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    protected void onPostExecute(Boolean success){

        if(success) {
            Log.d("GetProductQM", " Product selected | productId: " + productId);
            new PostProductQM().execute(credits, access_token, phone, productId);
        }



    }
}
