package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tiendas3b.almacen.db.dao.ObservationType;

import java.util.List;

/**
 * Created by dfa on 03/05/2016.
 */
public class SpinnerAdapter extends ArrayAdapter<ObservationType>{

    private final List<ObservationType> types;

    public SpinnerAdapter(Context context, int resource, List<ObservationType> objects) {
        super(context, resource, objects);
        this.types = objects;
    }

    @Override
    public long getItemId(int position) {
        return types.get(position).getId();
    }

    @Override
    public ObservationType getItem(int position) {
        return types.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(types.get(position).getDescription());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(types.get(position).getDescription());
        return view;
    }

    @Override
    public int getPosition(ObservationType item) {
        return super.getPosition(item);
    }
}
