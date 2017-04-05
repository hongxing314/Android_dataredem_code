package com.billaway.lyfepoints;


import android.content.Context;

import com.billaway.lyfepoints.data.remote.ResultsDenvelopingAdapter;
import com.billaway.lyfepoints.utils.Bus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.DateFormat;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final LyfePointsApplication app;

    public AppModule(LyfePointsApplication app) {
        this.app = app;
    }

    @Provides
    Context context() {
        return app;
    }

    @Provides
    @Singleton
    Gson gson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    Retrofit retrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(new ResultsDenvelopingAdapter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    Bus bus() {
        return new Bus();
    }
}
