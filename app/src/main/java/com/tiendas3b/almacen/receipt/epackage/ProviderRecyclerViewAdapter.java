package com.tiendas3b.almacen.receipt.epackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.picking.epackage.EpackageActivity;

/**
 * Created by dfa on 11/08/2016.
 */
public class ProviderRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProviderEpackage> {

    private IDatabaseManager db;
    private String dateStr;

    public ProviderRecyclerViewAdapter(Context context, IDatabaseManager datbase, String date, OnViewHolderClick listener) {
        super(context, listener);
        db = datbase;
        dateStr = date;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.row_vendor_epackage, viewGroup, false);
    }

    @Override
    protected void bindView(ProviderEpackage item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            TextView lblId = (TextView) viewHolder.getView(R.id.lblId);
            TextView lblName = (TextView) viewHolder.getView(R.id.lblName);
            TextView lblQuantityPackages = (TextView) viewHolder.getView(R.id.lblQuantityPackages);

            long id = item.getId();
            lblId.setText(String.valueOf(id));
            lblName.setText(item.getName());
            //lblQuantityPackages.setText(String.valueOf((db.listEPackages(id, EpackageActivity.RECEIPT_PENDING, dateStr).size()) + ((db.listEPackages(id, EpackageActivity.RECEIPT_RECEIVED, dateStr)) == null ? 0 :(db.listEPackages(id, EpackageActivity.RECEIPT_RECEIVED, dateStr).size())) + " / " + String.valueOf(db.listEPackages(id, EpackageActivity.RECEIPT_PENDING, dateStr).size())));
            lblQuantityPackages.setText((String.valueOf(db.listEPackages(id, EpackageActivity.RECEIPT_PENDING, dateStr).size())) + " / " + String.valueOf((db.listEPackages(id, EpackageActivity.RECEIPT_PENDING, dateStr).size()) + ((db.listEPackages(id, EpackageActivity.RECEIPT_RECEIVED, dateStr)) == null ? 0 :(db.listEPackages(id, EpackageActivity.RECEIPT_RECEIVED, dateStr).size()))));
        }
    }

    public void setDate(String date) {
        this.dateStr = date;
    }

    public void setDb(IDatabaseManager db) {
        this.db = db;
    }
}
