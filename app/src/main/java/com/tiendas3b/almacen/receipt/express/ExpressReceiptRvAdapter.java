package com.tiendas3b.almacen.receipt.express;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;

/**
 * Created by dfa on 11/08/2016.
 */
public class ExpressReceiptRvAdapter extends BaseRecyclerViewAdapter<ExpressReceipt> {

//    private EpackageActivity activity;

    public ExpressReceiptRvAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
//        activity = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.AppTheme);
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_express_receipt, viewGroup, false);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.row_epackage, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(ExpressReceipt item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            EditText txtBarcode = (EditText) viewHolder.getView(R.id.txtBarcode);
//            EditText txtProvider = (EditText) viewHolder.getView(R.id.txtProvider);
//            EditText txtFolio = (EditText) viewHolder.getView(R.id.txtFolio);
//            EditText txtPlatform = (EditText) viewHolder.getView(R.id.txtPlatform);
//            EditText txtTime = (EditText) viewHolder.getView(R.id.txtTime);
//            EditText txtDeliveryTime = (EditText) viewHolder.getView(R.id.txtDeliveryTime);

            txtBarcode.setText(item.getBarcode());
//            BuyDetail det = item.getBuyDetail();
//            Buy buy = det.getBuy();
//            txtProvider.setText(String.valueOf(buy.getProvider().getBusinessName()));//
//            txtFolio.setText(String.valueOf(buy.getFolio()));
//            txtPlatform.setText(String.valueOf(buy.getPlatform()));
//            txtTime.setText(buy.getTime());
//            txtDeliveryTime.setText(buy.getDeliveryTime());
        }
    }
}
