package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Roadmap;

/**
 * Created by dfa on 11/08/2016.
 */
public class RoadmapRecyclerViewAdapter extends BaseRecyclerViewAdapter<Roadmap> {

    public RoadmapRecyclerViewAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_travel, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(Roadmap item, BaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            TextView lblDate = (TextView) viewHolder.getView(R.id.lblDate);
            TextView lblTravel = (TextView) viewHolder.getView(R.id.lblTravel);
//            TextView lblTruck = (TextView) viewHolder.getView(R.id.lblTruck);
            TextView lblDriver = (TextView) viewHolder.getView(R.id.lblDriver);

            lblDate.setText(item.getDate());
            lblTravel.setText("Viaje: " + item.getTravel());
//            lblTruck.setText("Camión: " + ((Roadmap) viewHolder.getItem()).getTruck().getDescription());
            lblDriver.setText("Operador: " + item.getDriver().getDescription());
        }
    }
}
