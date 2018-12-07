package com.tiendas3b.almacen.adapters;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.fragments.BuysFragment.OnFragmentInteractionListener;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;
import java.util.Locale;

/**
 * Created by dfa on 01/04/2016.
 */
public class BuyRecyclerViewAdapter extends RecyclerView.Adapter<BuyRecyclerViewAdapter.BuyHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private final List<Buy> mValues;
    private final OnFragmentInteractionListener mListener;
    private final GlobalState mContext;

    public BuyRecyclerViewAdapter(GlobalState context, List<Buy> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;

//        Collections.sort(mValues, new Comparator<Buy>() {
//            @Override
//            public int compare(Buy bd1, Buy bd2) {
//                return bd1.getTime().compareTo(bd2.getTime());
//            }
//        });
    }

    @Override
    public BuyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_buy, parent, false);
        return new BuyHolder(view);
    }

    @Override
    public void onBindViewHolder(final BuyHolder holder, int position) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onFragmentInteraction(holder.mItem);
                }
            }
        };

        holder.mItem = mValues.get(position);
//        Log.d("ID:", holder.mItem.getId().toString());
        holder.txtFolio.setText(String.format(Locale.getDefault(), "%d", holder.mItem.getFolio()));
        Provider provider = holder.mItem.getProvider();
        if(provider == null){
            holder.txtProvider.setText("NA");
        } else {
            holder.txtProvider.setText(provider.getName());
        }

//        if(holder.mItem.getChecked() == Constants.ARRIVE){
//            holder.txtReceived.setText(R.string.received);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.txtReceived.setTextColor(mContext.getResources().getColor(R.color.received, mContext.getTheme()));
//            } else {
//                holder.txtReceived.setTextColor(mContext.getResources().getColor(R.color.received));
//            }
//        } else if(holder.mItem.getChecked() == Constants.IN_PROCESS) {
//            holder.txtReceived.setText(R.string.in_process);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.txtReceived.setTextColor(mContext.getResources().getColor(R.color.in_process, mContext.getTheme()));
//            } else {
//                holder.txtReceived.setTextColor(mContext.getResources().getColor(R.color.in_process));
//            }
//        } else {
//            holder.txtReceived.setText(R.string.no_received);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.txtReceived.setTextColor(mContext.getResources().getColor(R.color.no_received, mContext.getTheme()));
//            } else {
//                holder.txtReceived.setTextColor(mContext.getResources().getColor(R.color.no_received));
//            }
//        }

//        if (holder.mItem.getDay() == -1) {
//            holder.txtPlatform.setText(R.string.no_timetable);
//            holder.txtTime.setText(R.string.no_timetable);
//            holder.txtDeliveryTime.setText(R.string.no_timetable);
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//                holder.mView.setCardBackgroundColor(mContext.getResources().getColor(R.color.color_error, mContext.getTheme()));
//            } else {
//                holder.mView.setCardBackgroundColor(mContext.getResources().getColor(R.color.color_error));
//            }
//        } else {
            holder.txtPlatform.setText(String.format(Locale.getDefault(), "%d", holder.mItem.getPlatform()));
            holder.txtTime.setText(holder.mItem.getTime());
            holder.txtDeliveryTime.setText(holder.mItem.getDeliveryTime());
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                holder.mView.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white, mContext.getTheme()));
            } else {
                holder.mView.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            }
//        }
        holder.txtFolio.setOnClickListener(clickListener);
        holder.txtProvider.setOnClickListener(clickListener);
        holder.txtPlatform.setOnClickListener(clickListener);
        holder.txtTime.setOnClickListener(clickListener);
        holder.txtDeliveryTime.setOnClickListener(clickListener);
        holder.txtReceived.setOnClickListener(clickListener);
        holder.mView.setOnClickListener(clickListener);
    }

//    private TimetableReceipt getTimetable(long providerId) {
//        if(timetable != null){
//            for (TimetableReceipt t : timetable) {
//                if(t.getProviderId() == providerId){
//                    return t;
//                }
//            }
//        }
//        return null;
//    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return mValues.get(pos).getTime();
    }

    public class BuyHolder extends RecyclerView.ViewHolder {
        public final CardView mView;
        public final EditText txtProvider;
        public final EditText txtFolio;
        public final TextView txtReceived;
        public final EditText txtPlatform;
        public final EditText txtDeliveryTime;
        public final EditText txtTime;
        public Buy mItem;
//        public TimetableReceipt timetableReceipt;

        public BuyHolder(View view) {
            super(view);
            mView = (CardView) view;
            txtProvider = (EditText) view.findViewById(R.id.txtProvider);
            txtReceived = (TextView) view.findViewById(R.id.txtReceived);
            txtFolio = (EditText) view.findViewById(R.id.txtFolio);
            txtPlatform = (EditText) view.findViewById(R.id.txtPlatform);
            txtTime = (EditText) view.findViewById(R.id.txtTime);
            txtDeliveryTime = (EditText) view.findViewById(R.id.txtDeliveryTime);
        }
    }
}
