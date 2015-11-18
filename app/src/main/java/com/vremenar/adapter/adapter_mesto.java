package com.vremenar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vremenar.MainActivity;
import com.vremenar.R;
import com.vremenar.activity_info;
import com.vremenar.data.Mesto;

import java.util.List;

/**
 * Created by Mitja on 17. 11. 2015.
 */
public class adapter_mesto extends RecyclerView.Adapter<adapter_mesto.viewHolder_mesto> {

    private List<Mesto> lMesto;
    Context context;
    MainActivity actMain;

    public adapter_mesto(List<Mesto> listMesto, Context c, MainActivity act_Main) {
        this.lMesto = listMesto;
        this.context = c;
        this.actMain = act_Main;
    }

    @Override
    public viewHolder_mesto onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_element_seznama, viewGroup, false);


        return new viewHolder_mesto(itemView, context, actMain);
    }

    @Override
    public void onBindViewHolder(viewHolder_mesto viewHolder_mesto, int i) {

        //Iz dobljenega seznama vseh dodanih mest "lMesto" enega po enega nalagamo v ViewHolder ter vsakemu View-u posebej nastavljamo parametre Naziv ter temparaturo
        Mesto tmp = lMesto.get(i);

        viewHolder_mesto.tvNaziv.setText(tmp.getNaziv());
        viewHolder_mesto.tvTemparatura.setText(tmp.getTemparatura());
    }

    @Override
    public int getItemCount() {
        return lMesto.size();
    }

    public class viewHolder_mesto extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView tvNaziv;
        protected TextView tvTemparatura;
        Context c;
        MainActivity actMain;

        public viewHolder_mesto(View itemView, Context cnt, MainActivity act_Main) {
            super(itemView);
            c = cnt;
            actMain = act_Main;

            tvNaziv = (TextView)itemView.findViewById(R.id.tvMesto);
            tvTemparatura = (TextView)itemView.findViewById(R.id.tvTemp);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //Intent z informacijo o izbranem elementu
            TextView tvMesto = (TextView)v.findViewById(R.id.tvMesto);

            Intent intent = new Intent();
            //intent.putExtra("IzbranoMesto", tvMesto.getText().toString());
            intent.setClass(context, activity_info.class);

            actMain.vrniMesto(tvMesto.getText().toString());
            actMain.startActivity(intent);
        }
    }
}
