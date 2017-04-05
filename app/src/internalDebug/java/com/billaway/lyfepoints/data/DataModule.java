package com.billaway.lyfepoints.data;

import com.billaway.lyfepoints.data.remote.Api;
import com.billaway.lyfepoints.data.remote.MixedApi;
import com.billaway.lyfepoints.data.remote.MockApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class DataModule {

    @Provides
    @Singleton
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Api provideApi(Retrofit retrofit) {
        return new MixedApi(retrofit);
    }
}
