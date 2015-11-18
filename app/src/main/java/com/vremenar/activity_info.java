package com.vremenar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.vremenar.data.Mesto;

/**
 * Created by Mitja on 18. 11. 2015.
 */
public class activity_info extends Activity {

    MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info);

        app = (MyApplication)getApplication();

        Mesto ob_IzbranoMesto = app.ob_mesto;

        TextView tvNaziv = (TextView)findViewById(R.id.textView_naziv);
        tvNaziv.setText(ob_IzbranoMesto.getNaziv());

        TextView tvOpis = (TextView)findViewById(R.id.textView_opis);
        tvOpis.setText(ob_IzbranoMesto.getOpis());

        TextView tvTemperatura = (TextView)findViewById(R.id.textView_temp);
        tvTemperatura.setText(ob_IzbranoMesto.getTemparatura());

        TextView tvVlaga = (TextView)findViewById(R.id.textView_vlaga);
        tvVlaga.setText(ob_IzbranoMesto.getVlaga());
    }
}
