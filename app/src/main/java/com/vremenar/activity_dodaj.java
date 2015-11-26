package com.vremenar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Mitja on 17. 11. 2015.
 */
public class activity_dodaj extends AppCompatActivity
{
    SharedPrefs prefs;
    private EditText etVnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dodaj);

        //TO-DO odstranjena napaka - dodan actionbar
        getSupportActionBar().show();

        //Objekt za shared prefs, datoteko, ki vsebuje vsa trenutno dodana mesta
        prefs = new SharedPrefs();

        //Edit text
        etVnos = (EditText)findViewById(R.id.et_vnosMesta);

        //Gumb služi za potrditev vnosa naziva novega mesta
        AppCompatButton gumbPotrdi = (AppCompatButton)findViewById(R.id.gumb_potrdi);
        gumbPotrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etVnos.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Vnesite ime mesta!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Ob kliku na gumb za dodajanje mesta se vneseno mesto najprej shrani v shared prefs datoteko
                    prefs.addFavorite(getApplicationContext(), etVnos.getText().toString());

                    //Zatem, ko uspešno vnesemo novo mesto se vrnemo nazaj na prvi activity (MainActivity)
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
        return true;
    }
}
