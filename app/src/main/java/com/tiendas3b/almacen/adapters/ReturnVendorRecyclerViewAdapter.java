package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;

/**
 * Created by dfa on 11/08/2016.
 */
public class ReturnVendorRecyclerViewAdapter extends BaseRecyclerViewAdapter<Provider> implements RecyclerViewFastScroller.BubbleTextGetter{

    public ReturnVendorRecyclerViewAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_vendor, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(Provider item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);

//            mView = (CardView) view;
            TextView lblName = (TextView) viewHolder.getView(R.id.lblName);
            TextView lblBusinessName = (TextView) viewHolder.getView(R.id.lblBusinessName);
            TextView lblId = (TextView) viewHolder.getView(R.id.lblId);

//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.startActivity(new Intent(mContext, ReturnVendorCaptureActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("p", holder.mItem));
//                }
//            };
            lblId.setText(String.valueOf(item.getId()));
            lblName.setText(item.getName());
            lblBusinessName.setText(item.getBusinessName());
//            mView.setOnClickListener(clickListener);

        }
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return getList().get(pos).getId().toString();
    }

    public void animateTo(List<Provider> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Provider> newModels) {
        for (int i = getList().size() - 1; i >= 0; i--) {
            final Provider model = getList().get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Provider> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Provider model = newModels.get(i);
            if (!getList().contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Provider> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Provider model = newModels.get(toPosition);
            final int fromPosition = getList().indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Provider removeItem(int position) {
        final Provider model = getList().remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Provider model) {
        getList().add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Provider model = getList().remove(fromPosition);
        getList().add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
