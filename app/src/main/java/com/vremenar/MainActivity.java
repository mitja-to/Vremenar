package com.vremenar;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vremenar.adapter.adapter_mesto;
import com.vremenar.data.Mesto;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView kot korenski element seznama mest v katerega se dinamično nalaga en oz. več CardView-ov, ki predstavljajo posamezno mesto
        RecyclerView rv = (RecyclerView)findViewById(R.id.rvMesta);
        LinearLayoutManager llmMesta = new LinearLayoutManager(this);
        llmMesta.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llmMesta);

        ArrayList<Mesto> mojaMesta = new ArrayList<>(2);

        Mesto ms = new Mesto("Murska");
        Mesto mb = new Mesto("Maribor");
        Mesto ce = new Mesto("Celje");

        mojaMesta.add(ms);
        mojaMesta.add(mb);
        mojaMesta.add(ce);

        adapter_mesto am = new adapter_mesto(mojaMesta);
        rv.setAdapter(am);

        //Floating action button oz. gumb za dodajanje novih mest, ki se nahaja na dnu seznama. Implementiran onClick listener
        FloatingActionButton FAB_dodaj = (FloatingActionButton)findViewById(R.id.FAB_dodaj);
        FAB_dodaj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Kliknil si me!", Toast.LENGTH_SHORT).show();
            }
        });
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
