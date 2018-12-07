package com.tiendas3b.almacen.shipment.vrp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.shipment.vrp.dto.DesmadreDTO;

/**
 * Created by dfa on 01/04/2016.
 */
public class TravelsRvAdapter extends BaseRecyclerViewAdapter<DesmadreDTO> {

    public TravelsRvAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.row_travel2, viewGroup, false);
    }

    @Override
    protected void bindView(DesmadreDTO item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            TextView lblTravel = (TextView) viewHolder.getView(R.id.lblTravel);
            TextView lblOrigin = (TextView) viewHolder.getView(R.id.lblOrigin);
            TextView lblDestination = (TextView) viewHolder.getView(R.id.lblDestination);

            lblTravel.setText(String.valueOf(item.getIdviaje()));
            lblOrigin.setText(String.valueOf(item.getOrigen()));
            lblDestination.setText(String.valueOf(item.getDestino()));
        }
    }

}
