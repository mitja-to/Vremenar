package com.vremenar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vremenar.adapter.adapter_mesto;

import java.util.List;

public class MainActivity extends Activity {

    SharedPrefs prefs;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView kot korenski element seznama mest v katerega se dinamično nalaga en oz. več CardView-ov, ki predstavljajo posamezno mesto
        rv = (RecyclerView)findViewById(R.id.rvMesta);
        LinearLayoutManager llmMesta = new LinearLayoutManager(this);
        llmMesta.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llmMesta);

        List<String> shranjenaMesta = prefs.getFavorites(getApplicationContext());
        if(shranjenaMesta != null)
        {
            adapter_mesto am = new adapter_mesto(shranjenaMesta);
            rv.setAdapter(am);
        }
        else
        {

        }


        //APP ID: bccacdc74257e84215c17306191c8ecb

        //Floating action button oz. gumb za dodajanje novih mest, ki se nahaja na dnu seznama. Implementiran onClick listener, ki ob kliku
        //odpre nov activity za dodajanje mest (activity_dodaj.java)
        FloatingActionButton FAB_dodaj = (FloatingActionButton)findViewById(R.id.FAB_dodaj);
        FAB_dodaj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), activity_dodaj.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> shranjenaMesta = prefs.getFavorites(getApplicationContext());
        if(shranjenaMesta != null)
        {
            adapter_mesto am = new adapter_mesto(shranjenaMesta);
            rv.setAdapter(am);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
