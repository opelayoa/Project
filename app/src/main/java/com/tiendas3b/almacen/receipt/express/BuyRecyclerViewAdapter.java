package com.tiendas3b.almacen.receipt.express;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

/**
 * Created by dfa on 01/04/2016.
 */
public class BuyRecyclerViewAdapter extends BaseRecyclerViewAdapter<Buy> {

    //    private static final String TAG = "BuyRvAdapter";
    private IDatabaseManager db;

    public void setMalVersion(boolean malVersion) {
        this.malVersion = malVersion;
    }

    private boolean malVersion;

    public BuyRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    @SuppressLint("RestrictedApi")
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
//        Log.e(TAG, "createView");
        ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.AppTheme);
        return LayoutInflater.from(ctx).inflate(R.layout.row_buy_express, viewGroup, false);
    }

    @Override
    protected void bindView(Buy item, ViewHolder viewHolder) {
//        Log.e(TAG, "bindView");
        if (item != null) {
            viewHolder.setItem(item);
            EditText txtProvider = (EditText) viewHolder.getView(R.id.txtProvider);
            EditText txtFolio = (EditText) viewHolder.getView(R.id.txtFolio);
            EditText txtPlatform = (EditText) viewHolder.getView(R.id.txtPlatform);
            EditText txtTime = (EditText) viewHolder.getView(R.id.txtTime);
//            EditText txtDeliveryTime = (EditText) viewHolder.getView(R.id.txtDeliveryTime);
//            TextView lbl = (TextView) viewHolder.getView(R.id.txtReceived);

            try {
                Provider provider = item.getProvider();
                if (provider == null) {
                    txtProvider.setText("NA");
                } else {
                    if (malVersion) {
                        ExpressProvider ep = db.findExpressProvider(provider.getId());
                        if (ep == null) {
                            txtProvider.setText(provider.getName());
                        } else {
                            boolean automatic = ep.getAutomatic();
                            txtProvider.setText((automatic ? ("G".equals(ep.getDelivery()) ? "AG" : "PL")
                                    : ("G".equals(ep.getDelivery()) ? "AG" : "EX")) + " " + provider.getName());
                        }
                    } else {
                        ConfigProvider config = provider.getMmpConfig();

                        if (config == null) {
//                lbl.setText("Reportar prov:" + provider.getId());
                            txtProvider.setText(provider.getName());
                        } else {
                            txtProvider.setText((config.getP2() == 1 ? "EX" : "AG") + " " + provider.getName());
                        }
//            lbl.setVisibility(View.VISIBLE);

                    }
                    txtFolio.setText(String.valueOf(item.getFolio()));
                    txtPlatform.setText(String.valueOf(item.getPlatform()));
                    txtTime.setText(item.getTime());
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                txtProvider.setText("NO DAO");
            }

        }
    }

    public void setDb(IDatabaseManager db) {
        this.db = db;
    }
}
