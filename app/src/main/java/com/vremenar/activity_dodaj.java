package com.vremenar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Mitja on 17. 11. 2015.
 */
public class activity_dodaj extends Activity
{
    private EditText etVnos;
    SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dodaj);

        //Objekt za shared prefs, datoteko, ki vsebuje vsa trenutno dodana mesta
        prefs = new SharedPrefs();

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

    }
}
