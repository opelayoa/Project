package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

/**
 * Created by dfa on 01/04/2016.
 */
public class StoreRvAdapter extends BaseRecyclerViewAdapter<Store> implements RecyclerViewFastScroller.BubbleTextGetter {

    public StoreRvAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
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
//            TextView lblAddress = (TextView) viewHolder.getView(R.id.lblAddress);

            lblStoreName.setText(item.getName());
//            lblAddress.setText(item.getActivity().getDescription());
        }
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return getItem(pos).getName().substring(0, 3);
    }


//    private final List<Store> mValues;
////    private final OnListFragmentInteractionListener mListener;
//    private final GlobalState mContext;
//
//    public StoreRvAdapter(GlobalState context, List<Store> items/*, OnListFragmentInteractionListener listener*/) {
//        mValues = items;
////        mListener = listener;
//        mContext = context;
//
////        Collections.sort(mValues, new Comparator<Store>() {
////            @Override
////            public int compare(Store bd1, Store bd2) {
////                return bd1.getTime().compareTo(bd2.getTime());
////            }
////        });
//    }
//
//    @Override
//    public StoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store, parent, false);
//        return new StoreHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final StoreHolder holder, int position) {
//        View.OnClickListener clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, OutputTransferenceCaptureActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("p", holder.mItem));
////                if (null != mListener) {
////                    mListener.onListFragmentInteraction(holder.mItem);
////                }
//            }
//        };
//        holder.mItem = mValues.get(position);
//        holder.lblStoreName.setText(holder.mItem.getName());
//        holder.lblAddress.setText(holder.mItem.getAddress());
//        holder.mView.setOnClickListener(clickListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }
//
//    @Override
//    public String getTextToShowInBubble(int pos) {
//        return mValues.get(pos).getName().substring(0, 3);
//    }
//
//    public class StoreHolder extends RecyclerView.ViewHolder {
//        public final CardView mView;
////        public final EditText txtProvider;
////        public final EditText txtFolio;
////        public final EditText txtPlatform;
////        public final EditText txtDeliveryTime;
////        public final EditText txtTime;
//        public final TextView lblStoreName;
//        public final TextView lblAddress;
//        public Store mItem;
////        public TimetableReceipt timetableReceipt;
//
//        public StoreHolder(View view) {
//            super(view);
//            mView = (CardView) view;
//            lblStoreName = (TextView) view.findViewById(R.id.lblStoreName);
//            lblAddress = (TextView) view.findViewById(R.id.lblAddress);
////            txtPlatform = (EditText) view.findViewById(R.id.txtPlatform);
////            txtTime = (EditText) view.findViewById(R.id.txtTime);
////            txtDeliveryTime = (EditText) view.findViewById(R.id.txtDeliveryTime);
//        }
//    }


}
