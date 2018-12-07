package com.tiendas3b.almacen.shipment.epackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.epackage.EpackageActivity;

/**
 * Created by dfa on 11/08/2016.
 */
public class StoresRecyclerViewAdapter extends BaseRecyclerViewAdapter<Store> {

    private IDatabaseManager db;
    private String dateStr;

    public StoresRecyclerViewAdapter(Context context, IDatabaseManager datbase, String date, OnViewHolderClick listener) {
        super(context, listener);
        db = datbase;
        dateStr = date;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.row_store, viewGroup, false);
    }

    @Override
    protected void bindView(Store item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            TextView lblStoreName = (TextView) viewHolder.getView(R.id.lblStoreName);
            TextView lblQuantityPackages = (TextView) viewHolder.getView(R.id.lblQuantityPackages);

            long id = item.getId();
            lblStoreName.setText(item.getName());
            //System.out.println("**********************Adapter: " + id);
            //System.out.println("**********************Adapter db.listEPackages: " + (db.listEPackagesStore(id, EpackageActivity.PICKING_CAGE_IN, dateStr)));
            //lblQuantityPackages.setText((String.valueOf(db.listEPackages(id, EpackageActivity.RECEIPT_PENDING, dateStr).size())) + " / " + String.valueOf((db.listEPackages(id, EpackageActivity.RECEIPT_PENDING, dateStr).size()) + ((db.listEPackages(id, EpackageActivity.RECEIPT_RECEIVED, dateStr)) == null ? 0 :(db.listEPackages(id, EpackageActivity.RECEIPT_RECEIVED, dateStr).size()))));
            lblQuantityPackages.setText(String.valueOf(((db.listEPackagesStore(id, EpackageActivity.PICKING_CAGE_OUT, dateStr)) == null ? 0 :(db.listEPackagesStore(id, EpackageActivity.PICKING_CAGE_OUT, dateStr).size()))));
        }
    }

    public void setDate(String date) {
        this.dateStr = date;
    }

    public void setDb(IDatabaseManager db) {
        this.db = db;
    }
}
