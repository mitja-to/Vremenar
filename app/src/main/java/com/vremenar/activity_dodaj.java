package com.vremenar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * Created by Mitja on 17. 11. 2015.
 */
public class activity_dodaj extends Activity
{
    SharedPrefs prefs;
    private EditText etVnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.layout_dodaj);

        //Objekt za shared prefs, datoteko, ki vsebuje vsa trenutno dodana mesta
        prefs = new SharedPrefs();

        //Edit text
        etVnos = (EditText)findViewById(R.id.et_vnosMesta);

        //Gumb služi za potrditev vnosa naziva novega mesta
        AppCompatButton gumbPotrdi = (AppCompatButton)findViewById(R.id.gumb_potrdi);
        gumbPotrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ob kliku na gumb za dodajanje mesta se vneseno mesto najprej shrani v shared prefs datoteko
                prefs.addFavorite(getApplicationContext(), etVnos.getText().toString());

                //Zatem, ko uspešno vnesemo novo mesto se vrnemo nazaj na prvi activity (MainActivity)
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabZapri = (FloatingActionButton)findViewById(R.id.FAB_zapri);
        fabZapri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
