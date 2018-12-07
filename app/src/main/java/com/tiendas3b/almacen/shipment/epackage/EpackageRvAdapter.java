package com.tiendas3b.almacen.shipment.epackage;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.shipment.epackage.EpackageListener;

import mbanje.kurt.fabbutton.FabButton;

/**
 * Created by dfa on 11/08/2016.
 */
public class EpackageRvAdapter extends BaseRecyclerViewAdapter<EPackage> {

    private final EpackageListener listener;

    //    private EpackageActivity activity;
//
//    public EpackageRvAdapter(EpackageActivity context, OnViewHolderClick listener) {
//        super(context, listener);
//        activity = context;
//    }
    public EpackageRvAdapter(Context context, EpackageListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.AppTheme);
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_epackage_truck_in, viewGroup, false);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.row_epackage, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(final EPackage item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            EditText txtProvider = (EditText) viewHolder.getView(R.id.txtProvider);
            EditText txtFolio = (EditText) viewHolder.getView(R.id.txtFolio);
            //EditText txtArticleId = (EditText) viewHolder.getView(R.id.txtArticleId);
            EditText txtBarcode = (EditText) viewHolder.getView(R.id.txtBarcode);
            EditText txtPacking = (EditText) viewHolder.getView(R.id.txtPacking);
            EditText txtDescription = (EditText) viewHolder.getView(R.id.txtDescription);


            txtProvider.setText(String.valueOf(item.getProviderId()));
            txtFolio.setText(item.getFolio());
            //txtArticleId.setText(String.valueOf(item.getArticleId()));
            txtBarcode.setText(item.getBarcode());
            txtPacking.setText(item.getUnity());
            txtDescription.setText(item.getDescription());
            final FabButton btn = (FabButton) viewHolder.getView(R.id.btnAction);
            btn.showProgress(false);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn.showProgress(true);
                    listener.onClick(item, btn);
                }
            });
        }
    }

//    private void deleteItem(int position) {
//        mDataSet.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, mDataSet.size());
//        holder.itemView.setVisibility(View.GONE);
//    }

}
