package com.tiendas3b.almacen.shipment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.TripDetail;

import java.util.List;

public class TripDetailAdapter extends BaseAdapter {

    private int layout;
    private List<TripDetail> tripDetails;
    private Context context;

    public TripDetailAdapter(int layout, List<TripDetail> tripDetails, Context context) {
        this.layout = layout;
        this.tripDetails = tripDetails;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tripDetails.size();
    }

    @Override
    public TripDetail getItem(int position) {
        return tripDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tripDetails.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.storeName = convertView.findViewById(R.id.storeName);
            viewHolder.pallets = convertView.findViewById(R.id.pallets);
            viewHolder.icon = convertView.findViewById(R.id.tvIconTrip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final TripDetail currentTripDetail = getItem(position);

        viewHolder.title.setText("Tienda " + currentTripDetail.getStoreId());
        viewHolder.storeName.setText(currentTripDetail.getStoreDescription());
        viewHolder.pallets.setText("Tarimas: " + currentTripDetail.getPalletsNumber());
        viewHolder.icon.setBackgroundResource(getImageLayout(position, currentTripDetail.getStatus()));
        viewHolder.icon.setText(String.valueOf(currentTripDetail.getSequence()));
        return convertView;
    }

    private int getImageLayout(int position, int status) {
        if (tripDetails != null && tripDetails.size() == 1) {
            if (status == 1) {
                return R.drawable.ic_track_unique_started;
            } else if (status == 2) {
                return R.drawable.ic_track_unique_completed;
            } else {
                return R.drawable.ic_track_unique_not_yet_started;
            }
        } else {
            if (position == 0) {

                if (status == 0) {
                    return R.drawable.ic_track_start_not_yet_started;
                } else if (status == 1) {
                    return R.drawable.ic_track_start_started;
                } else if (status == 2) {
                    return R.drawable.ic_track_start_completed;
                }
                return R.drawable.ic_track_start_not_yet_started;
            } else if (position == tripDetails.size() - 1) {
                if (status == 0) {
                    return R.drawable.ic_track_finish_not_yet_started;
                } else if (status == 1) {
                    return R.drawable.ic_track_finish_started;
                } else if (status == 2) {
                    return R.drawable.ic_track_finish_completed;
                }
                return R.drawable.ic_track_finish_not_yet_started;
            } else {
                if (status == 0) {
                    return R.drawable.ic_track_not_yet_started;
                } else if (status == 1) {
                    return R.drawable.ic_track_started;
                } else if (status == 2) {
                    return R.drawable.ic_track_completed;
                }
                return R.drawable.ic_track_not_yet_started;
            }
        }
    }

    static class ViewHolder {
        private TextView icon;
        private TextView title;
        private TextView storeName;
        private TextView pallets;
    }
}
