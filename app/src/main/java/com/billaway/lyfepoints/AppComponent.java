package com.billaway.lyfepoints;


import android.content.Context;

import com.billaway.lyfepoints.data.Data;
import com.billaway.lyfepoints.data.DataModule;
import com.billaway.lyfepoints.utils.Bus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {

    Context context();

    Data data();

    Bus bus();

    void inject(BaseActivity activity);
}
