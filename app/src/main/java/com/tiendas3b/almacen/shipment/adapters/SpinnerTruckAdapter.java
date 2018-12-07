package com.tiendas3b.almacen.shipment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.Truck;

import java.util.List;

public class SpinnerTruckAdapter extends BaseAdapter {

    private List<Truck> trucks;
    private int layout;
    private Context context;

    public SpinnerTruckAdapter(List<Truck> trucks, int layout, Context context) {
        this.trucks = trucks;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trucks.size();
    }

    @Override
    public Truck getItem(int position) {
        return trucks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trucks.get(position).getTruckId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerTruckAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new SpinnerTruckAdapter.ViewHolder();
            viewHolder.description = convertView.findViewById(R.id.textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpinnerTruckAdapter.ViewHolder) convertView.getTag();
        }


        final Truck currentTruck = getItem(position);

        viewHolder.description.setText(currentTruck.getDescription());
        return convertView;
    }

    static class ViewHolder {
        private TextView description;
    }
}
