package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;

/**
 *
 * Created by dfa on 11/08/2016.
 */
public class RoadmapDetailRecyclerViewAdapter extends BaseRecyclerViewAdapter<RoadmapDetail> {

    public RoadmapDetailRecyclerViewAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.row_roadmap_detail, viewGroup, false);
    }

    @Override
    protected void bindView(RoadmapDetail item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            TextView lblTime = (TextView) viewHolder.getView(R.id.lblTime);
            TextView lblActivity = (TextView) viewHolder.getView(R.id.lblActivity);
            TextView lblStores = (TextView) viewHolder.getView(R.id.lblStores);
            TextView lblKm = (TextView) viewHolder.getView(R.id.lblKm);

            lblTime.setText(item.getInitialTime() + "-" + item.getFinalTime());
            lblActivity.setText(item.getActivity().getDescription());
            lblStores.setText("Origen: " + item.getOrigin().getId() + "  Destino: " + item.getDestination().getId());
            lblKm.setText("Km:" + item.getInitialKm() + "-" + item.getFinalKm());
        }
    }
}
