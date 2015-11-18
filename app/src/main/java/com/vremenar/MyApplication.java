package com.vremenar;

import android.app.Application;

import com.vremenar.data.Mesto;

/**
 * Created by Mitja on 18. 11. 2015.
 */
public class MyApplication extends Application {

    public Mesto ob_mesto;

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
