package com.vremenar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    SharedPrefs prefs;
    List<String> shranjenaMesta;
    private RecyclerView rv;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView kot korenski element seznama mest v katerega se dinamično nalaga en oz. več CardView-ov, ki predstavljajo posamezno mesto
        rv = (RecyclerView)findViewById(R.id.rvMesta);
        LinearLayoutManager llmMesta = new LinearLayoutManager(this);
        llmMesta.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llmMesta);

        //Progress dialog za prikazovanje stanja nalaganja
        progDialog = new ProgressDialog(MainActivity.this, R.style.MyTheme);
        progDialog.setIndeterminate(false);
        progDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progDialog.setCancelable(false);

        prefs = new SharedPrefs();
        shranjenaMesta = prefs.getFavorites(getApplicationContext());
        if(shranjenaMesta != null)
        {
            new LoadData().execute();
        }
        else
        {
            //Sporočimo, da je seznam prazen
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
            new LoadData().execute();
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

    class LoadData extends AsyncTask<String, String, String> {

        SharedPrefs prefs;
        List<String> mesta;
        ArrayList<String> lsJSONmesta = new ArrayList<>();
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            prefs = new SharedPrefs();
            mesta = prefs.getFavorites(getApplicationContext());
            progDialog.show();

            Window window = progDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

        }

        @Override
        protected String doInBackground(String... aurl) {

            lsJSONmesta = new ArrayList<>();
            InputStream is = null;
            try {
                for(String sMesto : mesta)
                {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + sMesto + "&appid=bccacdc74257e84215c17306191c8ecb");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    is = conn.getInputStream();

                    String contentAsString = convertStreamToString(is);

                    //int start = contentAsString.indexOf("{", contentAsString.indexOf("{") + 1);
                    //int end = contentAsString.lastIndexOf("}");

                    //String jsonResponse = contentAsString.substring(start, end);
                    lsJSONmesta.add(contentAsString);
                    //lsJSONmesta.add(contentAsString.substring(start, end));
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

        private String convertStreamToString(InputStream is) {
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


        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);

            for(int i=0; i<lsJSONmesta.size(); i++)
            {
                try {

                    JSONObject object = new JSONObject(lsJSONmesta.get(i));

                    JSONObject ObjektPod = object.getJSONObject("main");
                    String temparatura = ObjektPod.getString("temp");
                    String vlaga = ObjektPod.getString("humidity");

                    JSONArray PoljeOpis = object.getJSONArray("weather");

                    String opis = PoljeOpis.getJSONObject(0).getString("description");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            List<Mesto> objektiMesta = new ArrayList<>();
            for(String mesto : shranjenaMesta)
            {
                Mesto tmp = new Mesto();
                tmp.setNaziv(mesto.toString());
                objektiMesta.add(tmp);
            }
            adapter_mesto am = new adapter_mesto(objektiMesta);
            rv.setAdapter(am);

            progDialog.dismiss();
        }
    }


}
