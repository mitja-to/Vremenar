package com.vremenar;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    //S pomočjo funkcije preverimo, ali je naprava povezana z internetom, da lahko prenesemo podatke
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            //Ni aktivnih povezav
            return false;
        } else
            return true;
    }
}
