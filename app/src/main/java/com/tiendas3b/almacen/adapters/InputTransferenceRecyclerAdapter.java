package com.tiendas3b.almacen.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.fragments.InputTransferenceFragment.OnFragmentInteractionListener;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dfa on 01/04/2016.
 */
public class InputTransferenceRecyclerAdapter extends RecyclerView.Adapter<InputTransferenceRecyclerAdapter.InputTransferenceHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private final List<InputTransference> mValues;
    private final OnFragmentInteractionListener mListener;
    private final GlobalState mContext;
    private final SimpleDateFormat df;

    public InputTransferenceRecyclerAdapter(GlobalState context, List<InputTransference> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;

//        Collections.sort(mValues, new Comparator<InputTransference>() {
//            @Override
//            public int compare(InputTransference bd1, InputTransference bd2) {
//                return bd1.getTime().compareTo(bd2.getTime());
//            }
//        });
        df = new SimpleDateFormat(DateUtil.DD_MM, Locale.getDefault());
    }

    @Override
    public InputTransferenceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_input_transference, parent, false);
        return new InputTransferenceHolder(view);
    }

    @Override
    public void onBindViewHolder(final InputTransferenceHolder holder, int position) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onFragmentInteraction(holder.mItem);
                }
            }
        };
        holder.mItem = mValues.get(position);
        String folio = String.format("%d", holder.mItem.getFolio());
        String folio5 = folio.length() < 6 ? folio : folio.substring(0, 5);
        holder.txtFolio.setText(folio5);
        String storeName = holder.mItem.getStore() == null ? "N/A" : holder.mItem.getStore().getName();
        String storeName10 = storeName.length() < 11 ? storeName : storeName.substring(0, 10);
        holder.txtSend.setText(storeName10);
        holder.txtDate.setText(df.format(holder.mItem.getDate()));
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
//            holder.txtPlatform.setText(String.format("%d", holder.mItem.getPlatform()));
//            holder.txtTime.setText(holder.mItem.getTime());
//            holder.txtDeliveryTime.setText(holder.mItem.getDeliveryTime());
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//                holder.mView.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white, mContext.getTheme()));
//            } else {
//                holder.mView.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
//            }
//        }
        holder.txtFolio.setOnClickListener(clickListener);
        holder.txtSend.setOnClickListener(clickListener);
        holder.txtDate.setOnClickListener(clickListener);
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
        return mValues.get(pos).getStore().getName().substring(0,3);
    }

    public class InputTransferenceHolder extends RecyclerView.ViewHolder {
        public final CardView mView;
        public final EditText txtSend;
        public final EditText txtFolio;
        public final TextView txtDate;
        public InputTransference mItem;

        public InputTransferenceHolder(View view) {
            super(view);
            mView = (CardView) view;
            txtSend = (EditText) view.findViewById(R.id.txtSend);
            txtFolio = (EditText) view.findViewById(R.id.txtFolio);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
        }
    }
}
