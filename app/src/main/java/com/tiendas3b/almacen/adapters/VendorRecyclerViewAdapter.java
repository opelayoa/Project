package com.tiendas3b.almacen.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.ReturnVendorCaptureActivity;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;

/**
 * Created by dfa on 01/04/2016.
 */
public class VendorRecyclerViewAdapter extends RecyclerView.Adapter<VendorRecyclerViewAdapter.MyHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private final List<Provider> mValues;
//    private final ReturnVendorActivity.OnListFragmentInteractionListener mListener;
    private final GlobalState mContext;

    public VendorRecyclerViewAdapter(GlobalState context, List<Provider> items) {
        mValues = items;
//        mListener = listener;
        mContext = context;

//        Collections.sort(mValues, new Comparator<Store>() {
//            @Override
//            public int compare(Store bd1, Store bd2) {
//                return bd1.getTime().compareTo(bd2.getTime());
//            }
//        });
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.mItem = mValues.get(position);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ReturnVendorCaptureActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("p", holder.mItem));
            }
        };
        holder.lblId.setText(String.valueOf(holder.mItem.getId()));
        holder.lblName.setText(holder.mItem.getName());
        holder.lblBusinessName.setText(holder.mItem.getBusinessName());
        holder.mView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return mValues.get(pos).getId().toString();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public final CardView mView;
        //        public final EditText txtProvider;
//        public final EditText txtFolio;
//        public final EditText txtPlatform;
//        public final EditText txtDeliveryTime;
//        public final EditText txtTime;
        public final TextView lblName;
        public final TextView lblBusinessName;
        public final TextView lblId;
        public Provider mItem;
//        public TimetableReceipt timetableReceipt;

        public MyHolder(View view) {
            super(view);
            mView = (CardView) view;
            lblName = (TextView) view.findViewById(R.id.lblName);
            lblBusinessName = (TextView) view.findViewById(R.id.lblBusinessName);
            lblId = (TextView) view.findViewById(R.id.lblId);
//            txtPlatform = (EditText) view.findViewById(R.id.txtPlatform);
//            txtTime = (EditText) view.findViewById(R.id.txtTime);
//            txtDeliveryTime = (EditText) view.findViewById(R.id.txtDeliveryTime);
        }
    }

    public void animateTo(List<Provider> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Provider> newModels) {
        for (int i = mValues.size() - 1; i >= 0; i--) {
            final Provider model = mValues.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Provider> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Provider model = newModels.get(i);
            if (!mValues.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Provider> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Provider model = newModels.get(toPosition);
            final int fromPosition = mValues.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Provider removeItem(int position) {
        final Provider model = mValues.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Provider model) {
        mValues.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Provider model = mValues.remove(fromPosition);
        mValues.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    //---------
}
