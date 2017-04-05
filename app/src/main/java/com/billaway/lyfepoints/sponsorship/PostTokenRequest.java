package com.billaway.lyfepoints.sponsorship;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.billaway.lyfepoints.main.MainActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ABTBenjamins on 2/17/17.
 */

public class PostTokenRequest extends AsyncTask<String, String, Boolean> {
    String credits;
    String access_token;
    String phone;

    private ProgressDialog sponsorshipProgress;

    public PostTokenRequest(MainActivity activity){
        sponsorshipProgress = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        sponsorshipProgress.setMessage("Creating Mobile Data");
        sponsorshipProgress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String credentials = params[0];
        credits = params[1];
        phone = params[2];

        byte[] data = credentials.getBytes(StandardCharsets.UTF_8);
        String client_credentials = "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
        Request request = new Request.Builder()
                .url("https://mmptata.com/server/oauth/token?grant_type=client_credentials")
                .post(body)
                .addHeader("authorization", client_credentials)
                .addHeader("cache-control", "no-cache")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            JSONObject Jobject = null;

            try {
                Jobject = new JSONObject(jsonData);

                access_token = Jobject.getString("access_token");

                Log.d("PostTokenRequest", "Access Token: " + access_token);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success){

        if(success) {
            Log.d("PostTokenRequest", " Success Access to MMP. Queued Credits: " + credits);
            sponsorshipProgress.dismiss();
            new GetProductQM().execute(credits, access_token, phone);
        }



    }

}
