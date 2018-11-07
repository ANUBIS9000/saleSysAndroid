package com.example.anubis9000.saleandorid;

/**
 * Created by anubis9000 on 2017/12/25.
 */

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    public static Context getContext() {

        return context;

    }

}
