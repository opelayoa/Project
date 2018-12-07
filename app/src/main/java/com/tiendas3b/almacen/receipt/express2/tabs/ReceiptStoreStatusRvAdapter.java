package com.tiendas3b.almacen.receipt.express2.tabs;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

/**
 * Created by dfa on 01/04/2016.
 */
public class ReceiptStoreStatusRvAdapter extends BaseRecyclerViewAdapter<Buy>   implements RecyclerViewFastScroller.BubbleTextGetter, View.OnClickListener {

    //    private final List<StorePickingStatus> mValues;
//    private final OnListFragmentInteractionListener mListener;
    private final GlobalState mContext;
    private View.OnClickListener listener;

    public ReceiptStoreStatusRvAdapter(GlobalState context,OnViewHolderClick listener  /*, OnListFragmentInteractionListener listener*/) {
        super(context, listener);
        mContext = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.AppTheme);
//        View view = LayoutInflater.from(ctx).inflate(R.layout.row_store, viewGroup, false);
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_buy_express, viewGroup, false);

//        view.setOnClickListener(this);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.row_epackage, viewGroup, false);
        return view;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
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
                    txtProvider.setText("CARGANDO");
                } else {
                    ConfigProvider config = provider.getMmpConfig();

                    if (config == null) {

                            txtProvider.setText(provider.getName());
//                lbl.setText("Reportar prov:" + provider.getId());
                    } else {

                        if(config.getP2() == 0){
                            txtProvider.setText("S/C " + provider.getName());
                        }else{
                            if(provider.getName().length() >= 7){
                                txtProvider.setText((config.getP2() == 1 ? "PAL" : "AGR") + " " + provider.getName());
                            }else {
                                txtProvider.setText((config.getP2() == 1 ? "PAL" : "AGR") + " " + provider.getName() + "   ");
                            }
                        }

                    }
//            lbl.setVisibility(View.VISIBLE);

                    txtFolio.setText(String.valueOf(item.getFolio()));
                    txtPlatform.setText(String.valueOf(item.getPlatform()));
                    txtTime.setText(item.getTime());
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                txtProvider.setText("CARGANDO");
            }

        }
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }

//    @Override
//    protected void bindView(Buy item, ViewHolder viewHolder) {
//        if (item != null) {
//            viewHolder.setItem(item);
//            TextView lblStoreName = (TextView) viewHolder.getView(R.id.lblStoreName);
//            lblStoreName.setText(item.getStore().getName());
//        }
//    }

//    @Override
//    public StoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store, parent, false);
//        return new StoreHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final StoreHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.lblStoreName.setText(holder.mItem.getStore().getName());
////        holder.lblAddress.setText(holder.mItem.getAddress());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }

    @Override
    public String getTextToShowInBubble(int pos) {
//        return getList().get(pos).getStore().getName().substring(0, 3);
        return getList().get(pos).getProvider().getName();
    }

//    public class StoreHolder extends RecyclerView.ViewHolder {
//        public final CardView mView;
////        public final EditText txtProvider;
////        public final EditText txtFolio;
////        public final EditText txtPlatform;
////        public final EditText txtDeliveryTime;
////        public final EditText txtTime;
//        public final TextView lblStoreName;
////        public final TextView lblAddress;
//        public StorePickingStatus mItem;
////        public TimetableReceipt timetableReceipt;
//
//        public StoreHolder(View view) {
//            super(view);
//            mView = (CardView) view;
//            lblStoreName = view.findViewById(R.id.lblStoreName);
////            lblAddress = view.findViewById(R.id.lblAddress);
////            txtPlatform = (EditText) view.findViewById(R.id.txtPlatform);
////            txtTime = (EditText) view.findViewById(R.id.txtTime);
////            txtDeliveryTime = (EditText) view.findViewById(R.id.txtDeliveryTime);
//        }
//    }
}
