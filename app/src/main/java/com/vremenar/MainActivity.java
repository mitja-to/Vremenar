package com.vremenar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vremenar.adapter.adapter_mesto;
import com.vremenar.data.Mesto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private SharedPrefs prefs;                  //Objekt razreda za obdelavo shranjenih mest preko funkcije SharedPreferences
    private List<String> shranjenaMesta;        //Seznam nazivov shranjenih mest
    private List<Mesto> objektiMesta;           //Seznam objektov mest, na začetku sestoji samo iz nazivov iz seznama "shranjenaMesta" nato se dopolni z podatki iz API-ja
    MyApplication app;                          //Objekt aplikacije, namenjen za dostopanje globalnih spremenljivk razreda "MyApplication"
    private adapter_mesto am;                   //Objekt razreda "adapter_mesto", ki služi kot adapter za polnjenje glavnega seznama oz. RecyclerView-a
    private RecyclerView rv;                    //RecyclerView za prikaz elementov seznama
    private ProgressDialog progDialog;          //ProgressDialog za prikaz izvajanja funkcij v ozadju (ob prenosu informacij iz API-ja)
    private SwipeRefreshLayout srlOsvezi;       //Objekt za funkcionalnost za osveževanje podatkov ob potegu navzdol
    private TextView tvPrazno;                  //Prikaz besedila ob praznem seznamu mest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Pridobimo instanco razreda MyApplication
        app = (MyApplication)getApplication();

        //RecyclerView kot korenski element seznama mest v katerega se dinamično nalaga en oz. več CardView-ov, ki predstavljajo posamezno mesto
        rv = (RecyclerView)findViewById(R.id.rvMesta);
        LinearLayoutManager llmMesta = new LinearLayoutManager(this);
        llmMesta.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llmMesta);

        tvPrazno = (TextView)findViewById(R.id.empty_view);
        tvPrazno.setEnabled(false); //Nastavimo na disabled, da nebi prišlo do proženja dogodkov ob klikanju na TextView za prikaz praznega seznama

        //Progress dialog za prikazovanje stanja nalaganja
        progDialog = new ProgressDialog(MainActivity.this, R.style.MyTheme);
        progDialog.setIndeterminate(false);
        progDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progDialog.setCancelable(false);

        //Listener ob potegu tabele navzdol
        srlOsvezi = (SwipeRefreshLayout)findViewById(R.id.swipe);
        srlOsvezi.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Ob aktiviranju se prikaže vrteči se krogec skupaj z začetkom prenašanja novih podatkov
                srlOsvezi.setRefreshing(true);
                new LoadData().execute();
            }
        });

        //Initializiramo objekt za delo s shranjenimi mesti (SharedPrefs)
        prefs = new SharedPrefs();

        shranjenaMesta = new ArrayList<>();
        shranjenaMesta = prefs.getFavorites(getApplicationContext()); //V primeru, da obstajajo shranjena mesta jih naložimo iz datoteke

        //Nastavimo adapter za prikaz posameznih mest
        objektiMesta = new ArrayList<>();
        am = new adapter_mesto(objektiMesta, getApplicationContext(), MainActivity.this);
        rv.setAdapter(am);

        //Preverimo ali je seznam prikazanih mest prazen oz. so v njem elementi.
        if(am.getItemCount() != 0)
        {
            //V primeru, da ni prazen prikažemo RecyclerView ter omogočimo funkcijo Pull to refresh
            rv.setEnabled(true);
            rv.setVisibility(View.VISIBLE);
            tvPrazno.setVisibility(View.GONE);
            srlOsvezi.setEnabled(true);

            //Iz pridobljenih shranjenih mest ustvarimo isto število objektov razreda "Mesto", ter zgolj nastavimo nazive mest iz seznama shranjenih mest, da bomo kasneje lahko vsakemu posebej
            //dodajali podatke o vremenu
            for(String mesto : shranjenaMesta)
            {
                Mesto tmp = new Mesto();
                tmp.setNaziv(mesto);
                objektiMesta.add(tmp);
            }

            //Potem, ko imamo zbrana vsa mesta, pričnemo s prenosom informacij o vremenu za vsako mesto
            new LoadData().execute();
        }
        else
        {
            //Seznam je prazen. Onemogočimo (skrijemo) RecyclerView ter prikažemo besedilo, ki opozarja na prazen seznam
            rv.setEnabled(false);
            rv.setVisibility(View.GONE);
            tvPrazno.setVisibility(View.VISIBLE);
            srlOsvezi.setEnabled(false);
        }

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

        //Brisanje elementov iz seznama ob drsanju v levo oz. desno smer.
        //Ustvarimo ItemTouchHelper za zaznavo drsanja s prstom po posameznem seznamu. Pri tem se proži glavna metoda "onSwiped".
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                //Zaznano je bilo drsanje zato ustrezno ukrepamo
                //Najprej pridobimo informacijo katero mesto želi uporabnik izbrisati. Zato iz aktiviranega View-a pridobimo naziv mesta
                TextView tvMesto = (TextView)findViewById(R.id.tvMesto);

                //S pomočjo implementirane funkcije "vrniMesto(String)" iz naziva pridobimo celotni objekt izbranega mesta
                vrniMesto(tvMesto.getText().toString());

                //Izbrano mesto nato odstranimo iz seznama mest ter datoteke za shranjevanje
                objektiMesta.remove(app.ob_mesto);
                prefs.removeFavorite(getApplicationContext(), app.ob_mesto.getNaziv());

                //Ob uspešni odstranitvi mesta prikažemo informacijo na SnackBar-u
                CoordinatorLayout clMain = (CoordinatorLayout)findViewById(R.id.clMain);
                Snackbar snackbar = Snackbar.make(clMain, "Mesto odstranjeno", Snackbar.LENGTH_LONG);
                snackbar.show();

                //Ob zaključku osvežimo seznam mest
                rv.invalidate();

                //Preverimo, ali je nov seznam prazen oz. ima elemente. Če je prazen prikažemo informacijo o praznem seznamu, če ima elmenente pa le tega skrijemo ter prikažemo seznam
                if(objektiMesta.size() == 0)
                {
                    rv.setEnabled(false);
                    rv.setVisibility(View.GONE);
                    tvPrazno.setVisibility(View.VISIBLE);
                }
                else
                {
                    rv.setEnabled(true);
                    rv.setVisibility(View.VISIBLE);
                    tvPrazno.setVisibility(View.GONE);
                }
            }
        };

        //Ustvarjen ItemTouchHelper povežemo z RecycleView-om
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    //Ob vračanju na glavni activity se proži metoda "onResume" ob tem vedno preverimo ali se je seznam mest medtem na katerem drugem activity-u povečal oz. zmanjšal
    @Override
    protected void onResume() {
        super.onResume();

        //Pridobimo sveži seznam shranjenih mest iz datoteke
        shranjenaMesta = prefs.getFavorites(getApplicationContext());
        if(shranjenaMesta != null)
        {
            ////V primeru, da ni prazen prikažemo RecyclerView ter omogočimo funkcijo Pull to refresh
            rv.setEnabled(true);
            rv.setVisibility(View.VISIBLE);
            tvPrazno.setVisibility(View.GONE);
            srlOsvezi.setEnabled(true);

            //Iz pridobljenih shranjenih mest ustvarimo isto število objektov razreda "Mesto", ter zgolj nastavimo nazive mest iz seznama shranjenih mest, da bomo kasneje lahko vsakemu posebej
            //dodajali podatke o vremenu
            objektiMesta = new ArrayList<>();
            for(String mesto : shranjenaMesta)
            {
                Mesto tmp = new Mesto();
                tmp.setNaziv(mesto);
                objektiMesta.add(tmp);
            }

            //Nastavimo adapterju nove podatke
            adapter_mesto am = new adapter_mesto(objektiMesta, getApplicationContext(), MainActivity.this);
            rv.setAdapter(am);

            //Osvežimo informacije o vremenu
            new LoadData().execute();
        }
        else
        {
            //Seznam je prazen. Onemogočimo (skrijemo) RecyclerView ter prikažemo besedilo, ki opozarja na prazen seznam
            rv.setEnabled(false);
            rv.setVisibility(View.GONE);
            tvPrazno.setVisibility(View.VISIBLE);
            srlOsvezi.setEnabled(false);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Funkcija "vrniMesto(String)" kot vhodni parameter sprejme naziv mesta, nato poskuša podano mesto najti v seznamu objektov mest. Najdeni objekt nato nastavi kot globalno spremenljivko "ob_mesto"  v razredu "MyApplication"
    public void vrniMesto(String naziv)
    {
        Mesto tmp = new Mesto();
        for(Mesto m : objektiMesta)
        {
            if(m.getNaziv().equals(naziv))
            {
                tmp = m;
            }
        }

        app.ob_mesto = tmp;
    }

    //Razred za prenos podatkov v novi niti. Pri tem se pomikamo skozi seznam mest, kjer za vsako kličemo REST klic na API s parametrom naziva mesta. Kot odgovor
    //dobimo JSON datoteko z vsemi podatki, iz katere nato izluščimo nam potrebne
    class LoadData extends AsyncTask<String, String, String> {

        SharedPrefs prefs;                                      //Objekt SharedPrefs za delo s shranjenimi mesti
        List<String> mesta;                                     //Seznam shranjenih mest
        ArrayList<String> lsJSONmesta = new ArrayList<>();      //Seznam pridobljenih JSON datotek, shranjenih kot niz (String)

        //Preden začnemo s prenosom najprej pridobimo shranjena mesta iz datoteke, nato vklopimo ProgressDialog za prikaz, da aplikacija posodablja podatke
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Iz datoteke pridobimo shranjena mesta
            prefs = new SharedPrefs();
            mesta = prefs.getFavorites(getApplicationContext());

            //Vklop ProgressDialoga
            progDialog.show();

            //Dialog želimo prikazovati na sredini zaslona zato potrebujemo objekt zaslona oz. "Window", kateremu nastavimo željene atribute
            Window window = progDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
        }

        //Glavna metoda AsyncTask-a, ki skrbi za izvajanje kode v novi niti
        @Override
        protected String doInBackground(String... aurl) {

            //Inicializiramo seznam JSON nizov
            lsJSONmesta = new ArrayList<>();

            //Za shranjevanje prenosa v tok uporabimo InputStream
            InputStream is = null;

            try {

                //Pomikamo se skozi seznam mest ter za vsako mesto prenesemo podatke iz API-ja
                for(String sMesto : mesta)
                {
                    //URL http://api.openweathermap.org/data/2.5/weather?q= + naziv mesta + &units=metric (prikaz v stopinje celzija) + &appid=bccacdc74257e84215c17306191c8ecb (ključ aplikaicje)
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + sMesto + "&units=metric&appid=bccacdc74257e84215c17306191c8ecb");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    //Pričnemo s prenosom
                    conn.connect();
                    is = conn.getInputStream();

                    //Preneseni tok shrani koz String s pomočjo funkcije convertToString(InputStream)
                    String contentAsString = convertToString(is);

                    lsJSONmesta.add(contentAsString);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        //Preneseni tok shranimo koz String, tako da vsako vrstico "prilepimo" prejšnji
        private String convertToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        //Ob zaključku vseh prenosov pričnemo z obdelavo pridobljenih podatkov
        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);

            //Pomikamo se skozi seznam pridobljenih JSON nizov
            for(int i=0; i<lsJSONmesta.size(); i++)
            {
                try {
                    //Celotni niz shranimo kot JSONObject za nadaljno obdelavo
                    JSONObject object = new JSONObject(lsJSONmesta.get(i));

                    //Najprej rabimo podatke, ki se nahajajo v "celici" z naslovom "main", zato iz predhodnega objekta pridobimo objekt, ki vsebuje zgolj informacije, ki so hranjene v "main"
                    JSONObject ObjektPod = object.getJSONObject("main");

                    //Trenutnemu objektu mesta nastavimo temparaturo ter vlago
                    objektiMesta.get(i).setTemparatura(ObjektPod.getString("temp"));
                    objektiMesta.get(i).setVlaga(ObjektPod.getString("humidity"));

                    //Naslednje informacije se hranijo kot polje z nazivom "weather", zato pridobimo polje ter iz njega ustrezne podatke
                    JSONArray PoljeOpis = object.getJSONArray("weather");

                    //Trenutnemu objektu mesta nastavimo še opis vremena
                    objektiMesta.get(i).setOpis(PoljeOpis.getJSONObject(0).getString("description"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Ob koncu nastavimo novi adapter z novimi informacijami
            adapter_mesto am = new adapter_mesto(objektiMesta, getApplicationContext(), MainActivity.this);
            rv.setAdapter(am);

            //Prikaza o osveževanju izključimo
            srlOsvezi.setRefreshing(false);
            progDialog.dismiss();
        }
    }


}
