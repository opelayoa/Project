package com.tiendas3b.almacen.shipment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDao;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.util.DateUtil;

import java.util.List;

public class TripAdapter extends BaseAdapter {

    private int layout;
    private List<Trip> trips;
    private Context context;

    public TripAdapter(int layout, List<Trip> trips, Context context) {
        this.layout = layout;
        this.trips = trips;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trips.size();
    }

    @Override
    public Trip getItem(int position) {
        return trips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trips.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.branch = convertView.findViewById(R.id.branch);
            viewHolder.pallets = convertView.findViewById(R.id.pallets);
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.distance = convertView.findViewById(R.id.distance);
            viewHolder.icon = convertView.findViewById(R.id.tvIconTrip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Trip currentTrip = getItem(position);

        viewHolder.title.setText("Viaje " + currentTrip.getId());
        viewHolder.branch.setText("Tiendas: " + (currentTrip.getTripDetailList() != null ? currentTrip.getTripDetailList().size() : 0));
        int pallets = 0;
        if (currentTrip.getTripDetailList() != null) {
            for (TripDetail tripDetail : currentTrip.getTripDetailList()) {
                pallets += tripDetail.getPalletsNumber();
            }
        }
        viewHolder.pallets.setText("Tarimas: " + pallets);
        viewHolder.date.setText("Fecha: " + DateUtil.getDateStr(currentTrip.getDate()));
        viewHolder.distance.setText("Distancia: " + currentTrip.getRouteDistance() + " km");
        viewHolder.icon.setBackgroundResource(getImageLayout(position, currentTrip.getStatus()));
        viewHolder.icon.setText(String.valueOf(currentTrip.getSequence()));
        return convertView;
    }

    private int getImageLayout(int position, int status) {
        if (trips != null && trips.size() == 1) {
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
            } else if (position == trips.size() - 1) {
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
        private TextView branch;
        private TextView pallets;
        private TextView distance;
        private TextView date;
    }
}
