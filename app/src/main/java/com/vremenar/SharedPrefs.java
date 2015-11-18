package com.vremenar;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mitja on 18. 11. 2015.
 */
public class SharedPrefs {

    public static final String PREFS_NAME = "com.vremenar.shranjenaMesta";
    public static final String FILE = "shranjenaMesta";

    public SharedPrefs() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<String> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FILE, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, String naziv) {
        List<String> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<String>();
        favorites.add(naziv);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, String naziv) {
        ArrayList<String> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(naziv);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<String> getFavorites(Context context)
    {
        SharedPreferences settings;
        List<String> shranjenaMesta;

        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);

        if (settings.contains(FILE))
        {
            String jsonShranjenaMesta = settings.getString(FILE, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonShranjenaMesta,
                    String[].class);

            shranjenaMesta = Arrays.asList(favoriteItems);
            shranjenaMesta = new ArrayList<String>(shranjenaMesta);
        } else
            return null;

        return (ArrayList<String>) shranjenaMesta;
    }
}
