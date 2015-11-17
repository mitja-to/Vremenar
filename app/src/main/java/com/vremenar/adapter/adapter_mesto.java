package com.vremenar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vremenar.R;
import com.vremenar.data.Mesto;

import java.util.List;

/**
 * Created by Mitja on 17. 11. 2015.
 */
public class adapter_mesto extends RecyclerView.Adapter<adapter_mesto.viewHolder_mesto> {

    private List<Mesto> lMesto;

    public adapter_mesto(List<Mesto> listMesto) {
        this.lMesto = listMesto;
    }

    @Override
    public viewHolder_mesto onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_element_seznama, viewGroup, false);

        return new viewHolder_mesto(itemView);
    }

    @Override
    public void onBindViewHolder(viewHolder_mesto viewHolder_mesto, int i) {

        Mesto tmp = lMesto.get(i);

        viewHolder_mesto.tvNaziv.setText(tmp.getNaziv());
    }

    @Override
    public int getItemCount() {
        return lMesto.size();
    }

    public class viewHolder_mesto extends RecyclerView.ViewHolder {

        protected TextView tvNaziv;

        public viewHolder_mesto(View itemView) {
            super(itemView);
            tvNaziv = (TextView)itemView.findViewById(R.id.tvMesto);
        }
    }
}
